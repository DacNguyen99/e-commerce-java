import React, { useState, useEffect } from "react";
import ApiService from "../../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";
import "../../style/addProduct.css";

const EditProduct = () => {
  const { productId } = useParams();
  const [image, setImage] = useState(null);
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [message, setMessage] = useState("");
  const [imageUrl, setImageUrl] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    ApiService.getAllCategories().then((response) => {
      setCategories(response.categoryList || []);
    });

    if (productId) {
      ApiService.getProductsById(productId).then((response) => {
        setImageUrl(response.product.imageUrl);
        setCategoryId(response.product.categoryId);
        setName(response.product.name);
        setDescription(response.product.description);
        setPrice(response.product.price);
      });
    }
  }, [productId]);

  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
    setImageUrl(URL.createObjectURL(e.target.files[0]));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      if (image) {
        formData.append("image", image);
      }
      formData.append("productId", productId);
      formData.append("categoryId", categoryId);
      formData.append("name", name);
      formData.append("description", description);
      formData.append("price", price);

      const response = await ApiService.updateProduct(productId, formData);
      if (response.status === 200) {
        setMessage(response.message);
        setTimeout(() => {
          setMessage("");
          navigate("/admin/products");
        }, 3000);
      }
    } catch (error) {
      setMessage(
        error.response?.data?.message ||
          error.message ||
          "Error updating product"
      );
    }
  };

  return (
    <form onSubmit={handleSubmit} className="product-form">
      <h2>Edit Product</h2>
      {message && <div className="message">{message}</div>}

      <label htmlFor="image">Image</label>
      <input type="file" id="image" onChange={handleImageChange} />
      {imageUrl && <img src={imageUrl} alt={name} />}

      <label htmlFor="categoryId">Category</label>
      <select
        id="categoryId"
        value={categoryId}
        onChange={(e) => setCategoryId(e.target.value)}
        required
      >
        <option value="">Select Category</option>
        {categories.map((category) => (
          <option key={category.id} value={category.id}>
            {category.name}
          </option>
        ))}
      </select>

      <label htmlFor="name">Name</label>
      <input
        type="text"
        id="name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Enter product name"
        required
      />

      <label htmlFor="description">Description</label>
      <textarea
        id="description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        placeholder="Enter product description"
        required
      />

      <label htmlFor="price">Price</label>
      <input
        type="number"
        id="price"
        value={price}
        onChange={(e) => setPrice(e.target.value)}
        placeholder="Enter product price"
        required
      />

      <button type="submit">Update Product</button>
    </form>
  );
};

export default EditProduct;
