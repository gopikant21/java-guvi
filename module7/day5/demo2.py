import pandas as pd
import matplotlib.pyplot as plt

marks = [85, 90, 78, 92, 88, 95, 80, 87, 91, 89, 84, 93, 86, 94, 82, 88, 90, 85, 87, 91 ]

# histogram
plt.figure(figsize=(10, 6))
plt.hist(marks, bins=10, edgecolor='black')
plt.title('Histogram of Marks')
plt.xlabel('Marks')
plt.ylabel('Frequency')
plt.grid(axis='y', alpha=0.75)
plt.show()