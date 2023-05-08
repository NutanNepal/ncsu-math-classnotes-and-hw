def calculate_results(x, y, z):
    results = set()
    for a in range(0, 20):
        for b in range(0, 20):
            for c in range(0, 20):
                result = (a * x) + (b * y) + (c * z)
                results.add(result)
    return sorted(results)

print(calculate_results(125,0,4))