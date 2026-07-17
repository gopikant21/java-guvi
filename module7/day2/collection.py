name = ['Renjita', 'Rohit', 'Sakshi', 'Sandeep', 'Shivani', 'Siddharth', 'Sneha', 'Sonal', 'Sourabh', 'Swati']
print(name[1:5])  # retrieves the elements from index 1 to index 4.
print(name[3:7:2])  # retrieves every second element from the list.
print(name[::-1])  # retrieves the elements in reverse order.


#List comprehension:

numbers = []

for item in range(1, 11):
    numbers.append(item ** 2)

# Using list comprehension
squares = [item ** 2 for item in range(1, 11)]
print(squares)  # Output: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]


#Tuples: not editable, Temporary data, ()

fruits = ('apple', 'banana', 'cherry', 'date', 'elderberry')
fruits[1] = "blueberry"  # This will raise a TypeError since tuples are immutable.
print(fruits)  # Output: ('apple', 'banana', 'cherry', 'date', 'elderberry')

# lists: editable, Permanent data, []
# tuples: not editable, Temporary data, ()
# sets: editable, Permanent data, {}, unique values, unordered
# dictionaries: editable, Permanent data, {}, key-value pairs, ordered (as of Python 3.7+)
