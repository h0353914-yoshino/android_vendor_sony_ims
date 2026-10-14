#!/usr/bin/env python3
"""以 dexdump -d 輸出 + 原始 classes.dex 位元組建立每個方法的控制流，
比較兩個 dex 中「事件」之間的先後邊集合，用來找 JADX 還原錯誤（缺 return、
try/catch 範圍錯、分支搬位等）。用法：
  cmp_cfg.py orig.dump orig.dex new.dump new.dex [顯示筆數] [類別/方法過濾 regex]
"""
import re, sys, struct, collections

INS = re.compile(r"^([0-9a-f]{6}):\s+((?:[0-9a-f]{4}\s?)*)\s*(?:\.\.\.)?\s*\|([0-9a-f]{4}): (\S+)(?: (.*))?$")
CLS = re.compile(r"\s+#\d+\s+:\s+\(in (.*)\)")
NAME = re.compile(r"\s+name\s+:\s+'(.*)'")
TYPE = re.compile(r"\s+type\s+:\s+'(\(.*)'")
RANGE = re.compile(r"\s+0x([0-9a-f]+) - 0x([0-9a-f]+)\s*$")
HAND = re.compile(r"\s+(L[^;]+;|<any>) -> 0x([0-9a-f]+)")

SKIP_CLS = ('Ljava/lang/StringBuilder;', 'Ljava/lang/StringBuffer;')


def parse(dump_path, dex_path):
    dex = open(dex_path, 'rb').read()
    methods = {}
    cls = meth = cur = None
    ins = ranges = None
    rng = None

    def finish():
        if cur is not None:
            methods[cur] = (ins, ranges)

    for line in open(dump_path, errors='replace'):
        line = line.rstrip('\n')
        m = CLS.match(line)
        if m:
            finish(); cur = None; cls = m.group(1); continue
        m = NAME.match(line)
        if m:
            meth = m.group(1); continue
        m = TYPE.match(line)
        if m and cls:
            finish(); cur = cls + '.' + meth + m.group(1); ins = []; ranges = []; rng = None; continue
        if cur is None:
            continue
        m = RANGE.match(line)
        if m:
            rng = [int(m.group(1), 16), int(m.group(2), 16), []]; ranges.append(rng); continue
        m = HAND.match(line)
        if m and rng is not None:
            rng[2].append((m.group(1), int(m.group(2), 16))); continue
        m = INS.match(line)
        if m:
            foff = int(m.group(1), 16)
            addr = int(m.group(3), 16)
            ins.append((addr, m.group(4), (m.group(5) or ''), foff))
    finish()
    return methods, dex


def norm_target(t):
    t = re.sub(r'\.-\$\$Nest\$(?:sm|sf|m|f)', '.', t)
    t = t.replace('Landroid/os/IHwBinder;.hashCode', 'Ljava/lang/Object;.hashCode').replace('Ljava/lang/Integer;.toString', 'Ljava/lang/Object;.toString')
    t = re.sub(r':\(.*', '', t)
    t = t.replace('Ljava/lang/String;.equals', 'Ljava/lang/Object;.equals')
    t = t.replace('Ljava/lang/String;.hashCode', 'Ljava/lang/Object;.hashCode')
    t = re.sub(r'\$\$ExternalSyntheticLambda\d+|\$AnonymousClass\d+|\$\d+(?=[;.])', '$X', t)
    t = re.sub(r'\.m\d+lambda\$', '.lambda$', t)
    t = t.replace('-', '')
    return t


def label(op, rest):
    body = rest.split(' // ')[0]
    if op.startswith('invoke'):
        t = body.split('}, ', 1)[-1]
        if any(t.startswith(c) for c in SKIP_CLS):
            return None
        if t.startswith('Lcom/qualcomm/ims/utils/Log;'):
            return None  # 日誌呼叫不影響邏輯，且常被常數折疊
        return 'call ' + norm_target(t)
    if op.startswith(('iput', 'sput')):
        f = body.split(', ')[-1]
        if 'this$0' in f or 'val$' in f or '$SwitchMap' in f:
            return None
        return 'put ' + norm_target(f)
    if op == 'new-instance':
        t = body.split(', ')[-1]
        if t in SKIP_CLS:
            return None
        return 'new ' + norm_target(t)
    if op.startswith('return'):
        return 'RET'
    if op == 'throw':
        return 'THROW'
    if op.startswith('monitor'):
        return op
    return None


