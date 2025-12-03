import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import OrderPage from './pages/Order';
import LoginPage from './pages/Login';
import DashboardLayout from './components/DashboardLayout';
import Profile from './pages/dashboard/Profile';
import ItemManagement from './pages/dashboard/ItemManagement';
import OrderManagement from './pages/dashboard/OrderManagement';
import EmployeeManagement from './pages/dashboard/EmployeeManagement';
import PaymentManagement from './pages/dashboard/PaymentManagement';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        
        {/* Dashboard Routes */}
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<Navigate to="/dashboard/profile" replace />} />
          <Route path="profile" element={<Profile />} />
          <Route path="items" element={<ItemManagement />} />
          <Route path="orders" element={<OrderManagement />} />
          <Route path="employees" element={<EmployeeManagement />} />
          <Route path="payments" element={<PaymentManagement />} />
        </Route>

        {/* Standalone POS Route */}
        <Route path="/order" element={<OrderPage />} />
        
        {/* Default Redirect */}
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
