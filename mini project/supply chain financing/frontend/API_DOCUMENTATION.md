# Supply Chain Financing Backend - API Documentation

## Overview

This document provides complete API reference and JavaScript/HTML integration examples for connecting a frontend to the SCF backend.

**Base URL:** `http://localhost:8080`

**API Prefix:** `/api`

**Authentication:** JWT Bearer Token (Bearer scheme)

---

## Table of Contents

1. [Authentication APIs](#authentication-apis)
2. [Customer APIs](#customer-apis)
3. [Admin APIs](#admin-apis)
4. [Error Handling](#error-handling)
5. [JavaScript Integration Examples](#javascript-integration-examples)
6. [HTML Form Examples](#html-form-examples)

---

## Authentication APIs

### Register Customer

**Endpoint:** `POST /api/auth/register`

**Description:** Create a new customer account

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "phone": "9876543210"
}
```

**Response (201 Created):**
```json
{
  "message": "Registration successful"
}
```

**Validation Rules:**
- `name`: Not blank, max 255 characters
- `email`: Valid email format, unique
- `password`: Min 6 characters, max 100
- `phone`: Exactly 10 digits

---

### Login

**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate and receive JWT token

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwicm9sZSI6IkNVU1RPTUVSIiwiaWF0IjoxNjI1MTM5MDAwLCJleHAiOjE2MjUyMjUwMDB9.xyz"
}
```

**Notes:**
- Token is valid for 24 hours (86400000 ms)
- Use this token in `Authorization: Bearer <token>` header for protected endpoints
- Role is encoded in token: `CUSTOMER` or `ADMIN`

---

## Customer APIs

> **Authentication Required:** YES (Bearer Token)  
> **Role Required:** `CUSTOMER`

### Get Customer Profile

**Endpoint:** `GET /api/customer/profile`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "role": "CUSTOMER",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### Update Customer Profile

**Endpoint:** `PUT /api/customer/profile`

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Updated",
  "phone": "9876543211"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john@example.com",
  "phone": "9876543211",
  "role": "CUSTOMER",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### Apply for Loan

**Endpoint:** `POST /api/customer/loans`

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "amount": 500000,
  "interestRate": 12.5,
  "tenureMonths": 24,
  "purpose": "Inventory Purchase"
}
```

**Response (201 Created):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "customerId": 1,
  "customerName": "John Doe",
  "amount": 500000,
  "interestRate": 12.5,
  "tenureMonths": 24,
  "purpose": "Inventory Purchase",
  "status": "PENDING",
  "approvedById": null,
  "approvedDate": null,
  "disbursedDate": null,
  "remainingAmount": 560000,
  "rejectionReason": null,
  "createdAt": "2024-01-15T12:00:00"
}
```

**Validation:**
- `amount`: > 0
- `interestRate`: > 0
- `tenureMonths`: >= 1
- `purpose`: Not blank

---

### Get My Loans

**Endpoint:** `GET /api/customer/loans`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 10,
    "loanNumber": "LN-A1B2C3D4",
    "customerId": 1,
    "customerName": "John Doe",
    "amount": 500000,
    "interestRate": 12.5,
    "tenureMonths": 24,
    "purpose": "Inventory Purchase",
    "status": "PENDING",
    "remainingAmount": 560000,
    "createdAt": "2024-01-15T12:00:00"
  },
  {
    "id": 11,
    "loanNumber": "LN-B2C3D4E5",
    "customerId": 1,
    "customerName": "John Doe",
    "amount": 750000,
    "interestRate": 10.5,
    "tenureMonths": 36,
    "purpose": "Equipment",
    "status": "APPROVED",
    "remainingAmount": 850000,
    "createdAt": "2024-01-20T14:30:00"
  }
]
```

---

### Get Loan Details

**Endpoint:** `GET /api/customer/loans/{loanId}`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "customerId": 1,
  "customerName": "John Doe",
  "amount": 500000,
  "interestRate": 12.5,
  "tenureMonths": 24,
  "purpose": "Inventory Purchase",
  "status": "PENDING",
  "approvedById": null,
  "approvedDate": null,
  "disbursedDate": null,
  "remainingAmount": 560000,
  "rejectionReason": null,
  "createdAt": "2024-01-15T12:00:00"
}
```

---

### Repay Loan

**Endpoint:** `POST /api/customer/loans/{loanId}/repay`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "amount": 50000,
  "paymentMode": "NEFT",
  "remarks": "Monthly installment"
}
```

**Response (200 OK):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "customerId": 1,
  "customerName": "John Doe",
  "amount": 500000,
  "interestRate": 12.5,
  "tenureMonths": 24,
  "purpose": "Inventory Purchase",
  "status": "PARTIALLY_PAID",
  "remainingAmount": 510000,
  "createdAt": "2024-01-15T12:00:00"
}
```

**Payment Modes:**
- `UPI`
- `NEFT`
- `IMPS`
- `RTGS`
- `CASH`
- `CARD`

---

### Get Repayment History

**Endpoint:** `GET /api/customer/loans/{loanId}/repayments`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "loanId": 10,
    "amount": 50000,
    "paymentDate": "2024-01-20",
    "paymentMode": "NEFT",
    "remarks": "Monthly installment"
  },
  {
    "id": 2,
    "loanId": 10,
    "amount": 50000,
    "paymentDate": "2024-02-20",
    "paymentMode": "NEFT",
    "remarks": "Monthly installment"
  }
]
```

---

## Admin APIs

> **Authentication Required:** YES (Bearer Token)  
> **Role Required:** `ADMIN`

### Get Dashboard

**Endpoint:** `GET /api/admin/dashboard`

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
{
  "totalCustomers": 5,
  "totalLoans": 12,
  "pendingLoans": 3,
  "approvedLoans": 4,
  "rejectedLoans": 1,
  "disbursedLoans": 2,
  "closedLoans": 2,
  "totalAmountDisbursed": 2500000
}
```

