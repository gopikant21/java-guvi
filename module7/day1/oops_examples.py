# ============================================================================
# COMPREHENSIVE OBJECT-ORIENTED PROGRAMMING (OOPs) EXAMPLES IN PYTHON
# ============================================================================

# 1. BASIC CLASS AND OBJECT
# ============================================================================
print("=" * 70)
print("1. BASIC CLASS AND OBJECT")
print("=" * 70)

class Car:
    """A simple car class"""
    # Class attribute (shared by all instances)
    wheels = 4
    
    # Constructor - initializes instance attributes
    def __init__(self, brand, color, year):
        self.brand = brand
        self.color = color
        self.year = year
    
    # Instance method
    def display_info(self):
        return f"{self.year} {self.color} {self.brand} with {self.wheels} wheels"
    
    def honk(self):
        return "Beep! Beep!"

# Creating objects
car1 = Car("Toyota", "Red", 2022)
car2 = Car("Honda", "Blue", 2021)

print(car1.display_info())
print(car2.display_info())
print(car1.honk())


# 2. ENCAPSULATION (Data Hiding)
# ============================================================================
print("\n" + "=" * 70)
print("2. ENCAPSULATION (Data Hiding)")
print("=" * 70)

class BankAccount:
    """Demonstrates encapsulation with private attributes"""
    
    def __init__(self, account_holder, balance):
        self.account_holder = account_holder
        self.__balance = balance  # Private attribute (name mangling)
        self.__pin = 1234  # Private PIN
    
    def deposit(self, amount):
        if amount > 0:
            self.__balance += amount
            return f"Deposited: ${amount}. New balance: ${self.__balance}"
        return "Invalid amount"
    
    def withdraw(self, amount, pin):
        if pin == self.__pin:
            if amount <= self.__balance:
                self.__balance -= amount
                return f"Withdrawn: ${amount}. New balance: ${self.__balance}"
            return "Insufficient funds"
        return "Invalid PIN"
    
    def get_balance(self, pin):
        if pin == self.__pin:
            return f"Balance: ${self.__balance}"
        return "Invalid PIN"

account = BankAccount("John", 1000)
print(account.deposit(500))
print(account.withdraw(200, 1234))
print(account.get_balance(1234))
print(account.get_balance(0000))  # Wrong PIN


# 3. INHERITANCE
# ============================================================================
print("\n" + "=" * 70)
print("3. INHERITANCE (Parent-Child Classes)")
print("=" * 70)

# Parent class
class Animal:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    
    def eat(self):
        return f"{self.name} is eating"
    
    def sleep(self):
        return f"{self.name} is sleeping"

# Child class 1
class Dog(Animal):
    def __init__(self, name, age, breed):
        super().__init__(name, age)  # Call parent constructor
        self.breed = breed
    
    def bark(self):
        return f"{self.name} says: Woof! Woof!"
    
    def eat(self):  # Override parent method
        return f"{self.name} is eating dog food"

# Child class 2
class Cat(Animal):
    def meow(self):
        return f"{self.name} says: Meow! Meow!"
    
    def eat(self):  # Override parent method
        return f"{self.name} is eating cat food"

dog = Dog("Buddy", 5, "Golden Retriever")
cat = Cat("Whiskers", 3)

print(dog.eat())
print(dog.bark())
print(dog.sleep())
print(cat.eat())
print(cat.meow())
print(cat.sleep())


# 4. POLYMORPHISM
# ============================================================================
print("\n" + "=" * 70)
print("4. POLYMORPHISM (Many Forms)")
print("=" * 70)

# Example 1: Method Overriding
class Shape:
    def area(self):
        pass
    
    def perimeter(self):
        pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius
    
    def area(self):
        return 3.14 * self.radius ** 2
    
    def perimeter(self):
        return 2 * 3.14 * self.radius

class Rectangle(Shape):
    def __init__(self, length, width):
        self.length = length
        self.width = width
    
    def area(self):
        return self.length * self.width
    
    def perimeter(self):
        return 2 * (self.length + self.width)

class Square(Shape):
    def __init__(self, side):
        self.side = side
    
    def area(self):
        return self.side ** 2
    
    def perimeter(self):
        return 4 * self.side

# Polymorphic function
def print_shape_info(shape):
    print(f"Area: {shape.area()}, Perimeter: {shape.perimeter()}")

circle = Circle(5)
rectangle = Rectangle(4, 6)
square = Square(5)

print_shape_info(circle)
print_shape_info(rectangle)
print_shape_info(square)

# Example 2: Operator Overloading
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)
    
    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)
    
    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)
    
    def __str__(self):
        return f"({self.x}, {self.y})"

v1 = Vector(2, 3)
v2 = Vector(1, 4)

