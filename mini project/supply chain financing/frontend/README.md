# Supply Chain Financing (SCF) - Frontend

A modern, responsive, and user-friendly frontend for the Supply Chain Financing application. Built with HTML5, CSS3 (Bootstrap 5), and vanilla JavaScript.

## 📁 Project Structure

```
frontend/
├── index.html                          # Landing/Home page
├── css/                                # Bootstrap CSS files
│   ├── bootstrap.min.css
│   ├── bootstrap.bundle.min.js
│   └── ... (other Bootstrap files)
├── js/                                 # Bootstrap JavaScript
│   ├── bootstrap.bundle.min.js
│   └── ... (other Bootstrap files)
├── assets/
│   ├── css/
│   │   └── style.css                   # Custom global styles
│   └── js/
│       ├── api.js                      # API service layer
│       └── main.js                     # Utility functions
└── pages/
    ├── register.html                   # Customer registration
    ├── login.html                      # User login
    ├── dashboard.html                  # Customer dashboard
    ├── loans.html                      # My loans list
    ├── loan-details.html               # Loan details & repayment
    ├── profile.html                    # Customer profile
    ├── admin-dashboard.html            # Admin dashboard
    ├── admin-loans.html                # Admin loan management
    └── admin-customers.html            # Admin customer management
```

## 🚀 Features

### Customer Features
- **User Authentication**: Register and login with secure JWT tokens
- **Dashboard**: Overview of loans, statistics, and quick actions
- **Loan Management**: Apply for loans, view status, track repayment progress
- **Loan Details**: View detailed loan information and repayment history
- **Make Repayments**: Process loan repayments with multiple payment modes
- **Profile Management**: Update personal information
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices

### Admin Features
- **Dashboard**: System overview with key statistics
- **Loan Management**: View, approve, reject, and disburse loans
- **Customer Management**: View all customers and their loan activities
- **Advanced Filtering**: Filter loans by status or search customers
- **Repayment Tracking**: View complete repayment history

## 🛠️ Technology Stack

- **HTML5**: Semantic markup and structure
- **CSS3**: Custom styles with Bootstrap 5 framework
- **JavaScript (ES6+)**: Modern vanilla JavaScript (no frameworks)
- **Bootstrap 5**: Responsive UI components
- **Fetch API**: For backend API communication
- **LocalStorage**: For JWT token management

## 📋 Prerequisites

- Modern web browser (Chrome, Firefox, Safari, Edge)
- Backend API running on `http://localhost:8080`
- Basic understanding of how to run a local HTTP server

## ⚙️ Setup Instructions

### 1. Project Setup

```bash
# Navigate to frontend directory
cd frontend

# Start a local HTTP server (required for CORS and proper functionality)
# Using Python 3
python -m http.server 8000

# Or using Node.js (if you have http-server installed)
npx http-server -p 8000

# Or using PHP
php -S localhost:8000
```

### 2. Access the Application

Open your browser and navigate to:
```
http://localhost:8000
```

### 3. Backend Configuration

Ensure the backend is running on `http://localhost:8080`. If your backend is on a different URL, update the API configuration in `assets/js/api.js`:

```javascript
const API_CONFIG = {
    BASE_URL: 'http://your-backend-url:8080/api',
    TIMEOUT: 10000,
    HEADERS: {
        'Content-Type': 'application/json'
    }
};
```

## 🔐 Authentication

### Login Credentials (Demo)

```
Email: john@example.com
Password: SecurePass123
```

**Admin Demo Credentials**: (If admin account exists)
```
Email: admin@example.com
Password: AdminPass123
```

## 📖 API Integration

The frontend uses a centralized API service layer (`assets/js/api.js`) for all backend communications.

### Key API Methods

```javascript
// Authentication
api.register(userData)
api.login(credentials)

// Customer APIs
api.getProfile()
api.updateProfile(profileData)
api.applyLoan(loanData)
api.getMyLoans()
api.getLoanDetails(loanId)
api.repayLoan(loanId, repaymentData)
api.getRepaymentHistory(loanId)

// Admin APIs
api.getAdminDashboard()
api.getAllCustomers()
api.getCustomerDetails(customerId)
api.getAllLoans(filters)
api.approveLoan(loanId)
api.rejectLoan(loanId, reason)
api.disburseLoan(loanId)
```

## 🎨 UI Components & Styling

### Custom CSS Classes