---

### Get All Customers

**Endpoint:** `GET /api/admin/customers`

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "role": "CUSTOMER",
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "phone": "9876543211",
    "role": "CUSTOMER",
    "createdAt": "2024-01-16T11:45:00"
  }
]
```

---

### Get Customer Details

**Endpoint:** `GET /api/admin/customers/{customerId}`

**Path Parameters:**
- `customerId` (integer): Customer ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
{
  "customer": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "role": "CUSTOMER",
    "createdAt": "2024-01-15T10:30:00"
  },
  "loans": [
    {
      "id": 10,
      "loanNumber": "LN-A1B2C3D4",
      "amount": 500000,
      "status": "PENDING",
      "remainingAmount": 560000
    }
  ]
}
```

---

### Get All Loans

**Endpoint:** `GET /api/admin/loans`

**Query Parameters (Optional):**
- `status` (string): Filter by status (PENDING, APPROVED, REJECTED, DISBURSED, PARTIALLY_PAID, FULLY_PAID, CLOSED)
- `customerId` (integer): Filter by customer ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Examples:**
```
GET /api/admin/loans                           # All loans
GET /api/admin/loans?status=PENDING             # Pending loans
GET /api/admin/loans?customerId=1               # Loans for customer 1
GET /api/admin/loans?status=APPROVED&customerId=1  # Approved loans for customer 1
```

**Response (200 OK):**
```json
[
  {
    "id": 10,
    "loanNumber": "LN-A1B2C3D4",
    "customerId": 1,
    "customerName": "John Doe",
    "amount": 500000,
    "interestRate": 12.5,
    "status": "PENDING",
    "remainingAmount": 560000,
    "createdAt": "2024-01-15T12:00:00"
  }
]
```

---

### Get Loan Details

**Endpoint:** `GET /api/admin/loans/{loanId}`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
{
  "customer": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "role": "CUSTOMER"
  },
  "loan": {
    "id": 10,
    "loanNumber": "LN-A1B2C3D4",
    "customerId": 1,
    "customerName": "John Doe",
    "amount": 500000,
    "interestRate": 12.5,
    "status": "PENDING",
    "remainingAmount": 560000,
    "createdAt": "2024-01-15T12:00:00"
  },
  "repayments": [
    {
      "id": 1,
      "loanId": 10,
      "amount": 50000,
      "paymentDate": "2024-01-20",
      "paymentMode": "NEFT"
    }
  ]
}
```

---

### Approve Loan

**Endpoint:** `PUT /api/admin/loans/{loanId}/approve`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Request Body:**
Empty or no body required

**Response (200 OK):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "status": "APPROVED",
  "approvedById": 100,
  "approvedDate": "2024-01-18",
  "remainingAmount": 560000
}
```

---

### Reject Loan

**Endpoint:** `PUT /api/admin/loans/{loanId}/reject`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "Low credit score"
}
```

**Response (200 OK):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "status": "REJECTED",
  "rejectionReason": "Low credit score"
}
```

