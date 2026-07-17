from sqlalchemy import Column, Integer, String, create_engine
from sqlalchemy.orm import declarative_base, sessionmaker


# Step 1 - Establish a database connection
engine = create_engine("postgresql://postgres:12345@localhost:5432/jpademo")

# Step 2 - Initialize base object
Base = declarative_base()


# Step 3 - Define Student mapping
class Student(Base):
	__tablename__ = "py_students"

	id = Column(Integer, primary_key=True, autoincrement=True)
	name = Column(String(100), nullable=False)
	age = Column(Integer, nullable=False)
	course = Column(String(100), nullable=False)
	city = Column(String(100))

	def __repr__(self):
		return (
			f"Student(id={self.id}, name={self.name}, age={self.age}, "
			f"course={self.course}, city={self.city})"
		)


# Step 4 - Create table if it does not exist
Base.metadata.create_all(engine)

# Step 5 - Create session
Session = sessionmaker(bind=engine)
session = Session()

try:
	# Step 6 - Read all students
	print("All students:")
	students = session.query(Student).all()
	for student in students:
		print(student)

	# Step 7 - Insert a student (temporary demo record)
	new_student = Student(
		name="Rahul",
		age=21,
		course="Computer Science",
		city="Hyderabad",
	)
	session.add(new_student)
	session.commit()
	print(f"\nInserted student id={new_student.id}")

	# Step 8 - Read one student by id
	one_student = session.query(Student).filter_by(id=new_student.id).first()
	print("\nStudent by id:")
	print(one_student)

	# Step 9 - Update inserted student
	one_student.city = "Bengaluru"
	session.commit()
	print("\nUpdated student:")
	print(one_student)

	# Step 10 - Delete inserted student (cleanup)
	session.delete(one_student)
	session.commit()
	print(f"\nDeleted student id={new_student.id}")

finally:
	session.close()
