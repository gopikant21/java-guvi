# Frontend Folder Structure & File Guide

## 📂 Complete Project Structure

```
frontend/
│
├── index.html                          # 🏠 Landing/Home Page
│                                       # - Platform overview
│                                       # - Feature highlights  
│                                       # - Navigation to login/register
│
├── README.md                           # 📖 Comprehensive documentation
├── QUICKSTART.md                       # ⚡ Quick setup guide
├── API_DOCUMENTATION.md                # 📚 Backend API reference
│
├── css/                                # Bootstrap Framework
│   ├── bootstrap.min.css               # Minified Bootstrap CSS
│   ├── bootstrap.min.css.map           # CSS source map
│   ├── bootstrap-reboot.min.css        # Bootstrap reboot styles
│   ├── bootstrap-grid.min.css          # Bootstrap grid system
│   ├── bootstrap-utilities.min.css     # Bootstrap utilities
│   ├── bootstrap.rtl.min.css           # RTL support
│   └── ... (other Bootstrap variants)
│
├── js/                                 # Bootstrap JavaScript
│   ├── bootstrap.bundle.min.js         # Bundled Bootstrap JS (includes Popper.js)
│   ├── bootstrap.bundle.min.js.map     # JS source map
│   ├── bootstrap.esm.min.js            # ES Module version
│   └── ... (other Bootstrap JS variants)
│
├── assets/
│   ├── css/
│   │   └── style.css                   # 🎨 Custom Global Styles
│   │                                   # - CSS variables for colors
│   │                                   # - Component styles
│   │                                   # - Utility classes
│   │                                   # - Responsive design
│   │                                   # - Animations & transitions
│   │
│   └── js/
│       ├── api.js                      # 🔌 API Service Layer
│       │                               # - Centralized API calls
│       │                               # - JWT token management
│       │                               # - Error handling
│       │                               # - All CRUD operations
│       │
│       └── main.js                     # 🛠️  Utility Functions
│                                       # - Formatting functions
│                                       # - Validation helpers
│                                       # - UI utilities
│                                       # - Authentication helpers
│
└── pages/                              # 📄 HTML Pages
    │
    ├── register.html                   # 👤 Customer Registration
    │                                   # - Form with validation
    │                                   # - Password confirmation
    │                                   # - Phone number validation
    │                                   # - Email validation
    │
    ├── login.html                      # 🔐 User Login
    │                                   # - Email/Password form
    │                                   # - Demo credentials display
    │                                   # - Role-based redirect
    │
    ├── dashboard.html                  # 📊 Customer Dashboard
    │                                   # - Statistics cards
    │                                   # - Recent loans display
    │                                   # - Quick actions
    │                                   # - Apply loan modal
    │
    ├── loans.html                      # 📋 My Loans List
    │                                   # - All customer loans
    │                                   # - Filter by status
    │                                   # - Repayment progress
    │                                   # - Apply new loan button
    │
    ├── loan-details.html               # 💰 Loan Details & Repayment
    │                                   # - Detailed loan info
    │                                   # - Repayment history table
    │                                   # - Repayment form
    │                                   # - Progress visualization
    │
    ├── profile.html                    # 👨‍💼 Customer Profile
    │                                   # - Personal information
    │                                   # - Account settings
    │                                   # - Update profile form
    │                                   # - Sidebar navigation
    │
    ├── admin-dashboard.html            # 👨‍💼 Admin Dashboard
    │                                   # - System KPIs
    │                                   # - Statistics grid
    │                                   # - Recent loans table
    │                                   # - Quick action buttons
    │
    ├── admin-loans.html                # 📋 Admin Loan Management
    │                                   # - All loans list
    │                                   # - Status filters
    │                                   # - Approve/Reject/Disburse
    │                                   # - Detailed loan modal
    │                                   # - Repayment tracking
    │
    └── admin-customers.html            # 👥 Admin Customer Management
                                         # - All customers list
                                         # - Search functionality
                                         # - Customer details modal
                                         # - Loan summary per customer
```

## 📝 File Descriptions

### HTML Pages (pages/)

#### register.html
**Purpose**: Customer registration form  
**Key Features**:
- Form with name, email, phone, password fields
- Password confirmation validation
- Phone number pattern validation (10 digits)
- Success/Error alerts
- Link to login page

#### login.html
**Purpose**: User authentication  
**Key Features**:
- Email and password form
- Demo credentials display
- Role-based redirect (admin vs customer)
- Error handling with user-friendly messages
- Remember me functionality (localStorage)

#### dashboard.html
**Purpose**: Customer main dashboard  
**Key Features**:
- Welcome greeting
- Statistics cards (total loans, approved, pending, outstanding)
- Quick action buttons
- Recent loans display
- Apply for loan modal
- Responsive sidebar

#### loans.html
**Purpose**: List all customer loans  
**Key Features**:
- Complete loan list with pagination support
- Status filter dropdown
- Loan cards with repayment progress
- View details button
- Make repayment quick link
- Apply new loan button

#### loan-details.html
**Purpose**: Detailed loan view and repayment  
**Key Features**:
- Loan information cards
- Repayment progress bar
- Conditional repayment form (only for active loans)
- Repayment history table
- View loan application details
- Back button to loans list

#### profile.html
**Purpose**: Customer profile management  
**Key Features**:
- Sidebar navigation
- Profile update form
- Account information display
- Security settings
- Logout from all devices option
- Read-only email field

#### admin-dashboard.html
**Purpose**: Admin system overview  
**Key Features**:
- KPI cards (customers, loans, statistics)
- Disbursed amount summary
- Recent loans table
- Quick navigation to admin pages
- Dashboard statistics loading

