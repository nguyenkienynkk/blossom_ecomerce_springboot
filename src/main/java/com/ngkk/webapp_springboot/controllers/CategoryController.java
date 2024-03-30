package com.ngkk.webapp_springboot.controllers;

import com.ngkk.webapp_springboot.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    //http://localhost:8088/api/v1/categories?page=1&limit=10
    @GetMapping
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("getAllCategory ,page = %d limit  =%d", page, limit));
    }

    @PostMapping
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMs = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMs);
        }
        return ResponseEntity.ok(String.format("insertCategory") + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id) {
        return ResponseEntity.ok("This is category with id =" + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Delete categories with id = " + id);
    }

//    @GetMapping
//    public ResponseEntity<String> getAllCategories(
//            @RequestParam("page")     int page,
//            @RequestParam("limit")    int limit
//    ) {
//        return ResponseEntity.ok(String.format("getAllCategories, page = %d, limit = %d", page, limit));
//    }
//    @PostMapping
//    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
//    public ResponseEntity<?> insertCategory(
//            @Valid @RequestBody CategoryDTO categoryDTO,
//            BindingResult result) {
//        if(result.hasErrors()) {
//            List<String> errorMessages = result.getFieldErrors()
//                    .stream()
//                    .map(FieldError::getDefaultMessage)
//                    .toList();
//            return ResponseEntity.badRequest().body(errorMessages);
//        }
//        return ResponseEntity.ok("This is insertCategory"+categoryDTO);
//
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateCategory(@PathVariable Long id) {
//        return ResponseEntity.ok("insertCategory with id = "+id);
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
//        return ResponseEntity.ok("deleteCategory with id = "+id);
//    }
}