---

### Disburse Loan

**Endpoint:** `PUT /api/admin/loans/{loanId}/disburse`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Request Body:**
Empty or no body required

**Response (200 OK):**
```json
{
  "id": 10,
  "loanNumber": "LN-A1B2C3D4",
  "status": "DISBURSED",
  "disbursedDate": "2024-01-25"
}
```

---

### Get Loan Repayments

**Endpoint:** `GET /api/admin/loans/{loanId}/repayments`

**Path Parameters:**
- `loanId` (integer): Loan ID

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "loanId": 10,
    "amount": 50000,
    "paymentDate": "2024-01-20",
    "paymentMode": "NEFT",
    "remarks": "Monthly installment"
  }
]
```

---

## Error Handling

### Common Error Responses

**400 Bad Request - Validation Error:**
```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "email": "Email already registered",
    "phone": "Phone must be 10 digits"
  }
}
```

**401 Unauthorized:**
```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

**403 Forbidden (Wrong Role):**
```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Current user is not admin"
}
```

**404 Not Found:**
```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Loan not found"
}
```

**500 Internal Server Error:**
```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected error"
}
```

### HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Validation failed |
| 401 | Unauthorized - Invalid credentials or missing token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error |

---

## JavaScript Integration Examples

### 1. Register

```javascript
async function registerCustomer() {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      name: 'John Doe',
      email: 'john@example.com',
      password: 'SecurePass123',
      phone: '9876543210'
    })
  });

  if (response.ok) {
    const data = await response.json();
    console.log('Registration successful:', data.message);
  } else {
    const error = await response.json();
    console.error('Registration failed:', error.errors);
  }
}
```

---

### 2. Login & Store Token

```javascript
let authToken = null;

async function login() {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      email: 'john@example.com',
      password: 'SecurePass123'
    })
  });

  if (response.ok) {
    const data = await response.json();
    authToken = data.token;
    localStorage.setItem('authToken', authToken);
    console.log('Login successful! Token stored.');
  } else {
    const error = await response.json();
    console.error('Login failed:', error.message);
  }
}

// Retrieve token on page load
window.addEventListener('load', () => {
  authToken = localStorage.getItem('authToken');
});
```

---

### 3. Get Customer Profile

```javascript
async function getProfile() {
  const response = await fetch('http://localhost:8080/api/customer/profile', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${authToken}`
    }
  });

  if (response.ok) {
    const profile = await response.json();
    console.log('Profile:', profile);
    displayProfile(profile);
  } else if (response.status === 401) {
    console.error('Token expired or invalid. Please login again.');
  }
}

