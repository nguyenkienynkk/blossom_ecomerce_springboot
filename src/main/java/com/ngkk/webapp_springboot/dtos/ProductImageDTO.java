package com.ngkk.webapp_springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID must be greater >0")
    private Long productId;

    @Size(min = 5, max = 200, message = "Name must be between 3 and 200 characters")
    @JsonProperty("image_url")
    private String imageUrl;
}
