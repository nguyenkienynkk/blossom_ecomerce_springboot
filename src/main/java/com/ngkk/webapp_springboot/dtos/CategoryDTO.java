package com.ngkk.webapp_springboot.dtos;

import jakarta.validation.constraints.NotEmpty;
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
public class CategoryDTO {
    @NotEmpty(message = "Name category cannot be empty")
    private String name;
}
