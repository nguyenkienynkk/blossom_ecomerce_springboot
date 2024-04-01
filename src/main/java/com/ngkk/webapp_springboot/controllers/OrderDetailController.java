package com.ngkk.webapp_springboot.controllers;

import com.ngkk.webapp_springboot.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("Create Order Detail here");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok("Get Order Detail here" + id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@Valid @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok("Get Order Detail by Order Id here" + orderId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                                @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("Update Order Detail here" + id+ "with info is:"+orderDetailDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }

}
