package com.netflux.controller;

import com.netflux.dto.CustomerDto;
import com.netflux.dto.GenreUpdateRequest;
import com.netflux.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Integer id){
        var customerDto = this.service.getCustomer(id);
        return ResponseEntity.ok(customerDto);
    }

    @PatchMapping("/{id}/genre")
    public ResponseEntity<Void> updateGenre(@PathVariable Integer id, @RequestBody GenreUpdateRequest request){
        this.service.updateCustomerGenre(id, request);
        return ResponseEntity.noContent().build(); // 204
    }

}
