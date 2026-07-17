from sqlalchemy import BigInteger, Column, Float, Integer, String, create_engine
from sqlalchemy.orm import declarative_base, sessionmaker


# Step 1 - Establish a database connection
engine = create_engine("postgresql://postgres:12345@localhost:5432/jpademo")

# Step 2 - Initialize base object
Base = declarative_base()


# Step 3 - Define table mapping (schema unchanged)
class Product(Base):
    __tablename__ = "product"

    id = Column(BigInteger, primary_key=True)
    name = Column(String)
    price = Column(Float)
    category = Column(String)
    brand = Column(String)
    stocks = Column(Integer)

    def __repr__(self):
        return (
            f"Product(id={self.id}, name={self.name}, price={self.price}, "
            f"category={self.category}, brand={self.brand}, stocks={self.stocks})"
        )


# Step 4 - Create session
Session = sessionmaker(bind=engine)
session = Session()

try:
    # Step 5 - Read all products
    print("All products:")
    rows = session.query(Product).all()
    for row in rows:
        print(row)

    # Step 6 - Read one product by id
    print("\nProduct with id=1:")
    one_product = session.query(Product).filter_by(id=1).first()
    print(one_product)

    # Step 7 - Insert a product (temporary demo record)
    max_id = session.query(Product.id).order_by(Product.id.desc()).first()
    next_id = (max_id[0] if max_id else 0) + 1
    new_product = Product(
        id=next_id,
        name="Demo Product",
        price=99.99,
        category="OTHER",
        brand="DemoBrand",
        stocks=10,
    )
    session.add(new_product)
    session.commit()
    print(f"\nInserted product id={next_id}")

    # Step 8 - Update the inserted product
    inserted_product = session.query(Product).filter_by(id=next_id).first()
    inserted_product.brand = "UpdatedBrand"
    inserted_product.stocks = 20
    session.commit()
    print("Updated inserted product:")
    print(inserted_product)

    # Step 9 - Delete the inserted product
    session.delete(inserted_product)
    session.commit()
    print(f"Deleted product id={next_id}")

finally:
    session.close()