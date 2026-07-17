import psycopg2

#Establish a connection to the PostgreSQL database
conn = psycopg2.connect(
    host="localhost",
    database="jpademo",
    user="postgres",
    password="12345",
    port=5432
)

curr = conn.cursor()
curr.execute("SELECT * FROM product")
rows = curr.fetchall()
for row in rows:
    print(row)


curr.execute("SELECT * FROM product WHERE id = %s", (1,))
row = curr.fetchone()
print(row)

curr.close()
conn.close()