function displayProfile(profile) {
  document.getElementById('profileName').textContent = profile.name;
  document.getElementById('profileEmail').textContent = profile.email;
  document.getElementById('profilePhone').textContent = profile.phone;
}
```

---

### 4. Update Profile

```javascript
async function updateProfile(name, phone) {
  const response = await fetch('http://localhost:8080/api/customer/profile', {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${authToken}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      name: name,
      phone: phone
    })
  });

  if (response.ok) {
    const updated = await response.json();
    console.log('Profile updated successfully');
    displayProfile(updated);
  } else {
    const error = await response.json();
    console.error('Update failed:', error);
  }
}
```

---

### 5. Apply for Loan

```javascript
async function applyLoan() {
  const response = await fetch('http://localhost:8080/api/customer/loans', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${authToken}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      amount: 500000,
      interestRate: 12.5,
      tenureMonths: 24,
      purpose: 'Inventory Purchase'
    })
  });

  if (response.ok) {
    const loan = await response.json();
    console.log('Loan application successful:', loan.loanNumber);
    alert(`Loan created! Loan Number: ${loan.loanNumber}`);
  } else {
    const error = await response.json();
    console.error('Loan application failed:', error.message);
  }
}
```

---

### 6. Get My Loans

```javascript
async function getMyLoans() {
  const response = await fetch('http://localhost:8080/api/customer/loans', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${authToken}`
    }
  });

  if (response.ok) {
    const loans = await response.json();
    console.log('My Loans:', loans);
    displayLoans(loans);
  }
}

function displayLoans(loans) {
  const loansContainer = document.getElementById('loansContainer');
  loansContainer.innerHTML = '';

  loans.forEach(loan => {
    const html = `
      <div class="loan-card">
        <h3>${loan.loanNumber}</h3>
        <p><strong>Amount:</strong> ₹${loan.amount.toLocaleString()}</p>
        <p><strong>Status:</strong> <span class="status-${loan.status}">${loan.status}</span></p>
        <p><strong>Remaining:</strong> ₹${loan.remainingAmount.toLocaleString()}</p>
        <p><strong>Purpose:</strong> ${loan.purpose}</p>
      </div>
    `;
    loansContainer.innerHTML += html;
  });
}
```

---

### 7. Repay Loan

```javascript
async function repayLoan(loanId, amount) {
  const response = await fetch(`http://localhost:8080/api/customer/loans/${loanId}/repay`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${authToken}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      amount: parseFloat(amount),
      paymentMode: 'NEFT',
      remarks: 'Monthly installment'
    })
  });

  if (response.ok) {
    const updated = await response.json();
    console.log('Repayment successful! New status:', updated.status);
    alert(`Repayment successful! Remaining: ₹${updated.remainingAmount}`);
  } else {
    const error = await response.json();
    console.error('Repayment failed:', error.message);
  }
}
```

---

### 8. Get Loan Repayments

```javascript
async function getRepaymentHistory(loanId) {
  const response = await fetch(
    `http://localhost:8080/api/customer/loans/${loanId}/repayments`,
    {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${authToken}`
      }
    }
  );

  if (response.ok) {
    const repayments = await response.json();
    console.log('Repayment History:', repayments);
    displayRepayments(repayments);
  }
}

function displayRepayments(repayments) {
  const html = repayments.map(r => `
    <tr>
      <td>${r.paymentDate}</td>
      <td>₹${r.amount.toLocaleString()}</td>
      <td>${r.paymentMode}</td>
      <td>${r.remarks}</td>
    </tr>
  `).join('');
  
  document.getElementById('repaymentTable').innerHTML = html;
}
```

---

### 9. Admin - Get Dashboard

```javascript
async function getAdminDashboard() {
  const response = await fetch('http://localhost:8080/api/admin/dashboard', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${authToken}`
    }
  });

  if (response.ok) {
    const dashboard = await response.json();
    console.log('Dashboard:', dashboard);
    displayDashboard(dashboard);
  }
}

function displayDashboard(data) {
  document.getElementById('totalCustomers').textContent = data.totalCustomers;
  document.getElementById('totalLoans').textContent = data.totalLoans;
  document.getElementById('pendingLoans').textContent = data.pendingLoans;
  document.getElementById('approvedLoans').textContent = data.approvedLoans;
  document.getElementById('disbursedLoans').textContent = data.disbursedLoans;
  document.getElementById('totalDisbursed').textContent = 
    `₹${data.totalAmountDisbursed.toLocaleString()}`;
}
```

---

### 10. Admin - Get All Loans

```javascript
async function getAllLoans(status = null, customerId = null) {
  let url = 'http://localhost:8080/api/admin/loans';
  const params = new URLSearchParams();
  
  if (status) params.append('status', status);
  if (customerId) params.append('customerId', customerId);
  
  if (params.toString()) {
    url += '?' + params.toString();
  }

  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${authToken}`
    }
  });

  if (response.ok) {
    const loans = await response.json();
    console.log('All Loans:', loans);
    return loans;
  }
}

// Usage examples:
// getAllLoans();                           // All loans
// getAllLoans('PENDING');                  // Only pending
// getAllLoans(null, 1);                    // For customer 1
// getAllLoans('APPROVED', 1);              // Approved for customer 1
```

---

### 11. Admin - Approve Loan

```javascript
async function approveLoan(loanId) {
  const response = await fetch(
    `http://localhost:8080/api/admin/loans/${loanId}/approve`,
    {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    }
  );

  if (response.ok) {
    const loan = await response.json();
    console.log('Loan approved:', loan.loanNumber);
    alert(`Loan ${loan.loanNumber} approved successfully!`);
  } else {
    const error = await response.json();
    console.error('Approval failed:', error.message);
  }
}
```

---

### 12. Admin - Reject Loan

```javascript
async function rejectLoan(loanId, reason) {
  const response = await fetch(
    `http://localhost:8080/api/admin/loans/${loanId}/reject`,
    {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        reason: reason
      })
    }
  );

  if (response.ok) {
    const loan = await response.json();
    console.log('Loan rejected:', loan.loanNumber);
    alert(`Loan ${loan.loanNumber} rejected.`);
  }
}
```

---

### 13. Admin - Disburse Loan

```javascript
async function disburseLoan(loanId) {
  const response = await fetch(
    `http://localhost:8080/api/admin/loans/${loanId}/disburse`,
    {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    }
  );

  if (response.ok) {
    const loan = await response.json();
    console.log('Loan disbursed:', loan.loanNumber);
    alert(`Loan ${loan.loanNumber} disbursed on ${loan.disbursedDate}!`);
  }
}
```

---

## HTML Form Examples

### Registration Form

```html
<!DOCTYPE html>
<html>
<head>
  <title>Register - SCF</title>
  <style>
    .form-container { max-width: 400px; margin: 50px auto; }
    input { width: 100%; padding: 10px; margin: 10px 0; }
    button { padding: 10px 20px; background: #007bff; color: white; border: none; cursor: pointer; }
    .error { color: red; font-size: 12px; }
  </style>
</head>
<body>
  <div class="form-container">
    <h2>Register New Account</h2>
    <form onsubmit="handleRegister(event)">
      <input type="text" id="name" placeholder="Full Name" required>
      <input type="email" id="email" placeholder="Email" required>
      <input type="password" id="password" placeholder="Password (min 6 chars)" required>
      <input type="tel" id="phone" placeholder="Phone (10 digits)" pattern="[0-9]{10}" required>
      <button type="submit">Register</button>
    </form>
    <div id="message"></div>
  </div>

  <script>
    async function handleRegister(e) {
      e.preventDefault();

      const payload = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        phone: document.getElementById('phone').value
      };

      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const messageDiv = document.getElementById('message');
      if (response.ok) {
        messageDiv.innerHTML = '<p style="color: green;">Registration successful! Please login.</p>';
        e.target.reset();
      } else {
        const error = await response.json();
        messageDiv.innerHTML = `<p class="error">${JSON.stringify(error.errors)}</p>`;
      }
    }
  </script>
