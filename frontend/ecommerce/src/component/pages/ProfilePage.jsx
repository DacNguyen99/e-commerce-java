import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";
import "../../style/profile.css";
import Pagination from "../common/Pagination";

const ProfilePage = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;
  const navigate = useNavigate();

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = async () => {
    try {
      const response = await ApiService.getLoggedInUserInfo();
      setUserInfo(response.user);
    } catch (error) {
      setError(
        error.response?.data?.message ||
          error.message ||
          "An error occurred while fetching user info"
      );
    }
  };

  if (!userInfo) {
    return <div>Loading user info...</div>;
  }

  const orderItemList = userInfo.orderItemList || [];
  const totalPages = Math.ceil(orderItemList.length / itemsPerPage);
  const paginatedOrders = orderItemList.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <div className="profile-page">
      <h2>Welcome, {userInfo.name}</h2>

      {error ? (
        <p className="error-message">{error}</p>
      ) : (
        <div>
          <p>
            <strong>Name: </strong>
            {userInfo.name}
          </p>
          <p>
            <strong>Email: </strong>
            {userInfo.email}
          </p>
          <p>
            <strong>Phone Number: </strong>
            {userInfo.phoneNumber}
          </p>

          <div>
            <h3>Address</h3>
            {userInfo.addresses && userInfo.addresses.length > 0 ? (
              <div>
                {userInfo.addresses.map((address, index) => (
                  <div key={index} className="address-item">
                    <h4>Address {index + 1}</h4>
                    <p>
                      <strong>Street: </strong>
                      {address.street}
                    </p>
                    <p>
                      <strong>City: </strong>
                      {address.city}
                    </p>
                    <p>
                      <strong>State: </strong>
                      {address.state}
                    </p>
                    <p>
                      <strong>Zip Code: </strong>
                      {address.zipCode}
                    </p>
                    <p>
                      <strong>Country: </strong>
                      {address.country}
                    </p>
                    <button
                      onClick={() => navigate(`/edit-address/${address.id}`)}
                      className="profile-button"
                    >
                      Edit This Address
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <p>No addresses information available</p>
            )}
            <button
              onClick={() => navigate("/add-address")}
              className="profile-button"
            >
              Add New Address
            </button>
          </div>

          <h3>Order History</h3>
          <ul>
            {paginatedOrders.map((order) => (
              <li key={order.id}>
                <img src={order.product?.imageUrl} alt={order.product?.name} />
                <div>
                  <p>
                    <strong>Name: </strong>
                    {order.product?.name}
                  </p>
                  <p>
                    <strong>Status: </strong>
                    {order.status}
                  </p>
                  <p>
                    <strong>Quantity: </strong>
                    {order.quantity}
                  </p>
                  <p>
                    <strong>Price: </strong>
                    {order.price.toFixed(2)}
                  </p>
                </div>
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

export default ProfilePage;
