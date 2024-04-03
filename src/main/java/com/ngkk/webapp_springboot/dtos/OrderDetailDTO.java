package com.ngkk.webapp_springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be >0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be >0")
    private Long productId;

    @Min(value = 1, message = "Price must be >=0")
    @JsonProperty("price")
    private Float price;

    @Min(value = 1, message = "Number of product must be >=1")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >=0")
    private Float totalMoney;

    private String color;

}
