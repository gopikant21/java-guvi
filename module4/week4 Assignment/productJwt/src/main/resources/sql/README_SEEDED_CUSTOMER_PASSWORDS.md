# Seeded Customer Password Reference

This file explains the decoded (plain text) passwords for customer logins created by:

- `src/main/resources/sql/seed_full_ecommerce_data.sql`

## Important schema note

The current `Customer` entity in this project does **not** contain a password field.
So the seed script creates a dedicated auth table:

- `customer_auth(customer_id, username, password_hash, role, ...)`

Passwords are stored in `customer_auth.password_hash` using BCrypt via PostgreSQL `pgcrypto`:

- `crypt(plain_password, gen_salt('bf', 10))`

## Username and decoded password pattern

For each customer id `N` (1..60):

- Username: `customerNNN`
- Decoded Password: `Cust@NNN`

Where `NNN` is zero-padded to 3 digits.

Examples:

- `customer001` -> `Cust@001`
- `customer002` -> `Cust@002`
- `customer010` -> `Cust@010`
- `customer060` -> `Cust@060`

## Full credential list (all seeded customers)

| Customer ID | Username    | Decoded Password | Role |
|-------------|-------------|------------------|------|
| 1 | customer001 | Cust@001 | ROLE_ADMIN |
| 2 | customer002 | Cust@002 | ROLE_ADMIN |
| 3 | customer003 | Cust@003 | ROLE_USER |
| 4 | customer004 | Cust@004 | ROLE_USER |
| 5 | customer005 | Cust@005 | ROLE_USER |
| 6 | customer006 | Cust@006 | ROLE_USER |
| 7 | customer007 | Cust@007 | ROLE_USER |
| 8 | customer008 | Cust@008 | ROLE_USER |
| 9 | customer009 | Cust@009 | ROLE_USER |
| 10 | customer010 | Cust@010 | ROLE_USER |
| 11 | customer011 | Cust@011 | ROLE_USER |
| 12 | customer012 | Cust@012 | ROLE_USER |
| 13 | customer013 | Cust@013 | ROLE_USER |
| 14 | customer014 | Cust@014 | ROLE_USER |
| 15 | customer015 | Cust@015 | ROLE_USER |
| 16 | customer016 | Cust@016 | ROLE_USER |
| 17 | customer017 | Cust@017 | ROLE_USER |
| 18 | customer018 | Cust@018 | ROLE_USER |
| 19 | customer019 | Cust@019 | ROLE_USER |
| 20 | customer020 | Cust@020 | ROLE_USER |
| 21 | customer021 | Cust@021 | ROLE_USER |
| 22 | customer022 | Cust@022 | ROLE_USER |
| 23 | customer023 | Cust@023 | ROLE_USER |
| 24 | customer024 | Cust@024 | ROLE_USER |
| 25 | customer025 | Cust@025 | ROLE_USER |
| 26 | customer026 | Cust@026 | ROLE_USER |
| 27 | customer027 | Cust@027 | ROLE_USER |
| 28 | customer028 | Cust@028 | ROLE_USER |
| 29 | customer029 | Cust@029 | ROLE_USER |
| 30 | customer030 | Cust@030 | ROLE_USER |
| 31 | customer031 | Cust@031 | ROLE_USER |
| 32 | customer032 | Cust@032 | ROLE_USER |
| 33 | customer033 | Cust@033 | ROLE_USER |
| 34 | customer034 | Cust@034 | ROLE_USER |
| 35 | customer035 | Cust@035 | ROLE_USER |
| 36 | customer036 | Cust@036 | ROLE_USER |
| 37 | customer037 | Cust@037 | ROLE_USER |
| 38 | customer038 | Cust@038 | ROLE_USER |
| 39 | customer039 | Cust@039 | ROLE_USER |
| 40 | customer040 | Cust@040 | ROLE_USER |
| 41 | customer041 | Cust@041 | ROLE_USER |
| 42 | customer042 | Cust@042 | ROLE_USER |
| 43 | customer043 | Cust@043 | ROLE_USER |
| 44 | customer044 | Cust@044 | ROLE_USER |
| 45 | customer045 | Cust@045 | ROLE_USER |
| 46 | customer046 | Cust@046 | ROLE_USER |
| 47 | customer047 | Cust@047 | ROLE_USER |
| 48 | customer048 | Cust@048 | ROLE_USER |
| 49 | customer049 | Cust@049 | ROLE_USER |
| 50 | customer050 | Cust@050 | ROLE_USER |
| 51 | customer051 | Cust@051 | ROLE_USER |
| 52 | customer052 | Cust@052 | ROLE_USER |
| 53 | customer053 | Cust@053 | ROLE_USER |
| 54 | customer054 | Cust@054 | ROLE_USER |
| 55 | customer055 | Cust@055 | ROLE_USER |
| 56 | customer056 | Cust@056 | ROLE_USER |
| 57 | customer057 | Cust@057 | ROLE_USER |
| 58 | customer058 | Cust@058 | ROLE_USER |
| 59 | customer059 | Cust@059 | ROLE_USER |
| 60 | customer060 | Cust@060 | ROLE_USER |

## Run the seed script (PostgreSQL)

```sql
\i 'C:/Users/gopi.kant/Java narc/module4/week4 Assignment/productJwt/src/main/resources/sql/seed_full_ecommerce_data.sql'
```

Or with `psql`:

```bash
psql -h localhost -U postgres -d jpademo -f "C:/Users/gopi.kant/Java narc/module4/week4 Assignment/productJwt/src/main/resources/sql/seed_full_ecommerce_data.sql"
```