def build(methods, dex, key):
    ins, ranges = methods[key]
    if not ins:
        return None
    byaddr = {a: i for i, (a, _, _, _) in enumerate(ins)}
    succ = [[] for _ in ins]
    for i, (a, op, rest, foff) in enumerate(ins):
        nxt = ins[i + 1][0] if i + 1 < len(ins) else None
        falls = True
        if op.startswith('return') or op == 'throw':
            falls = False
        elif op.startswith('goto'):
            m = re.search(r'// ([+-][0-9a-f]+)', rest)
            if m:
                succ[i].append(a + int(m.group(1), 16))
            falls = False
        elif op.startswith('if-'):
            m = re.search(r'// ([+-][0-9a-f]+)', rest)
            if m:
                succ[i].append(a + int(m.group(1), 16))
        elif op in ('packed-switch', 'sparse-switch'):
            m = re.search(r'// ([+-][0-9a-f]+)', rest)
            if m:
                poff = a + int(m.group(1), 16)
                # 找 payload 指令的檔案位移
                pi = byaddr.get(poff)
                if pi is not None:
                    pfoff = ins[pi][3]
                    ident = struct.unpack_from('<H', dex, pfoff)[0]
                    size = struct.unpack_from('<H', dex, pfoff + 2)[0]
                    if ident == 0x0100:
                        tg = struct.unpack_from('<%di' % size, dex, pfoff + 8)
                    elif ident == 0x0200:
                        tg = struct.unpack_from('<%di' % size, dex, pfoff + 4 + 4 * size)
                    else:
                        tg = ()
                    for t in tg:
                        succ[i].append(a + t)
        elif op.endswith('-data') or op == 'nop' and False:
            falls = False
        if falls and nxt is not None:
            succ[i].append(nxt)
        # 例外邊
        for (s, e, hs) in ranges:
            if s <= a < e:
                for (_t, h) in hs:
                    succ[i].append(h)
    labels = [label(op, rest) for (_, op, rest, _) in ins]
    sidx = [[byaddr[x] for x in s if x in byaddr] for s in succ]
    edges = set()

    def nextevents(start_list):
        out = set(); seen = set(); stack = list(start_list)
        while stack:
            j = stack.pop()
            if j in seen:
                continue
            seen.add(j)
            if labels[j] is not None:
                out.add(labels[j]); continue
            stack.extend(sidx[j])
        return out

    for e in nextevents([0]):
        edges.add(('START', e))
    for i, l in enumerate(labels):
        if l is None or l in ('RET', 'THROW'):
            continue
        for e in nextevents(sidx[i]):
            edges.add((l, e))
    return edges


def main():
    od, ox, nd, nx = sys.argv[1:5]
    top = int(sys.argv[5]) if len(sys.argv) > 5 else 40
    flt = re.compile(sys.argv[6]) if len(sys.argv) > 6 else None
    A, adex = parse(od, ox)
    B, bdex = parse(nd, nx)
    common = sorted(set(A) & set(B))
    bad = []
    for k in common:
        if flt and not flt.search(k):
            continue
        ea = build(A, adex, k); eb = build(B, bdex, k)
        if ea is None or eb is None:
            continue
        if ea != eb:
            d1 = ea - eb; d2 = eb - ea
            if '.<init>(' in k:
                d1 = {e for e in d1 if not (e[0].startswith(('put','call L')) and e[1].startswith(('put','RET')))}
                d2 = {e for e in d2 if not (e[0].startswith(('put','call L')) and e[1].startswith(('put','RET')))}
                if not d1 and not d2: continue
            bad.append((len(d1) + len(d2), k, d1, d2))
    bad.sort(reverse=True, key=lambda x: x[0])
    print('compared', len(common), 'methods; differing:', len(bad))
    for n, k, d1, d2 in bad[:top]:
        print(n, k)
        for e in sorted(d1)[:4]:
            print('   -', e[0][:70], '->', e[1][:70])
        for e in sorted(d2)[:4]:
            print('   +', e[0][:70], '->', e[1][:70])



def anon_report(od, ox, nd, nx, top=30):
    A, adex = parse(od, ox); B, bdex = parse(nd, nx)
    isanon = lambda k: bool(re.search(r'\$(\d+|AnonymousClass\d+|\$ExternalSyntheticLambda\d+);\.', k))
    def sigs(M, dex):
        byc = collections.defaultdict(list)
        for k in M:
            if not isanon(k):
                continue
            cls, m = k.split(';.', 1)
            name = m.split('(')[0]
            if name == '<init>' or name == '<clinit>':
                continue
            e = build(M, dex, k)
            if e is None:
                continue
            outer = re.sub(r'\$(\d+|AnonymousClass\d+|\$ExternalSyntheticLambda\d+)$', '', cls)
            byc[cls].append((name, frozenset(e)))
        c = collections.Counter()
        for cls, lst in byc.items():
            outer = re.sub(r'\$(\d+|AnonymousClass\d+|\$ExternalSyntheticLambda\d+)$', '', cls)
            c[(outer, tuple(sorted((n, tuple(sorted(e))) for n, e in lst)))] += 1
        return c
    a = sigs(A, adex); b = sigs(B, bdex)
    print('anon classes orig', sum(a.values()), 'new', sum(b.values()))
    da = a - b; db = b - a
    print('unmatched orig', sum(da.values()), 'unmatched new', sum(db.values()))
    for (o, lst), n in list(da.items())[:top]:
        print('  - ', o.split('/')[-1], [x[0] for x in lst][:3], 'edges', sum(len(x[1]) for x in lst))
    for (o, lst), n in list(db.items())[:top]:
        print('  + ', o.split('/')[-1], [x[0] for x in lst][:3], 'edges', sum(len(x[1]) for x in lst))

if len(sys.argv) > 7 and sys.argv[7] == 'anon':
    anon_report(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
else:
    main()
