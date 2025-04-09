import React, { useState, useEffect } from "react";
import ApiService from "../../service/ApiService";
import { useNavigate } from "react-router-dom";
import "../../style/adminCategory.css";

const AdminCategory = () => {
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();
  const [deleteConfirmation, setDeleteConfirmation] = useState("");

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await ApiService.getAllCategories();
      setCategories(response.categoryList || []);
    } catch (error) {
      console.error("Error fetching categories:", error);
    }
  };

  const handleEdit = async (categoryId) => {
    navigate(`/admin/edit-category/${categoryId}`);
  };

  const handleDelete = async (categoryId) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this category?"
    );
    if (confirmed) {
      try {
        await ApiService.deleteCategory(categoryId);
        setDeleteConfirmation("Category deleted successfully");
        setTimeout(() => {
          setDeleteConfirmation("");
        }, 3000);
        fetchCategories();
      } catch (error) {
        console.error("Error deleting category:", error);
      }
    }
  };

  return (
    <div className="admin-category-page">
      <div className="admin-category-list">
        <h2>Categories</h2>
        <button onClick={() => navigate("/admin/add-category")}>
          Add Category
        </button>
        {deleteConfirmation && (
          <p className="delete-confirmation">{deleteConfirmation}</p>
        )}
        <ul>
          {categories.map((category) => (
            <li key={category.id}>
              <span>{category.name}</span>
              <div className="admin-bt">
                <button
                  onClick={() => handleEdit(category.id)}
                  className="admin-btn-edit"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(category.id)}
                  className="admin-btn-delete"
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default AdminCategory;
