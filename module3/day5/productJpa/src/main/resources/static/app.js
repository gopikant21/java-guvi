const output = document.getElementById('output');

function print(data) {
  output.textContent = JSON.stringify(data, null, 2);
}

async function apiRequest(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });

  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json')
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    throw new Error(typeof body === 'string' ? body : JSON.stringify(body));
  }

  return body;
}

document.getElementById('customerForm').addEventListener('submit', async (event) => {
  event.preventDefault();

  const payload = {
    name: document.getElementById('customerName').value,
    email: document.getElementById('customerEmail').value,
    address: document.getElementById('customerAddress').value,
    phone: document.getElementById('customerPhone').value
  };

  try {
    const data = await apiRequest('/api/customers/register', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('productForm').addEventListener('submit', async (event) => {
  event.preventDefault();

  const payload = {
    name: document.getElementById('productName').value,
    price: Number(document.getElementById('productPrice').value),
    category: document.getElementById('productCategory').value,
    brand: document.getElementById('productBrand').value,
    stocks: Number(document.getElementById('productStocks').value)
  };

  try {
    const data = await apiRequest('/api/products', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('createOrderForm').addEventListener('submit', async (event) => {
  event.preventDefault();

  const customerId = document.getElementById('orderCustomerId').value;

  try {
    const data = await apiRequest(`/api/orders/customer/${customerId}`, {
      method: 'POST'
    });
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('orderItemForm').addEventListener('submit', async (event) => {
  event.preventDefault();

  const orderId = document.getElementById('itemOrderId').value;
  const payload = {
    productId: Number(document.getElementById('itemProductId').value),
    quantity: Number(document.getElementById('itemQuantity').value)
  };

  try {
    const data = await apiRequest(`/api/orders/${orderId}/items`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('loadCustomersBtn').addEventListener('click', async () => {
  try {
    const data = await apiRequest('/api/customers');
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('loadProductsBtn').addEventListener('click', async () => {
  try {
    const data = await apiRequest('/api/products');
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

document.getElementById('loadOrdersBtn').addEventListener('click', async () => {
  try {
    const data = await apiRequest('/api/orders');
    print(data);
  } catch (error) {
    print({ error: error.message });
  }
});