print(f"v1 + v2 = {v1 + v2}")
print(f"v1 - v2 = {v1 - v2}")
print(f"v1 * 3 = {v1 * 3}")


# 5. ABSTRACTION
# ============================================================================
print("\n" + "=" * 70)
print("5. ABSTRACTION (Abstract Base Class)")
print("=" * 70)

from abc import ABC, abstractmethod

class Vehicle(ABC):
    """Abstract base class for vehicles"""
    
    @abstractmethod
    def start(self):
        pass
    
    @abstractmethod
    def stop(self):
        pass
    
    @abstractmethod
    def accelerate(self):
        pass

class Motorcycle(Vehicle):
    def start(self):
        return "Motorcycle engine started"
    
    def stop(self):
        return "Motorcycle engine stopped"
    
    def accelerate(self):
        return "Motorcycle accelerating"

class Truck(Vehicle):
    def start(self):
        return "Truck engine started"
    
    def stop(self):
        return "Truck engine stopped"
    
    def accelerate(self):
        return "Truck accelerating slowly"

motorcycle = Motorcycle()
truck = Truck()

print(motorcycle.start())
print(motorcycle.accelerate())
print(motorcycle.stop())
print(truck.start())
print(truck.accelerate())
print(truck.stop())


# 6. STATIC METHODS AND CLASS METHODS
# ============================================================================
print("\n" + "=" * 70)
print("6. STATIC METHODS AND CLASS METHODS")
print("=" * 70)

class MathOperations:
    pi = 3.14159
    
    # Static method - doesn't need self or cls
    @staticmethod
    def add(a, b):
        return a + b
    
    @staticmethod
    def multiply(a, b):
        return a * b
    
    # Class method - works with class, not instance
    @classmethod
    def from_string(cls, numbers):
        a, b = map(int, numbers.split(','))
        return cls(a, b)
    
    # Class method to access class variable
    @classmethod
    def get_pi(cls):
        return cls.pi

print(f"5 + 3 = {MathOperations.add(5, 3)}")
print(f"5 * 3 = {MathOperations.multiply(5, 3)}")
print(f"PI value: {MathOperations.get_pi()}")


# 7. MULTIPLE INHERITANCE
# ============================================================================
print("\n" + "=" * 70)
print("7. MULTIPLE INHERITANCE")
print("=" * 70)

class Flyable:
    def fly(self):
        return "Flying in the sky"

class Swimmable:
    def swim(self):
        return "Swimming in water"

class Duck(Flyable, Swimmable):
    def quack(self):
        return "Quack! Quack!"

duck = Duck()
print(duck.fly())
print(duck.swim())
print(duck.quack())
print(f"MRO (Method Resolution Order): {Duck.mro()}")


# 8. COMPOSITION
# ============================================================================
print("\n" + "=" * 70)
print("8. COMPOSITION (Has-A Relationship)")
print("=" * 70)

class Engine:
    def __init__(self, horsepower):
        self.horsepower = horsepower
    
    def start(self):
        return f"Engine with {self.horsepower} HP started"

class Wheel:
    def __init__(self, size):
        self.size = size
    
    def rotate(self):
        return f"Wheel of size {self.size} is rotating"

class Vehicle2:
    def __init__(self, brand, engine, wheels):
        self.brand = brand
        self.engine = engine
        self.wheels = wheels
    
    def drive(self):
        result = self.engine.start() + "\n"
        for wheel in self.wheels:
            result += wheel.rotate() + "\n"
        return result

engine = Engine(150)
wheels = [Wheel(17), Wheel(17), Wheel(17), Wheel(17)]
vehicle = Vehicle2("Toyota", engine, wheels)
print(vehicle.drive())


# 9. DECORATORS (Property)
# ============================================================================
print("\n" + "=" * 70)
print("9. DECORATORS (Property)")
print("=" * 70)

class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age  # Convention: _ means private
    
    @property
    def age(self):
        """Getter for age"""
        return self._age
    
    @age.setter
    def age(self, value):
        """Setter for age"""
        if value < 0:
            raise ValueError("Age cannot be negative")
        self._age = value
    
    @property
    def is_adult(self):
        return self._age >= 18

person = Person("Alice", 25)
print(f"Name: {person.name}, Age: {person.age}")
print(f"Is Adult: {person.is_adult}")
person.age = 30
print(f"Updated Age: {person.age}")


# 10. SPECIAL METHODS (Magic Methods/Dunder Methods)
# ============================================================================
print("\n" + "=" * 70)
print("10. SPECIAL METHODS (Magic/Dunder Methods)")
print("=" * 70)

class Book:
    def __init__(self, title, author, pages):
        self.title = title
        self.author = author
        self.pages = pages
    
    def __str__(self):
        return f"'{self.title}' by {self.author}"
    
    def __repr__(self):
        return f"Book('{self.title}', '{self.author}', {self.pages})"
    
    def __len__(self):
        return self.pages
    
    def __lt__(self, other):
        return self.pages < other.pages
    
    def __eq__(self, other):
        return self.title == other.title and self.author == other.author
    
    def __hash__(self):
        return hash((self.title, self.author))