</body>
</html>
```

---

### Login Form

```html
<!DOCTYPE html>
<html>
<head>
  <title>Login - SCF</title>
  <style>
    .form-container { max-width: 400px; margin: 50px auto; }
    input { width: 100%; padding: 10px; margin: 10px 0; }
    button { padding: 10px 20px; background: #28a745; color: white; border: none; cursor: pointer; }
    .success { color: green; }
    .error { color: red; }
  </style>
</head>
<body>
  <div class="form-container">
    <h2>Login</h2>
    <form onsubmit="handleLogin(event)">
      <input type="email" id="email" placeholder="Email" required>
      <input type="password" id="password" placeholder="Password" required>
      <button type="submit">Login</button>
    </form>
    <div id="message"></div>
  </div>

  <script>
    async function handleLogin(e) {
      e.preventDefault();

      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: document.getElementById('email').value,
          password: document.getElementById('password').value
        })
      });

      const messageDiv = document.getElementById('message');
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('authToken', data.token);
        messageDiv.innerHTML = '<p class="success">Login successful! Redirecting...</p>';
        setTimeout(() => window.location.href = '/dashboard.html', 2000);
      } else {
        const error = await response.json();
        messageDiv.innerHTML = `<p class="error">${error.message}</p>`;
      }
    }
  </script>
</body>
</html>
```

---

### Loan Application Form

```html
<!DOCTYPE html>
<html>
<head>
  <title>Apply for Loan - SCF</title>
  <style>
    .form-container { max-width: 500px; margin: 50px auto; }
    input, select { width: 100%; padding: 10px; margin: 10px 0; }
    button { padding: 10px 20px; background: #007bff; color: white; border: none; cursor: pointer; }
  </style>
</head>
<body>
  <div class="form-container">
    <h2>Apply for Loan</h2>
    <form onsubmit="handleLoanApplication(event)">
      <label>Loan Amount (₹)</label>
      <input type="number" id="amount" min="1000" step="1000" required>

      <label>Interest Rate (%)</label>
      <input type="number" id="rate" min="0.1" step="0.1" required>

      <label>Tenure (Months)</label>
      <input type="number" id="tenure" min="1" max="120" required>

      <label>Purpose</label>
      <input type="text" id="purpose" placeholder="e.g., Inventory Purchase" required>

      <button type="submit">Apply for Loan</button>
    </form>
    <div id="message"></div>
  </div>

  <script>
    async function handleLoanApplication(e) {
      e.preventDefault();

      const token = localStorage.getItem('authToken');
      if (!token) {
        alert('Please login first');
        return;
      }

      const response = await fetch('http://localhost:8080/api/customer/loans', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          amount: parseFloat(document.getElementById('amount').value),
          interestRate: parseFloat(document.getElementById('rate').value),
          tenureMonths: parseInt(document.getElementById('tenure').value),
          purpose: document.getElementById('purpose').value
        })
      });

      const messageDiv = document.getElementById('message');
      if (response.ok) {
        const loan = await response.json();
        messageDiv.innerHTML = `<p style="color: green;">
          ✓ Loan application successful!<br>
          Loan Number: <strong>${loan.loanNumber}</strong><br>
          Status: <strong>${loan.status}</strong>
        </p>`;
        e.target.reset();
      } else {
        const error = await response.json();
        messageDiv.innerHTML = `<p style="color: red;">${error.message}</p>`;
      }
    }
  </script>
