# 🎯 Getting Started Checklist

## ✅ Pre-Setup Requirements

- [ ] Backend API running on `http://localhost:8080`
- [ ] Modern web browser installed (Chrome, Firefox, Safari, Edge)
- [ ] Python 3 or Node.js installed (for running local server)
- [ ] Git or file manager to navigate directories
- [ ] Text editor if you need to modify code

## 🚀 Setup Steps

### Step 1: Verify Backend is Running
```bash
# Test backend connectivity
curl http://localhost:8080/api/auth/login

# Expected: Error response (since no credentials sent)
# This confirms backend is running
```

**If Failed**: Start your backend server first

### Step 2: Start Frontend Server

**Choose one of these options:**

#### Option A: Python 3
```bash
# Navigate to frontend directory
cd "C:\Users\gopi.kant\Java narc\mini project\supply chain financing\frontend"

# Start server
python -m http.server 8000

# Output: Serving HTTP on 0.0.0.0 port 8000
```

#### Option B: Node.js + http-server
```bash
# Install http-server (one-time)
npm install -g http-server

# Navigate to frontend
cd "C:\Users\gopi.kant\Java narc\mini project\supply chain financing\frontend"

# Start server
http-server -p 8000
```

#### Option C: PHP
```bash
# Navigate to frontend
cd "C:\Users\gopi.kant\Java narc\mini project\supply chain financing\frontend"

# Start server
php -S localhost:8000
```

### Step 3: Open Application
```
http://localhost:8000
```

**Expected**: Beautiful landing page with SCF branding

### Step 4: Test Login
```
Email: john@example.com
Password: SecurePass123
```

---

## 📱 Page Walkthrough

### For New Users

#### 1. Home Page (http://localhost:8000)
- ✅ Overview of platform
- ✅ Features section
- ✅ "Get Started" button
- ✅ "Login" button in navbar

#### 2. Registration Page (pages/register.html)
- ✅ Fill in: Name, Email, Phone, Password
- ✅ Must enter 10-digit phone number
- ✅ Password must be min 6 characters
- ✅ Password confirmation must match
- ✅ Click "Create Account"
- ✅ Redirect to login page on success

#### 3. Login Page (pages/login.html)
- ✅ Use demo credentials or registered email
- ✅ Enter email and password
- ✅ Click "Sign In"
- ✅ Redirect to appropriate dashboard

#### 4. Customer Dashboard (pages/dashboard.html)
- ✅ See welcome message
- ✅ View loan statistics
- ✅ See recent loans
- ✅ "Apply New Loan" button
- ✅ Navigate to "My Loans", "Profile"

#### 5. Apply for Loan
- ✅ Click "Apply New Loan" button
- ✅ Fill modal form:
  - Loan Amount: Min 10000 (in ₹)
  - Interest Rate: e.g., 12.5
  - Tenure: 24 months
  - Purpose: "Equipment Purchase"
- ✅ Click "Submit Application"
- ✅ See success message
- ✅ New loan appears in "My Loans"

#### 6. My Loans Page (pages/loans.html)
- ✅ View all your loans
- ✅ See loan cards with:
  - Loan number
  - Amount
  - Status badge
  - Interest rate
  - Remaining amount
  - Repayment progress
- ✅ Filter by status dropdown
- ✅ "View Details" button for each loan

#### 7. Loan Details (pages/loan-details.html?id=1)
- ✅ Complete loan information
- ✅ Repayment progress bar
- ✅ Statistics cards for:
  - Interest rate
  - Tenure
  - Amount paid
  - Remaining amount
- ✅ If loan is disbursed:
  - Repayment form appears
  - Enter amount, select payment mode
  - Add optional remarks
- ✅ Repayment history table

#### 8. Make Repayment
- ✅ Enter repayment amount
- ✅ Select payment mode:
  - UPI, NEFT, IMPS, RTGS, CASH, CARD
- ✅ Add remarks (optional)
- ✅ Click "Process Repayment"
- ✅ See success message
- ✅ Data updates immediately

#### 9. Profile Page (pages/profile.html)
- ✅ View personal information
- ✅ Update name and phone
- ✅ View account status
- ✅ See member since date
- ✅ Logout option

---

## 👨‍💼 Admin Workflow

### Admin Login
```
Email: admin@example.com
Password: AdminPass123
```

(Or use customer account with admin role)

### Admin Dashboard (pages/admin-dashboard.html)
- ✅ See KPI cards:
  - Total customers
  - Total loans
  - Pending loans
  - Approved loans
  - Disbursed loans
  - Rejected loans
  - Closed loans
  - Total amount disbursed
- ✅ Recent loans table
- ✅ Quick action buttons

### Loan Management (pages/admin-loans.html)
- ✅ View all loans in table
- ✅ Filter by status
- ✅ Search by customer ID
- ✅ Click "View" to see loan details
- ✅ Modal shows:
  - Customer details
  - Loan information
  - Repayment history
  - Action buttons

#### Loan Actions
1. **Approve Pending Loan**
   - ✅ Click "View" on pending loan
   - ✅ Click "Approve" button
   - ✅ Confirm action
   - ✅ Loan status changes to APPROVED

2. **Reject Pending Loan**
   - ✅ Click "View" on pending loan
   - ✅ Click "Reject" button
   - ✅ Enter rejection reason
   - ✅ Click "Reject Loan"
   - ✅ Loan status changes to REJECTED

3. **Disburse Approved Loan**
   - ✅ Click "View" on approved loan
   - ✅ Click "Disburse" button
   - ✅ Confirm action
   - ✅ Loan status changes to DISBURSED
   - ✅ Disbursed date is set