book1 = Book("Python 101", "John Doe", 300)
book2 = Book("Python 101", "John Doe", 300)
book3 = Book("Java Basics", "Jane Smith", 250)

print(f"str(): {str(book1)}")
print(f"repr(): {repr(book1)}")
print(f"len(): {len(book1)} pages")
print(f"book3 < book1: {book3 < book1}")
print(f"book1 == book2: {book1 == book2}")


# 11. INSTANCE VS CLASS ATTRIBUTES
# ============================================================================
print("\n" + "=" * 70)
print("11. INSTANCE VS CLASS ATTRIBUTES")
print("=" * 70)

class Student:
    school = "XYZ University"  # Class attribute (shared)
    student_count = 0  # Class variable to count students
    
    def __init__(self, name, grade):
        self.name = name  # Instance attribute
        self.grade = grade  # Instance attribute
        Student.student_count += 1
    
    @classmethod
    def get_student_count(cls):
        return f"Total students: {cls.student_count}"

s1 = Student("Alice", "A")
s2 = Student("Bob", "B")
s3 = Student("Charlie", "A")

print(f"School: {Student.school}")
print(Student.get_student_count())


# 12. SINGLE AND MULTIPLE INHERITANCE WITH SUPER()
# ============================================================================
print("\n" + "=" * 70)
print("12. SINGLE AND MULTIPLE INHERITANCE WITH SUPER()")
print("=" * 70)

class Parent1:
    def __init__(self, name):
        self.name = name
    
    def greet(self):
        return f"Hello from Parent1: {self.name}"

class Parent2:
    def __init__(self, age):
        self.age = age
    
    def greet(self):
        return f"Hello from Parent2: age {self.age}"

class Child(Parent1, Parent2):
    def __init__(self, name, age, city):
        Parent1.__init__(self, name)
        Parent2.__init__(self, age)
        self.city = city
    
    def greet(self):
        return f"Hello, I'm {self.name}, {self.age} years old, from {self.city}"

child = Child("David", 10, "New York")
print(child.greet())


# 13. EXCEPTION HANDLING WITH OOPs
# ============================================================================
print("\n" + "=" * 70)
print("13. EXCEPTION HANDLING WITH OOPs")
print("=" * 70)

class InsufficientFundsError(Exception):
    """Custom exception for insufficient funds"""
    pass

class Account:
    def __init__(self, balance):
        self.balance = balance
    
    def withdraw(self, amount):
        if amount > self.balance:
            raise InsufficientFundsError(f"Need ${amount}, but only have ${self.balance}")
        self.balance -= amount
        return f"Withdrew ${amount}. Balance: ${self.balance}"

account = Account(100)
try:
    print(account.withdraw(50))
    print(account.withdraw(100))  # This will raise exception
except InsufficientFundsError as e:
    print(f"Error: {e}")


# 14. GETTERS, SETTERS, AND DELETERS
# ============================================================================
print("\n" + "=" * 70)
print("14. GETTERS, SETTERS, AND DELETERS")
print("=" * 70)

class Temperature:
    def __init__(self, celsius):
        self._celsius = celsius
    
    @property
    def celsius(self):
        """Get temperature in Celsius"""
        return self._celsius
    
    @celsius.setter
    def celsius(self, value):
        """Set temperature in Celsius"""
        if value < -273.15:
            raise ValueError("Temperature cannot be below absolute zero")
        self._celsius = value
    
    @celsius.deleter
    def celsius(self):
        """Delete temperature"""
        del self._celsius
    
    @property
    def fahrenheit(self):
        """Get temperature in Fahrenheit"""
        return (self._celsius * 9/5) + 32

temp = Temperature(25)
print(f"Celsius: {temp.celsius}°C")
print(f"Fahrenheit: {temp.fahrenheit}°F")
temp.celsius = 30
print(f"Updated Celsius: {temp.celsius}°C")


# 15. DATACLASS (Python 3.7+)
# ============================================================================
print("\n" + "=" * 70)
print("15. DATACLASS (Python 3.7+)")
print("=" * 70)

from dataclasses import dataclass, field
from typing import List

@dataclass
class Person2:
    name: str
    age: int
    email: str = "unknown@email.com"
    hobbies: List[str] = field(default_factory=list)
    
    def introduce(self):
        return f"Hi, I'm {self.name}, {self.age} years old"

person2 = Person2("Emma", 28, hobbies=["reading", "coding"])
print(person2)
print(person2.introduce())

print("\n" + "=" * 70)
print("END OF OOPs EXAMPLES")
print("=" * 70)
