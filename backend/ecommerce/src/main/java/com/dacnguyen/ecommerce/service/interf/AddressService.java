package com.dacnguyen.ecommerce.service.interf;

import com.dacnguyen.ecommerce.dto.general.AddressDto;
import com.dacnguyen.ecommerce.dto.response.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}
