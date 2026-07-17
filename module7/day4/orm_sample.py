from sqlalchemy import BigInteger, Column, Float, Integer, String, create_engine
from sqlalchemy.orm import declarative_base, sessionmaker

engine = create_engine("postgresql+psycopg2://postgres:12345@localhost:5432/jpademo")
Base = declarative_base()

class Product(Base):
    __tablename__ = "product"
    id = Column(BigInteger, primary_key=True)
    name = Column(String)
    price = Column(Float)
    category = Column(String)
    brand = Column(String)
    stocks = Column(Integer)

Session = sessionmaker(bind=engine)

# Fetch all products
with Session() as session:
    products = session.query(Product).all()
    for p in products:
        print(p.id, p.name, p.price, p.category, p.brand, p.stocks)

# Fetch one product by id
with Session() as session:
    p = session.query(Product).filter(Product.id == 1).first()
    if p:
        print("Found:", p.id, p.name, p.price, p.category, p.brand, p.stocks)
    else:
        print("Product not found")


session.close()