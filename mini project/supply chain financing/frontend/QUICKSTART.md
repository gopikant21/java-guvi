# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Start Backend API
Ensure your backend is running on `http://localhost:8080`

```bash
# Check backend is running
curl http://localhost:8080/api/auth/login
```

### Step 2: Start Frontend Server

**Option A: Using Python 3**
```bash
cd frontend
python -m http.server 8000
```

**Option B: Using Node.js**
```bash
cd frontend
npx http-server -p 8000
```

**Option C: Using PHP**
```bash
cd frontend
php -S localhost:8000
```

### Step 3: Open Browser
Navigate to: `http://localhost:8000`

### Step 4: Test Login
Use demo credentials:
- **Email**: john@example.com
- **Password**: SecurePass123

## 📱 Page Guide

### For Customers
1. **Home Page** (`/`)
   - Overview of SCF platform
   - Links to register or login

2. **Registration** (`/pages/register.html`)
   - Create new customer account
   - Validate email and phone number

3. **Login** (`/pages/login.html`)
   - Sign in with email and password
   - Demo credentials available

4. **Dashboard** (`/pages/dashboard.html`)
   - View loan statistics
   - Quick actions to apply for loan
   - See recent loans

5. **My Loans** (`/pages/loans.html`)
   - List all your loans
   - Filter by status
   - View repayment progress

6. **Loan Details** (`/pages/loan-details.html?id=1`)
   - Detailed loan information
   - Repayment history
   - Make new repayment

7. **Profile** (`/pages/profile.html`)
   - Update personal information
   - View account details

### For Admins
1. **Admin Dashboard** (`/pages/admin-dashboard.html`)
   - System overview with KPIs
   - Recent loans list
   - Quick access to management pages

2. **Loan Management** (`/pages/admin-loans.html`)
   - View all loans
   - Filter by status
   - Approve/Reject/Disburse loans

3. **Customer Management** (`/pages/admin-customers.html`)
   - View all customers
   - Search by name/email/phone
   - View customer loan history

## 🔑 Key Features

### Loan Management
- **Apply for Loan**: Specify amount, rate, tenure, and purpose
- **Track Status**: See loan status (Pending, Approved, Disbursed, etc.)
- **Monitor Progress**: Visual progress bar showing repayment status
- **Make Repayment**: Process repayments with multiple payment modes

### Payment Modes Supported
- UPI
- NEFT
- IMPS
- RTGS
- CASH
- CARD

## 💡 Tips & Tricks

### For Testing
1. Register a new account with test data
2. Apply for a loan
3. As admin, approve and disburse the loan
4. As customer, make repayments

### Browser DevTools
- Press `F12` or `Ctrl+Shift+I` to open Developer Tools
- Check Console tab for any JavaScript errors
- Network tab shows API calls

### Session Management
- Login token is stored in browser localStorage
- Token persists until logout or manual deletion
- You must logout to switch user accounts

## ⚠️ Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Page shows "Loading..." forever | Check backend is running, verify API_CONFIG in assets/js/api.js |
| "Unauthorized" error | Token expired, login again |
| Form validation errors | Check input format (10-digit phone, valid email, etc.) |
| CORS error in console | Backend CORS not enabled, check backend configuration |
| Page doesn't load | Check browser console for errors, verify file paths |

## 📧 API Base URL

Default: `http://localhost:8080/api`

To change, edit `assets/js/api.js`:
```javascript
const API_CONFIG = {
    BASE_URL: 'http://your-api-url:port/api',
    // ...
};
```

## 🎯 Features by Role

### Customer
✅ Register & Login  
✅ View Dashboard  
✅ Apply for Loan  
✅ View Loan Details  
✅ Track Repayment Progress  
✅ Make Repayments  
✅ View Repayment History  
✅ Update Profile  

### Admin
✅ View Dashboard with KPIs  
✅ Manage Loan Approvals  
✅ Reject Loans with Reason  
✅ Disburse Loans  
✅ View Customer Details  
✅ Search Customers  
✅ View Loan Repayments  
✅ Filter Loans by Status  

## 🔒 Security

- All passwords must be min 6 characters
- Phone number must be exactly 10 digits
- Email format validation
- JWT token-based authentication
- Tokens auto-clear on 401 errors

## 📞 Support

For issues:
1. Check browser console for errors (F12)
2. Verify backend API is running
3. Check API response in Network tab
4. Review API_DOCUMENTATION.md for endpoint details

## ✨ Next Steps

1. Explore the dashboard
2. Try applying for a loan (as customer)
3. Approve loan (as admin)
4. Make repayment (as customer)
5. Check repayment history

---

**Ready to go!** 🎉

Happy using SCF!

