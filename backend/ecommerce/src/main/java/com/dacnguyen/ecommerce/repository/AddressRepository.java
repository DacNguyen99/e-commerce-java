package com.dacnguyen.ecommerce.repository;

import com.dacnguyen.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
