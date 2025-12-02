import React, { useState } from 'react';
import '../../css/Order.css';
import logo from '../assets/logo.png';

// Mock Data
const CATEGORIES = [
  { id: 'burger', name: 'Burgers', icon: '🍔' },
  { id: 'pizza', name: 'Pizza', icon: '🍕' },
  { id: 'drink', name: 'Drinks', icon: '🥤' },
  { id: 'dessert', name: 'Dessert', icon: '🍦' },
  { id: 'snack', name: 'Snacks', icon: '🍟' },
];

const MENU_ITEMS = [
  { id: 1, category: 'burger', name: 'Teddy Classic', price: 8.99, image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=500&q=60', desc: 'Beef patty, cheddar, lettuce, tomato, house sauce' },
  { id: 2, category: 'burger', name: 'Double Trouble', price: 12.99, image: 'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?auto=format&fit=crop&w=500&q=60', desc: 'Double beef, double cheese, bacon, onion rings' },
  { id: 3, category: 'burger', name: 'Chicken Crunch', price: 9.50, image: 'https://images.unsplash.com/photo-1615557960916-5f4791effe9d?auto=format&fit=crop&w=500&q=60', desc: 'Crispy chicken, spicy mayo, pickles' },
  { id: 4, category: 'pizza', name: 'Margherita', price: 10.00, image: 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=500&q=60', desc: 'Tomato sauce, mozzarella, basil' },
  { id: 5, category: 'pizza', name: 'Pepperoni Feast', price: 14.50, image: 'https://images.unsplash.com/photo-1628840042765-356cda07504e?auto=format&fit=crop&w=500&q=60', desc: 'Double pepperoni, extra cheese' },
  { id: 6, category: 'drink', name: 'Honey Lemon Tea', price: 3.50, image: 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=500&q=60', desc: 'Freshly brewed tea with honey and lemon' },
  { id: 7, category: 'drink', name: 'Berry Smoothie', price: 5.00, image: 'https://images.unsplash.com/photo-1623593688280-a503c00213cb?auto=format&fit=crop&w=500&q=60', desc: 'Mixed berries, yogurt, mint' },
  { id: 8, category: 'snack', name: 'Golden Fries', price: 3.99, image: 'https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?auto=format&fit=crop&w=500&q=60', desc: 'Crispy salted french fries' },
  { id: 9, category: 'snack', name: 'Onion Rings', price: 4.50, image: 'https://images.unsplash.com/photo-1639024471283-03518883512d?auto=format&fit=crop&w=500&q=60', desc: 'Battered and fried onion rings' },
  { id: 10, category: 'dessert', name: 'Choco Lava', price: 6.50, image: 'https://images.unsplash.com/photo-1624353365286-3f8d62daad51?auto=format&fit=crop&w=500&q=60', desc: 'Warm chocolate cake with molten center' },
];

const OrderPage = () => {
  const [activeCategory, setActiveCategory] = useState('burger');
  const [cart, setCart] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');

  const filteredItems = MENU_ITEMS.filter(item => 
    item.category === activeCategory && 
    item.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const addToCart = (item) => {
    setCart(prev => {
      const existing = prev.find(i => i.id === item.id);
      if (existing) {
        return prev.map(i => i.id === item.id ? { ...i, qty: i.qty + 1 } : i);
      }
      return [...prev, { ...item, qty: 1 }];
    });
  };

  const updateQty = (id, delta) => {
    setCart(prev => prev.map(item => {
      if (item.id === id) {
        const newQty = Math.max(0, item.qty + delta);
        return { ...item, qty: newQty };
      }
      return item;
    }).filter(item => item.qty > 0));
  };

  const subtotal = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);
  const tax = subtotal * 0.1;
  const total = subtotal + tax;

  return (
    <div className="order-page">
      {/* Sidebar Navigation */}
      <div className="sidebar">
        <div className="logo-container">
          <img src={logo} alt="TeddyBite" className="logo-img" />
        </div>
        {CATEGORIES.map(cat => (
          <div 
            key={cat.id} 
            className={`nav-item ${activeCategory === cat.id ? 'active' : ''}`}
            onClick={() => setActiveCategory(cat.id)}
          >
            <span className="nav-icon">{cat.icon}</span>
            <span>{cat.name}</span>
          </div>
        ))}
      </div>

      {/* Main Content */}
      <div className="main-content">
        <div className="header">
          <div className="header-title">
            <h2 className="category-title">{CATEGORIES.find(c => c.id === activeCategory)?.name} Menu</h2>
            <p style={{color: '#777'}}>Choose your favorite meal 😋</p>
          </div>
          <div className="search-bar">
            <span>🔍</span>
            <input 
              type="text" 
              placeholder="Search menu..." 
              className="search-input"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
        </div>

        <div className="menu-grid">
          {filteredItems.map(item => (
            <div key={item.id} className="menu-card" onClick={() => addToCart(item)}>
              <div className="card-image">
                <img src={item.image} alt={item.name} />
              </div>
              <div className="card-details">
                <h3 className="card-title">{item.name}</h3>
                <p className="card-desc">{item.desc}</p>
                <div className="card-footer">
                  <span className="card-price">${item.price.toFixed(2)}</span>
                  <button className="add-btn">+</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Order Summary Sidebar */}
      <div className="order-sidebar">
        <div className="order-header">
          <h2 className="order-title">Current Order</h2>
          <span className="order-id">#092834</span>
        </div>

        <div className="order-list">
          {cart.length === 0 ? (
            <div style={{textAlign: 'center', color: '#999', marginTop: '50px'}}>
              <p>No items in cart</p>
              <p style={{fontSize: '3rem'}}>🛒</p>
            </div>
          ) : (
            cart.map(item => (
              <div key={item.id} className="order-item">
                <img src={item.image} alt={item.name} className="order-item-img" />
                <div className="order-item-details">
                  <div className="order-item-name">{item.name}</div>
                  <div className="order-item-price">${(item.price * item.qty).toFixed(2)}</div>
                </div>
                <div className="order-item-qty">
                  <button className="qty-btn" onClick={() => updateQty(item.id, -1)}>-</button>
                  <span className="qty-val">{item.qty}</span>
                  <button className="qty-btn" onClick={() => updateQty(item.id, 1)}>+</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="order-footer">
          <div className="summary-row">
            <span>Subtotal</span>
            <span>${subtotal.toFixed(2)}</span>
          </div>
          <div className="summary-row">
            <span>Tax (10%)</span>
            <span>${tax.toFixed(2)}</span>
          </div>
          <div className="summary-row total">
            <span>Total</span>
            <span>${total.toFixed(2)}</span>
          </div>
          <button className="checkout-btn">
            Checkout Now
          </button>
        </div>
      </div>
    </div>
  );
};

export default OrderPage;
