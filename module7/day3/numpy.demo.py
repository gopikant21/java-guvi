import numpy as np

a = np.array([1, 2, 3])

print(f" sum: {a.sum()}")
print(f" mean: {a.mean()}")
print(f" std: {a.std()}")
print(f" min: {a.min()}")
print(f" max: {a.max()}")

print("===============================================================")

multi = np.array([[1, 2, 3], [4, 5, 6]])
print(f" multi_sum: {multi.sum()}")
print(f" multi_mean: {multi.mean()}")
print(f" multi_std: {multi.std()}")
print(f" multi_min: {multi.min()}")
print(f" multi_max: {multi.max()}")
print(f" multi_sum_axis0: {multi.sum(axis=0)}")
print(f" multi_sum_axis1: {multi.sum(axis=1)}")
print(f"shape: {multi.shape}")
print(f"reshape: {multi.reshape(3, 2)}")
print(f"transpose: {multi.T}")
print(f"flatten: {multi.flatten()}")
print(f"dtype: {multi.dtype}")