</body>
</html>
```

---

### Repayment Form

```html
<!DOCTYPE html>
<html>
<head>
  <title>Make Repayment - SCF</title>
  <style>
    .form-container { max-width: 400px; margin: 50px auto; }
    input, select { width: 100%; padding: 10px; margin: 10px 0; }
    button { padding: 10px 20px; background: #28a745; color: white; border: none; cursor: pointer; }
  </style>
</head>
<body>
  <div class="form-container">
    <h2>Make Repayment</h2>
    <form onsubmit="handleRepayment(event)">
      <label>Loan ID</label>
      <input type="number" id="loanId" required>

      <label>Repayment Amount (₹)</label>
      <input type="number" id="amount" min="100" step="100" required>

      <label>Payment Mode</label>
      <select id="paymentMode" required>
        <option value="">Select Payment Mode</option>
        <option value="UPI">UPI</option>
        <option value="NEFT">NEFT</option>
        <option value="IMPS">IMPS</option>
        <option value="RTGS">RTGS</option>
        <option value="CASH">CASH</option>
        <option value="CARD">CARD</option>
      </select>

      <label>Remarks (Optional)</label>
      <input type="text" id="remarks" placeholder="e.g., Monthly installment">

      <button type="submit">Submit Repayment</button>
    </form>
    <div id="message"></div>
  </div>

  <script>
    async function handleRepayment(e) {
      e.preventDefault();

      const token = localStorage.getItem('authToken');
      const loanId = document.getElementById('loanId').value;

      const response = await fetch(
        `http://localhost:8080/api/customer/loans/${loanId}/repay`,
        {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            amount: parseFloat(document.getElementById('amount').value),
            paymentMode: document.getElementById('paymentMode').value,
            remarks: document.getElementById('remarks').value
          })
        }
      );

      const messageDiv = document.getElementById('message');
      if (response.ok) {
        const loan = await response.json();
        messageDiv.innerHTML = `<p style="color: green;">
          ✓ Repayment successful!<br>
          New Status: <strong>${loan.status}</strong><br>
          Remaining: ₹${loan.remainingAmount.toLocaleString()}
        </p>`;
        e.target.reset();
      } else {
        const error = await response.json();
        messageDiv.innerHTML = `<p style="color: red;">${error.message}</p>`;
      }
    }
  </script>
</body>
</html>
```

---

## CORS Configuration

The backend is configured with CORS enabled. You can make requests from any origin during development.

For production, update `SecurityConfig.java`:

```java
.cors(cors -> cors.configurationSource(request -> {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("https://yourdomain.com"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    return config;
}))
```

---

## Testing with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "phone": "9876543210"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'

# Get Profile (replace TOKEN with actual token)
curl -X GET http://localhost:8080/api/customer/profile \
  -H "Authorization: Bearer TOKEN"

# Apply Loan
curl -X POST http://localhost:8080/api/customer/loans \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500000,
    "interestRate": 12.5,
    "tenureMonths": 24,
    "purpose": "Inventory Purchase"
  }'
```

---

## Quick Start Checklist

- [ ] Start PostgreSQL
- [ ] Start Spring Boot backend (`mvn spring-boot:run`)
- [ ] Load test data (`psql -U postgres -d jpademo -f data.sql`)
- [ ] Create HTML form files
- [ ] Include JavaScript code snippets
- [ ] Test login endpoint with test credentials
- [ ] Store JWT token in `localStorage`
- [ ] Use token in Authorization header for all requests
- [ ] Access Swagger UI at `http://localhost:8080/swagger-ui.html`

---

**Last Updated:** July 9, 2026  
**API Version:** v1  
**Status:** Production Ready

