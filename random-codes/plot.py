import matplotlib.pyplot as plt
import numpy as np

def draw_polynomial():
    x = range(-5, 6)
    y = [i**3 for i in x]
    plt.plot(x, y)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('y = x^4')
    plt.grid(True)
    plt.show()


draw_polynomial()