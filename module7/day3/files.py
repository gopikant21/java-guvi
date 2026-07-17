f = open("abc.json", "r")

print(f.read())
f.close()

with open("abc.csv", "r") as f:
    print(f.read())