import React, { useState, useEffect } from "react";
import ApiService from "../../service/ApiService";
import { useNavigate } from "react-router-dom";
import "../../style/adminProduct.css";
import Pagination from "../common/Pagination";

const AdminProduct = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState(null);
  const itemsPerPage = 5;
  const [deleteConfirmation, setDeleteConfirmation] = useState("");

  useEffect(() => {
    fetchProducts(currentPage);
  }, [currentPage]);

  const fetchProducts = async (currentPage) => {
    try {
      const response = await ApiService.getAllProducts();
      const productList = response.productList || [];
      setTotalPages(Math.ceil(productList.length / itemsPerPage));
      setProducts(
        productList.slice(
          (currentPage - 1) * itemsPerPage,
          currentPage * itemsPerPage
        )
      );
    } catch (error) {
      setError(
        error.response?.data?.message ||
          error.message ||
          "Error fetching products"
      );
    }
  };

  const handleEdit = async (productId) => {
    navigate(`/admin/edit-product/${productId}`);
  };

  const handleDelete = async (productId) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this product?"
    );
    if (confirmed) {
      try {
        await ApiService.deleteProduct(productId);
        setDeleteConfirmation("Product deleted successfully");
        setTimeout(() => {
          setDeleteConfirmation("");
        }, 3000);
        fetchProducts(currentPage);
      } catch (error) {
        setError(
          error.response?.data?.message ||
            error.message ||
            "Error deleting product"
        );
      }
    }
  };

  return (
    <div className="admin-product-list">
      {error ? (
        <p className="error-message">{error}</p>
      ) : (
        <div>
          <h2>Product List</h2>
          <button
            onClick={() => navigate("/admin/add-product")}
            className="product-btn"
          >
            Add Product
          </button>
          {deleteConfirmation && (
            <p className="delete-confirmation">{deleteConfirmation}</p>
          )}
          <ul>
            {products.map((product) => (
              <li key={product.id}>
                <span>{product.name}</span>
                <button
                  className="product-btn"
                  onClick={() => handleEdit(product.id)}
                >
                  Edit
                </button>
                <button
                  className="product-btn-delete"
                  onClick={() => handleDelete(product.id)}
                >
                  Delete
                </button>
              </li>
            ))}
          </ul>
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={(page) => setCurrentPage(page)}
          />
        </div>
      )}
    </div>
  );
};

export default AdminProduct;
