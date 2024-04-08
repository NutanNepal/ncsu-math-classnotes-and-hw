R.<x> = PolynomialRing(CC)
def gcduni(g,f):
    f1=g; f2=f
    q, r = g.quo_rem(f)
    if (q==0):
        f1 = f; f2 = g
    h=f1; s=f2

    while s!= 0:
        q, r = h.quo_rem(s)
        h = s
        s = r

    return h

f1=x^6-1
f2=x^3+1
gcduni(f1, f2)

S.<x,y> = PolynomialRing(CC,2,'xy',order='degrevlex')
def multidiv(f, listf):
    listofqs = []
    p=f
    for fs in listf:
        q, r = p.quo_rem(fs)
        listofqs.append(q)
        p=r
    return (listofqs, p)

f1=x^6-y^2*x
f2=x^3+y*x^2

multidiv(x^10*y^10-x^8*y^8, [f1,f2])

def buchbergerAlgorithm(listf):
    listg = listf
    while True:
        gTemp = listg
        for p in gTemp:
            for q in gTemp:
                if not p == q:
                    gamma = lcm(p,q)
                    s = gamma/p.lt()*p - gamma/q.lt()*q
                    r = multidiv(s, gTemp)[1]
                    if r != 0:
                        g.append(r)
        if listg == gTemp:
            break

    return listg

def minimalizeGB(listg):
    gTemp = listg
    for gi in gTemp:
        gi = gi*(1/gi.lc())
    for gj in gTemp:
        for gi in gTemp:
            if gi != gj and (gi.lt()).quo_rem(gj.lt())[1]==0:
                gTemp.remove(gi)

    return gTemp


def reducedGB(listg):
    gTemp = minimalizeGB(listg)
    reducedBasis = []
    for gi in gTemp:
        temp = 0
        while True:
            s = gi.lt()
            if s == 0: break
            flag = 0
            for gj in gTemp:
                if gi != gj and s.quo_rem(gj.lt())[1] == 0: flag = 1
            if not flag: temp = temp + s
            gi = gi - s
        reducedBasis.append(temp)
        
    return reducedBasis