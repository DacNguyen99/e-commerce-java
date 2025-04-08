import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ApiService from "../../service/ApiService";
import "../../style/address.css";

const AddressPage = () => {
  const [address, setAddress] = useState({
    street: "",
    city: "",
    state: "",
    zipCode: "",
    country: "",
  });

  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { addressId } = useParams();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await ApiService.getLoggedInUserInfo();
        const selectedAddress = response.user.addresses.find(
          (addr) => addr.id === parseInt(addressId)
        );
        if (selectedAddress) {
          setAddress(selectedAddress);
        }
      } catch (error) {
        setError(
          error.response?.data?.message ||
            error.message ||
            "An error occurred while fetching user info"
        );
      }
    };

    if (addressId) {
      fetchUserInfo();
    }
  }, [addressId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddress((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await ApiService.saveAddress(address);
      navigate("/profile");
    } catch (error) {
      setError(
        error.response?.data?.message ||
          error.message ||
          "An error occurred while saving address"
      );
    }
  };

  return (
    <div className="address-page">
      <h2>{addressId ? "Edit Address" : "Add Address"}</h2>
      {error && <p className="error-message">{error}</p>}

      <form onSubmit={handleSubmit}>
        <label htmlFor="street">Street:</label>
        <input
          type="text"
          id="street"
          name="street"
          value={address.street}
          onChange={handleChange}
          required
        />

        <label htmlFor="city">City:</label>
        <input
          type="text"
          id="city"
          name="city"
          value={address.city}
          onChange={handleChange}
          required
        />

        <label htmlFor="state">State:</label>
        <input
          type="text"
          id="state"
          name="state"
          value={address.state}
          onChange={handleChange}
          required
        />

        <label htmlFor="zipCode">Zip Code:</label>
        <input
          type="text"
          id="zipCode"
          name="zipCode"
          value={address.zipCode}
          onChange={handleChange}
          required
        />

        <label htmlFor="country">Country:</label>
        <input
          type="text"
          id="country"
          name="country"
          value={address.country}
          onChange={handleChange}
          required
        />

        <button type="submit">
          {addressId ? "Update Address" : "Add Address"}
        </button>
      </form>
    </div>
  );
};

export default AddressPage;