#### admin-loans.html
**Purpose**: Loan management for admins  
**Key Features**:
- Complete loans list
- Status filter
- Customer search filter
- Approve/Reject/Disburse actions
- Detailed loan modal
- Repayment history viewing

#### admin-customers.html
**Purpose**: Customer management for admins  
**Key Features**:
- All customers list with pagination
- Search by name, email, or phone
- Customer details modal
- Loan summary per customer
- Account status display

### JavaScript Files (assets/js/)

#### api.js
**Purpose**: Centralized API communication  
**Key Classes/Functions**:
- `APIService` class - Main API handler
- `setToken()` - Store JWT token
- `getHeaders()` - Get authorization headers
- `request()` - Generic HTTP request method
- Auth methods: `register()`, `login()`
- Customer methods: `getProfile()`, `applyLoan()`, `repayLoan()`
- Admin methods: `getAdminDashboard()`, `approveLoan()`, `rejectLoan()`

**Global Instance**: `api` object available globally

#### main.js
**Purpose**: Utility and helper functions  
**Key Functions**:
- `formatCurrency()` - Format numbers as Indian Rupees
- `formatDate()` - Format dates to readable format
- `showAlert()` - Display alert messages
- `showLoader()` / `hideLoader()` - Loading spinner
- `isAuthenticated()` - Check login status
- `requireAuth()` / `requireAdmin()` - Access control
- `logout()` - Clear session and redirect
- `getUserRole()` - Extract role from JWT token
- Validation functions: `isValidEmail()`, `isValidPhone()`, `isValidPassword()`
- `handleAPIError()` - Centralized error handling
- `getStatusBadge()` - Generate status badge HTML
- `formatLoanNumber()` - Format loan display
- `createEmptyState()` - Empty state HTML

### CSS Files (assets/css/)

#### style.css
**Purpose**: Custom global styling  
**Sections**:
- CSS Variables (colors, spacing)
- Base typography
- Navbar styles
- Hero section styling
- Card components with hover effects
- Forms and inputs
- Buttons with various states
- Alerts with types
- Tables with hover effects
- Status badges (7 types)
- Auth container layout
- Dashboard layout
- Sidebar navigation
- Loan cards
- Empty states
- Loading spinner
- Modal styling
- Responsive design breakpoints
- Animation keyframes

## 🔄 Data Flow Between Files

### Authentication Flow
```
login.html 
    ↓ (user submits)
api.js (api.login())
    ↓ (receives token)
main.js (setToken in localStorage)
    ↓ (redirect based on role)
dashboard.html OR admin-dashboard.html
```

### Loan Application Flow
```
dashboard.html (form submission)
    ↓
api.js (api.applyLoan())
    ↓
main.js (showAlert, formatCurrency, handleAPIError)
    ↓
update dashboard / loans.html
```

### Repayment Flow
```
loan-details.html (repayment form)
    ↓
api.js (api.repayLoan())
    ↓
main.js (handleAPIError, showAlert, formatCurrency)
    ↓
reload loan details with updated status
```

## 🎯 Component Interactions

### index.html → pages/
- register.html: Direct link from "Get Started" button
- login.html: Direct link from "Login" button
- Navigation in navbar

### pages/ → api.js
All pages use centralized API calls through the `api` object:
```javascript
// Example usage in any page
const data = await api.getProfile();
```

### pages/ → main.js
All pages use utility functions:
```javascript
// Example usage in any page
showAlert('Success!', 'success');
const formatted = formatCurrency(5000);
```

### pages/ → style.css
All pages use custom CSS classes for consistent styling:
```html
<div class="stat-card">
<div class="loan-card">
<button class="btn btn-primary">
```

## 📦 Dependencies

### External
- Bootstrap 5 (CSS & JS)
- No other external dependencies!

### Internal
- All pages depend on: api.js, main.js, style.css
- Pages may have inline JavaScript

## 🔐 Authentication Files

Files that handle authentication:
1. `register.html` - Registration logic
2. `login.html` - Login logic
3. `api.js` - Token management
4. `main.js` - Auth check functions

Protected pages (require auth):
- All pages in `pages/` except login.html and register.html

## 🗂️ File Size Reference

```
index.html              ~8 KB
register.html           ~6 KB
login.html              ~5 KB
dashboard.html          ~12 KB
loans.html              ~10 KB
loan-details.html       ~10 KB
profile.html            ~10 KB
admin-dashboard.html    ~8 KB
admin-loans.html        ~14 KB
admin-customers.html    ~12 KB

assets/css/style.css    ~20 KB
assets/js/api.js        ~12 KB
assets/js/main.js       ~15 KB

Bootstrap CSS           ~180 KB
Bootstrap JS            ~80 KB
```

## 🚀 How to Extend

### Add New Page
1. Create file in `pages/` folder
2. Copy navbar and structure from existing page
3. Include required scripts:
   ```html
   <script src="../js/bootstrap.bundle.min.js"></script>
   <script src="../assets/js/api.js"></script>
   <script src="../assets/js/main.js"></script>
   ```
4. Add link in navbar if needed

### Add New API Method
1. Add method in `api.js` APIService class
2. Use in any page as: `api.newMethod()`

### Add New Utility Function
1. Add function in `assets/js/main.js`
2. Use in any page as: `functionName()`

### Add New Styles
1. Add CSS in `assets/css/style.css`
2. Use classes in HTML elements

---

**Version**: 1.0  
**Last Updated**: July 9, 2026  
**Framework**: Bootstrap 5 + Vanilla JS

