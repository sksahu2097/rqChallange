package com.reliaquest.api.model;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse<T> {
    private T data;
    private String status;
}
