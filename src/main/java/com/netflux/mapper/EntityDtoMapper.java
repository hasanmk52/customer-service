package com.netflux.mapper;

import com.netflux.dto.CustomerDto;
import com.netflux.dto.MovieDto;
import com.netflux.entity.Customer;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerDto toDto(Customer customer, List<MovieDto> movies){
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getFavoriteGenre(),
                movies
        );
    }

}
