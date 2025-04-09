import React, { useEffect, useState } from "react";
import ApiService from "../../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";
import "../../style/addCategory.css";

const EditCategory = () => {
  const { categoryId } = useParams();
  const [name, setName] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategory = async () => {
      try {
        const response = await ApiService.getCategoryById(categoryId);
        setName(response.category.name);
      } catch (error) {
        setMessage(
          error.response?.data?.message ||
            error.message ||
            "Error fetching category"
        );
      }
    };

    fetchCategory();
  }, [categoryId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await ApiService.updateCategory(categoryId, { name });
      if (response.status === 200) {
        setMessage(response.message);
        setTimeout(() => {
          setMessage("");
          navigate("/admin/categories");
        }, 3000);
      }
      setName("");
    } catch (error) {
      setMessage(
        error.response?.data?.message ||
          error.message ||
          "Error updating category"
      );
    }
  };

  return (
    <div className="add-category-page">
      {message && <p className="message">{message}</p>}
      <form onSubmit={handleSubmit} className="category-form">
        <h2>Edit Category</h2>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Category Name"
          required
        />
        <button type="submit">Update</button>
      </form>
    </div>
  );
};

export default EditCategory;
