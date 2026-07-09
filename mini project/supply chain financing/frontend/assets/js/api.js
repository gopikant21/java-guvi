/**
 * API Service for SCF
 * Handles all backend API calls with centralized configuration
 */

const API_CONFIG = {
    BASE_URL: 'http://localhost:8080/api',
    TIMEOUT: 10000,
    HEADERS: {
        'Content-Type': 'application/json'
    }
};

class APIService {
    constructor() {
        this.token = localStorage.getItem('authToken');
    }

    /**
     * Set authentication token
     */
    setToken(token) {
        this.token = token;
        localStorage.setItem('authToken', token);
    }

    /**
     * Get authentication token
     */
    getToken() {
        return this.token || localStorage.getItem('authToken');
    }

    /**
     * Clear authentication token
     */
    clearToken() {
        this.token = null;
        localStorage.removeItem('authToken');
    }

    /**
     * Get authorization headers
     */
    getHeaders(includeAuth = true) {
        const headers = { ...API_CONFIG.HEADERS };
        if (includeAuth && this.getToken()) {
            headers['Authorization'] = `Bearer ${this.getToken()}`;
        }
        return headers;
    }

    /**
     * Make HTTP request with error handling
     */
    async request(endpoint, method = 'GET', body = null, includeAuth = true) {
        try {
            const url = `${API_CONFIG.BASE_URL}${endpoint}`;
            const options = {
                method,
                headers: this.getHeaders(includeAuth),
                timeout: API_CONFIG.TIMEOUT
            };

            if (body && (method === 'POST' || method === 'PUT')) {
                options.body = JSON.stringify(body);
            }

            const response = await fetch(url, options);
            const data = await response.json();

            if (!response.ok) {
                const error = new Error(data.message || 'API Error');
                error.status = response.status;
                error.data = data;
                throw error;
            }

            return data;
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            throw error;
        }
    }

    // ========== AUTH ENDPOINTS ==========

    /**
     * Register new customer
     */
    async register(userData) {
        return this.request('/auth/register', 'POST', userData, false);
    }

    /**
     * Login customer/admin
     */
    async login(credentials) {
        const response = await this.request('/auth/login', 'POST', credentials, false);
        if (response.token) {
            this.setToken(response.token);
        }
        return response;
    }

    // ========== CUSTOMER ENDPOINTS ==========

    /**
     * Get customer profile
     */
    async getProfile() {
        return this.request('/customer/profile', 'GET');
    }

    /**
     * Update customer profile
     */
    async updateProfile(profileData) {
        return this.request('/customer/profile', 'PUT', profileData);
    }

    /**
     * Apply for loan
     */
    async applyLoan(loanData) {
        return this.request('/customer/loans', 'POST', loanData);
    }

    /**
     * Get customer's loans
     */
    async getMyLoans() {
        return this.request('/customer/loans', 'GET');
    }

    /**
     * Get specific loan details
     */
    async getLoanDetails(loanId) {
        return this.request(`/customer/loans/${loanId}`, 'GET');
    }

    /**
     * Repay loan
     */
    async repayLoan(loanId, repaymentData) {
        return this.request(`/customer/loans/${loanId}/repay`, 'POST', repaymentData);
    }

    /**
     * Get repayment history for a loan
     */
    async getRepaymentHistory(loanId) {
        return this.request(`/customer/loans/${loanId}/repayments`, 'GET');
    }

    // ========== ADMIN ENDPOINTS ==========

    /**
     * Get admin dashboard stats
     */
    async getAdminDashboard() {
        return this.request('/admin/dashboard', 'GET');
    }

    /**
     * Get all customers (admin)
     */
    async getAllCustomers() {
        return this.request('/admin/customers', 'GET');
    }

    /**
     * Get specific customer details (admin)
     */
    async getCustomerDetails(customerId) {
        return this.request(`/admin/customers/${customerId}`, 'GET');
    }

    /**
     * Get all loans with optional filters
     */
    async getAllLoans(filters = {}) {
        let endpoint = '/admin/loans';
        const params = new URLSearchParams();

        if (filters.status) params.append('status', filters.status);
        if (filters.customerId) params.append('customerId', filters.customerId);

        if (params.toString()) {
            endpoint += '?' + params.toString();
        }

        return this.request(endpoint, 'GET');
    }

    /**
     * Get loan details (admin)
     */
    async getAdminLoanDetails(loanId) {
        return this.request(`/admin/loans/${loanId}`, 'GET');
    }

    /**
     * Approve loan (admin)
     */
    async approveLoan(loanId) {
        return this.request(`/admin/loans/${loanId}/approve`, 'PUT');
    }

    /**
     * Reject loan (admin)
     */
    async rejectLoan(loanId, reason) {
        return this.request(`/admin/loans/${loanId}/reject`, 'PUT', { reason });
    }

    /**
     * Disburse loan (admin)
     */
    async disburseLoan(loanId) {
        return this.request(`/admin/loans/${loanId}/disburse`, 'PUT');
    }

    /**
     * Get loan repayments (admin)
     */
    async getAdminLoanRepayments(loanId) {
        return this.request(`/admin/loans/${loanId}/repayments`, 'GET');
    }
}

// Create global API service instance
const api = new APIService();

