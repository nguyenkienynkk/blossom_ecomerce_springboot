package com.ngkk.webapp_springboot.services.impl;

import com.ngkk.webapp_springboot.dtos.ProductDTO;
import com.ngkk.webapp_springboot.dtos.ProductImageDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Product;
import com.ngkk.webapp_springboot.models.ProductImage;
import com.ngkk.webapp_springboot.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(Long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(Long id);
    boolean existsByName(String name);
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
