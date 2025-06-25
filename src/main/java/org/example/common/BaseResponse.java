package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BaseResponse {
    private String code;
    private String message;
    private Map<String, Object> data;


}
