function generateSet(x, y, z) {
    let result = [];
    let count = 0;
    let a = 0, b = 0, c = 0;
    while (count < 500) {
        let num = a * x + b * y + c * z;
        result.push(num);
        count++;
        if (c < 499) {
            c++;
        } else if (b < 499) {
            b++;
            c = 0;
        } else if (a < 499) {
            a++;
            b = 0;
            c = 0;
        }
    }
    return result.sort((a, b) => a - b);
}

console.log(generateSet(5,7,3))
