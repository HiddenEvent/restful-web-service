
package com.example.restfulwebservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"password"})
public class User {
    private Integer id;
    @Size(min = 2, message = "Name은 2글자 이상 입력해 주세요.")
    private String name;
    @Past //현재 날짜 로부터 과거인 경우만 허용
    private Date joinDate;
    private String password;
    private String ssn;
}