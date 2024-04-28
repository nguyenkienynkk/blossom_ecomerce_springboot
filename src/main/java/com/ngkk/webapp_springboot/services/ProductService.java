package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.ProductDTO;
import com.ngkk.webapp_springboot.dtos.ProductImageDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.exceptions.InvalidParamExeption;
import com.ngkk.webapp_springboot.models.Category;
import com.ngkk.webapp_springboot.models.Product;
import com.ngkk.webapp_springboot.models.ProductImage;
import com.ngkk.webapp_springboot.repositories.CategoryRepository;
import com.ngkk.webapp_springboot.repositories.ProductImageRepository;
import com.ngkk.webapp_springboot.repositories.ProductRepository;
import com.ngkk.webapp_springboot.responses.ProductResponse;
import com.ngkk.webapp_springboot.services.impl.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existsCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id " + productDTO.getCategoryId()));
        //xem giỏ hàng có tồn tại không
        //nếu tồn tại thì mới lấy ra sản phẩm nên phải tìm sản giỏ hàng theo id của categoryId(Product)
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existsCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id:" + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo page(trang) và limit(giới hạn)     
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            //copy các thuộc tính từ dto sang product
            //có thể sử dụng modelmapper
            Category existsCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setCategory(existsCategory);
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId,
                                           ProductImageDTO productImageDTO) throws Exception {
        Product existsProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Không cho insert 5 ảnh trên 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new DataNotFoundException("You can not upload more than " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " images");
        }
        return productImageRepository.save(newProductImage);
    }
}
