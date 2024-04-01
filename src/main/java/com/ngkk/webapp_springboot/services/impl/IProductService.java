package com.ngkk.webapp_springboot.services.impl;

import com.ngkk.webapp_springboot.dtos.ProductDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long id) throws DataNotFoundException;
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    boolean existsByName(String name);
}
