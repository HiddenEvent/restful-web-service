package com.example.restfulwebservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private Date timestamp;
    private String errorMessage;
    private String errorObject;
    private String errorField;
    private String requestUrl;

}