- `.stat-card`: Statistics card with colored left border
- `.loan-card`: Loan information card with hover effects
- `.auth-container`: Centered authentication page container
- `.dashboard-container`: Main dashboard wrapper
- `.empty-state`: Placeholder for empty data states
- `.status-{STATUS}`: Status-specific badge colors

### Bootstrap Components Used

- Navigation bar with collapse on mobile
- Cards for content organization
- Modals for forms and confirmations
- Tables with hover effects
- Progress bars for loan repayment tracking
- Badges for status display
- Responsive grid system

## 🔄 Data Flow

1. **User Registration/Login**
   - User enters credentials
   - Frontend sends request to backend
   - Backend returns JWT token
   - Token stored in localStorage
   - User redirected to appropriate dashboard

2. **Loan Application**
   - User fills loan form
   - Frontend validates input
   - API call with JWT token
   - Dashboard updates with new loan

3. **Loan Repayment**
   - User enters repayment amount
   - Frontend validates amount
   - API call processes repayment
   - Loan status and remaining amount updated

## 🛡️ Security Features

- JWT token-based authentication
- Tokens stored in localStorage
- Token validation before API calls
- Automatic redirect to login on 401 error
- Role-based access control (CUSTOMER/ADMIN)
- CORS enabled for backend communication
- Input validation on client side

## 📱 Responsive Design

The application is fully responsive and optimized for:

- **Desktop**: Full feature set with multi-column layouts
- **Tablet**: Adjusted layouts with single/double columns
- **Mobile**: Single column layout with touch-friendly buttons

## 🐛 Error Handling

The application includes comprehensive error handling:

- API error messages displayed to users
- Form validation with helpful feedback
- Loading spinners during API calls
- Alert notifications for success/failure
- Graceful handling of network errors

## 🎯 Best Practices Implemented

1. **Code Organization**
   - Separation of concerns (API, utilities, styling)
   - Reusable functions and components
   - Clear naming conventions

2. **Performance**
   - Minified Bootstrap CSS/JS
   - Lazy loading where applicable
   - Efficient DOM manipulation
   - Optimized images

3. **Accessibility**
   - Semantic HTML structure
   - ARIA labels where needed
   - Keyboard navigation support
   - Color contrast compliance

4. **User Experience**
   - Consistent navigation across pages
   - Clear error messages
   - Loading states during API calls
   - Confirmation dialogs for critical actions
   - Toast-like notifications

## 📝 Form Validation

### Customer Registration
- Name: Non-empty, max 255 characters
- Email: Valid email format, unique
- Phone: Exactly 10 digits
- Password: Min 6 characters

### Loan Application
- Amount: Greater than 0
- Interest Rate: Greater than 0
- Tenure: At least 1 month
- Purpose: Non-empty

### Loan Repayment
- Amount: Between 100 and remaining balance
- Payment Mode: Must select one
- Remarks: Optional

## 🔧 Troubleshooting

### CORS Issues
- Ensure backend has CORS enabled
- Backend should be running on `http://localhost:8080`
- Check browser console for specific CORS errors

### Token Not Working
- Clear browser cookies and localStorage
- Re-login with valid credentials
- Check if token is properly stored in localStorage

### API Not Found
- Verify backend is running
- Check API_CONFIG.BASE_URL in `assets/js/api.js`
- Check network tab in browser developer tools

## 📚 Additional Resources

- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.0/)
- [API Documentation](./API_DOCUMENTATION.md)
- [MDN Web Docs](https://developer.mozilla.org/)

## 👨‍💻 Development Tips

### Adding New Features

1. Create new page in `pages/` directory
2. Include necessary scripts (bootstrap, api.js, main.js)
3. Follow existing HTML structure and naming patterns
4. Update navigation if needed

### Modifying Styles

- Global styles in `assets/css/style.css`
- Use CSS variables defined in `:root` for consistency
- Follow mobile-first responsive design approach

### API Calls

Always use the centralized `api` object:

```javascript
try {
    const data = await api.someMethod(params);
    // Process data
} catch (error) {
    const message = handleAPIError(error);
    showAlert(message, 'danger');
}
```

## 📄 License

This project is part of the Supply Chain Financing system.

## 🤝 Support

For issues or questions, refer to the API documentation or contact your system administrator.

---

**Last Updated**: July 9, 2026  
**Frontend Version**: 1.0  
**Status**: Production Ready

