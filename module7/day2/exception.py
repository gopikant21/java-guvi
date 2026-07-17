try:
    age = int(input("Enter your age: "))
    num1 = int(input("Enter a number: "))
    num2 = int(input("Enter another number: "))
    print(num1 / num2)
    print(age)
except ValueError:
    print("Invalid input. Please enter a valid age.")
except ZeroDivisionError:
    print("Cannot divide by zero.")
except Exception as e:
    print(f"An unexpected error occurred: {e}")
