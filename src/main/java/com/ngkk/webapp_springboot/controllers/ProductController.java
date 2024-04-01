package com.ngkk.webapp_springboot.controllers;

import com.ngkk.webapp_springboot.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO,
                                           BindingResult result
//                                           ,@RequestPart("file") MultipartFile file
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file != null) {
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body("File is too large, Maximum 10MB");
                    }
                    //Xem co phai dinh dang file anh khong
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }
                    //Lưu file và cập nhaajt thumbnail trong DTO
                    String fileName = storeFile(file); //Thay thế hàm này với code của bạn để lưu file
                    //lưu vào đối tượng product trong DB =>Làm sau
                }
            }


            return ResponseEntity.ok("Product successfuly with :" + productDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //Neu khong lay ten khi upload tu 2 may khac nhau se bi ghi de len nhau dan den mat file
        //Them uuid vao truoc de dam bao duy nhat
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("getProduct here");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") String productId
    ) {
        return ResponseEntity.ok("getProductById here" + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully with" + id);
    }
}
