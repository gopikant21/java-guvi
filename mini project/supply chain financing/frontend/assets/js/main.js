/**
 * Utility Functions for SCF Frontend
 */

/**
 * Format currency as Indian Rupees
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(amount);
}

/**
 * Format date to readable format
 */
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-IN', options);
}

/**
 * Show alert message to user
 */
function showAlert(message, type = 'info', containerId = 'alertContainer') {
    const alertContainer = document.getElementById(containerId);
    if (!alertContainer) return;

    const alertHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;

    alertContainer.innerHTML = alertHTML;

    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const alert = alertContainer.querySelector('.alert');
        if (alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }
    }, 5000);
}

/**
 * Show loading spinner
 */
function showLoader() {
    let loader = document.getElementById('loader');
    if (!loader) {
        loader = document.createElement('div');
        loader.id = 'loader';
        loader.className = 'spinner-overlay';
        loader.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        `;
        document.body.appendChild(loader);
    }
    loader.style.display = 'flex';
}

/**
 * Hide loading spinner
 */
function hideLoader() {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = 'none';
    }
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return !!localStorage.getItem('authToken');
}

/**
 * Redirect to login if not authenticated
 */
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/pages/login.html';
    }
}

/**
 * Logout user
 */
function logout() {
    api.clearToken();
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    window.location.href = '/index.html';
}

/**
 * Get user role from token
 */
function getUserRole() {
    const token = localStorage.getItem('authToken');
    if (!token) return null;

    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')
        );
        return JSON.parse(jsonPayload).role;
    } catch (error) {
        console.error('Error parsing token:', error);
        return null;
    }
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone number (10 digits)
 */
function isValidPhone(phone) {
    return /^\d{10}$/.test(phone);
}

/**
 * Validate password (min 6 characters)
 */
function isValidPassword(password) {
    return password.length >= 6;
}

/**
 * Get status color class
 */
function getStatusColorClass(status) {
    const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'DISBURSED': 'info',
        'PARTIALLY_PAID': 'info',
        'FULLY_PAID': 'success',
        'CLOSED': 'success'
    };
    return statusMap[status] || 'secondary';
}

/**
 * Get status badge HTML
 */
function getStatusBadge(status) {
    const colorClass = getStatusColorClass(status);
    return `<span class="badge bg-${colorClass}">${status.replace(/_/g, ' ')}</span>`;
}

/**
 * Truncate text
 */
function truncateText(text, maxLength) {
    if (text.length > maxLength) {
        return text.substring(0, maxLength) + '...';
    }
    return text;
}

/**
 * Create table row HTML
 */
function createTableRow(data, columns) {
    let html = '<tr>';
    columns.forEach(col => {
        const value = eval(`data.${col.key}`);
        if (col.format) {
            html += `<td>${col.format(value)}</td>`;
        } else {
            html += `<td>${value}</td>`;
        }
    });
    html += '</tr>';
    return html;
}

/**
 * Handle API errors
 */
function handleAPIError(error) {
    console.error('API Error:', error);

    if (error.status === 401) {
        api.clearToken();
        window.location.href = '/pages/login.html';
        return 'Session expired. Please login again.';
    } else if (error.status === 403) {
        return 'You do not have permission to perform this action.';
    } else if (error.status === 404) {
        return 'Resource not found.';
    } else if (error.data && error.data.errors) {
        return Object.values(error.data.errors).join(', ');
    }

    return error.message || 'An error occurred. Please try again.';
}

/**
 * Format loan number display
 */
function formatLoanNumber(loanNumber) {
    return `<strong>${loanNumber}</strong>`;
}

/**
 * Create empty state HTML
 */
function createEmptyState(message, icon = 'inbox') {
    return `
        <div class="empty-state">
            <div class="empty-state-icon">
                <i class="bi bi-${icon}" style="font-size: 3rem; opacity: 0.3;"></i>
            </div>
            <p class="text-muted">${message}</p>
        </div>
    `;
}

/**
 * Check if user is admin
 */
function isAdmin() {
    const role = getUserRole();
    return role === 'ADMIN';
}

/**
 * Redirect if not admin
 */
function requireAdmin() {
    if (!isAuthenticated() || !isAdmin()) {
        window.location.href = '/index.html';
    }
}

/**
 * Format large numbers with comma
 */
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

/**
 * Convert percentage to visual representation
 */
function getProgressPercentage(remaining, total) {
    return Math.round(((total - remaining) / total) * 100);
}