### Customer Management (pages/admin-customers.html)
- ✅ See list of all customers
- ✅ Search by:
  - Name
  - Email
  - Phone number
- ✅ Click "View" on customer
- ✅ Modal shows:
  - Customer details
  - Loan summary
  - All loans of customer
  - Loan amounts and statuses

---

## 🔍 Troubleshooting Guide

### Issue: Page Shows "Loading..." Forever
**Solution**:
1. Check backend is running: `curl http://localhost:8080/api/auth/login`
2. Verify API_CONFIG in `assets/js/api.js`
3. Check browser console (F12) for errors
4. Check Network tab for failed API calls

### Issue: "Unauthorized" Error
**Solution**:
1. Session token expired
2. Login again
3. Clear localStorage: Open DevTools → Application → LocalStorage → Clear

### Issue: CORS Error in Console
**Solution**:
1. Ensure backend is running
2. Check backend CORS configuration
3. Verify API_CONFIG.BASE_URL points to correct backend

### Issue: Form Won't Submit
**Solution**:
1. Check all required fields are filled
2. Verify input validation (phone: 10 digits, email: valid format)
3. Check browser console for JavaScript errors
4. Check network tab for API response

### Issue: Data Not Updating After Action
**Solution**:
1. Page auto-refreshes but may be slow
2. Manually refresh: F5 or Cmd+R
3. Check API response in Network tab
4. Check backend logs for errors

---

## 📊 Test Scenarios

### Scenario 1: Complete Loan Journey (Customer)
- [ ] Register new customer account
- [ ] Login to dashboard
- [ ] Apply for loan (₹100,000, 12%, 24 months)
- [ ] Verify loan appears as PENDING
- [ ] Switch to admin account
- [ ] Find loan and approve it
- [ ] Disburse the loan
- [ ] Switch back to customer
- [ ] See loan status changed to DISBURSED
- [ ] Make repayment (₹10,000, UPI)
- [ ] Verify remaining amount decreased
- [ ] Check repayment appears in history
- [ ] Logout

### Scenario 2: Loan Rejection (Admin)
- [ ] Create new loan as customer
- [ ] Switch to admin
- [ ] Find pending loan
- [ ] Click View
- [ ] Click Reject
- [ ] Enter reason: "Credit score too low"
- [ ] Verify loan status is REJECTED
- [ ] Verify rejection reason is visible
- [ ] Switch to customer
- [ ] Verify loan shows as REJECTED with reason

### Scenario 3: Profile Update (Customer)
- [ ] Login as customer
- [ ] Go to Profile page
- [ ] Change name to "John Updated"
- [ ] Change phone to "9876543211"
- [ ] Click "Save Changes"
- [ ] Verify success message
- [ ] Refresh page
- [ ] Verify changes persisted

---

## 🔐 Security Checks

- [ ] Cannot access customer pages without login
- [ ] Cannot access admin pages without admin role
- [ ] Tokens expire properly
- [ ] Cannot modify other users' data
- [ ] Form inputs are validated
- [ ] API returns appropriate error codes
- [ ] Sensitive data not logged to console

---

## 🎨 UI Validation

- [ ] All pages load without errors
- [ ] Responsive on mobile (375px width)
- [ ] Responsive on tablet (768px width)
- [ ] Responsive on desktop (1920px width)
- [ ] All buttons are clickable
- [ ] Forms validate inputs
- [ ] Success/Error messages display
- [ ] Loading spinners appear during API calls
- [ ] Status badges show correct colors
- [ ] Progress bars display correctly

---

## ✨ Performance Checks

- [ ] Pages load in < 2 seconds
- [ ] No console errors
- [ ] No memory leaks
- [ ] API calls complete in < 5 seconds
- [ ] Smooth animations (no jank)
- [ ] Mobile performance good

---

## 📚 Documentation Review

- [ ] README.md is clear
- [ ] QUICKSTART.md is helpful
- [ ] FOLDER_STRUCTURE.md explains layout
- [ ] API_DOCUMENTATION.md has all endpoints
- [ ] Code comments are helpful
- [ ] Error messages are user-friendly

---

## 🚀 Pre-Production Checklist

- [ ] All features tested and working
- [ ] All pages accessible
- [ ] Responsive on all devices
- [ ] Documentation complete
- [ ] Error handling in place
- [ ] Security measures implemented
- [ ] Performance acceptable
- [ ] API_CONFIG updated to production URL
- [ ] Demo credentials removed or documented
- [ ] Browser compatibility verified

---

## 📞 Quick Reference

### URLs
```
Frontend: http://localhost:8000
Backend:  http://localhost:8080
API:      http://localhost:8080/api
```

### Demo Credentials
```
Customer: john@example.com / SecurePass123
Admin:    admin@example.com / AdminPass123
```

### File Modifications
- API URL: `assets/js/api.js` (line ~3)
- Styles: `assets/css/style.css`
- Utilities: `assets/js/main.js`

### Browser DevTools Shortcuts
```
F12 or Ctrl+Shift+I    - Open DevTools
Ctrl+Shift+C           - Inspect Element
Ctrl+Shift+J           - Console
Ctrl+Shift+E           - Network Tab
Ctrl+Shift+K           - Console (Firefox)
```

---

## 🎉 Ready to Go!

Once you complete these steps, you have a fully functional Supply Chain Financing platform!

**Enjoy and Happy Coding!** 💻✨

---

**Last Updated**: July 9, 2026  
**Version**: 1.0  
**Status**: Production Ready

