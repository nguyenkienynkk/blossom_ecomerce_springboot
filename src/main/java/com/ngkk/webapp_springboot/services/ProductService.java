package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.ProductDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Category;
import com.ngkk.webapp_springboot.models.Product;
import com.ngkk.webapp_springboot.repositories.CategoryRepository;
import com.ngkk.webapp_springboot.repositories.ProductRepository;
import com.ngkk.webapp_springboot.services.impl.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductService implements IProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException(
                        "Cannot find category with id "+productDTO.getCategoryId()));
        Product newProduct = Product.builder()
               .name(productDTO.getName())
               .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existsCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Can not find product with id:"+id));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo page(trang) và limit(giới hạn)     
        return null;
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }
}
