import "./App.css";
import { Routes, Route, BrowserRouter } from "react-router-dom";
import Navbar from "./component/common/Navbar";
import Footer from "./component/common/Footer";
import { CartProvider } from "./component/context/CartContext";
import Home from "./component/pages/Home";
import ProductDetailsPage from "./component/pages/ProductDetailsPage";
import CategoryListPage from "./component/pages/CategoryListPage";
import CategoryProductsPage from "./component/pages/CategoryProductsPage";
import CartPage from "./component/pages/CartPage";
import RegisterPage from "./component/pages/RegisterPage";
import LoginPage from "./component/pages/LoginPage";
import ProfilePage from "./component/pages/ProfilePage";
import AddressPage from "./component/pages/AddressPage";
import AdminPage from "./component/admin/AdminPage";
import { ProtectedRoute, AdminRoute } from "./service/Guard";
import AdminCategory from "./component/admin/AdminCategory";
import AddCategory from "./component/admin/AddCategory";
import EditCategory from "./component/admin/EditCategory";
import AdminProduct from "./component/admin/AdminProduct";
import AddProduct from "./component/admin/AddProduct";
import EditProduct from "./component/admin/EditProduct";

function App() {
  return (
    <BrowserRouter
      future={{
        v7_startTransition: true,
        v7_relativeSplatPath: true,
      }}
    >
      <CartProvider>
        <Navbar />
        <Routes>
          <Route exact path="/" element={<Home />} />
          <Route path="/product/:productId" element={<ProductDetailsPage />} />
          <Route path="/categories" element={<CategoryListPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/category/:categoryId"
            element={<CategoryProductsPage />}
          />

          <Route
            path="/profile"
            element={<ProtectedRoute element={<ProfilePage />} />}
          />
          <Route
            path="/edit-address/:addressId"
            element={<ProtectedRoute element={<AddressPage />} />}
          />
          <Route
            path="/add-address"
            element={<ProtectedRoute element={<AddressPage />} />}
          />

          <Route
            path="/admin"
            element={<AdminRoute element={<AdminPage />} />}
          />

          <Route
            path="/admin/categories"
            element={<AdminRoute element={<AdminCategory />} />}
          />

          <Route
            path="/admin/add-category"
            element={<AdminRoute element={<AddCategory />} />}
          />

          <Route
            path="/admin/edit-category/:categoryId"
            element={<AdminRoute element={<EditCategory />} />}
          />

          <Route
            path="/admin/products"
            element={<AdminRoute element={<AdminProduct />} />}
          />

          <Route
            path="/admin/add-product"
            element={<AdminRoute element={<AddProduct />} />}
          />

          <Route
            path="/admin/edit-product/:productId"
            element={<AdminRoute element={<EditProduct />} />}
          />
        </Routes>
        <Footer />
      </CartProvider>
    </BrowserRouter>
  );
}

export default App;
