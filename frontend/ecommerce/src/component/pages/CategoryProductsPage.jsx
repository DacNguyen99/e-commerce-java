import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ApiService from "../../service/ApiService";
import ProductList from "../common/ProductList";
import Pagination from "../common/Pagination";
import "../../style/home.css";

const CategoryProductsPage = () => {
  const { categoryId } = useParams();
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState(null);
  const itemsPerPage = 2;

  useEffect(() => {
    fetchProducts();
  });

  const fetchProducts = async () => {
    try {
      const response = await ApiService.getProductsByCategory(categoryId);
      const allProducts = response.productList || [];
      setTotalPages(Math.ceil(allProducts.length / itemsPerPage));
      setProducts(
        allProducts.slice(
          (currentPage - 1) * itemsPerPage,
          currentPage * itemsPerPage
        )
      );
    } catch (error) {
      setError(
        error.response?.data?.message ||
          error.message ||
          "An error occurred while fetching products"
      );
    }
  };

  return (
    <div className="home">
      {error ? (
        <p className="error-message">{error}</p>
      ) : (
        <div>
          <ProductList products={products} />
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

export default CategoryProductsPage;
