import pandas as pd
import matplotlib.pyplot as plt

# Load the dataset
sales = {
    'month': ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
    'sales': [200, 250, 300, 400, 500],
    'profit': [50, 60, 70, 80, 90]
}

# Display the first few rows of the dataset
print(pd.DataFrame(sales).head())

# Plot a simple graph
plt.figure(figsize=(10, 6))

# Assuming the dataset has columns 'x' and 'y'
plt.plot(sales['month'], sales['sales'], marker='o')
plt.title('Simple Line Plot')
plt.xlabel('Month')
plt.ylabel('Sales')
plt.grid()

# Show the plot
plt.show()

# Bar chart
plt.figure(figsize=(10, 6))
plt.bar(sales['month'], sales['sales'])
plt.title('Sales Bar Chart')
plt.xlabel('Month')
plt.ylabel('Sales')
plt.show()

# Pie chart
plt.figure(figsize=(8, 8))
plt.pie(sales['sales'], labels=sales['month'], autopct='%1.1f%%', startangle=140)
plt.title('Sales Distribution Pie Chart')
plt.show()