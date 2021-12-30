package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.domain.User;
import com.example.restfulwebservice.domain.UserV2;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.service.UserDaoService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
    private final UserDaoService service;

    @GetMapping("/user")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name","password", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        return mapping;
    }

    //@GetMapping("/v1/user/{id}") // uri로 버저닝 처리 -- 일반 브라우저에서 실행 O
    //@GetMapping(value = "/user/{id}", params = "version=1") // 파라미터로 버저닝 처리 -- 일반 브라우저에서 실행 O
    //@GetMapping(value = "/user/{id}", headers = "X-API-VERSION=1") // 헤더 버저닝 처리 -- 일반 브라우저에서 실행 X
    @GetMapping(value = "/user/{id}", produces = "application/vnd.company.appv1+json") // mime 타입으로 버저닝 처리 -- 일반 브라우저에서 실행 X
    public MappingJacksonValue retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("ID [" + id + "] not found");
        }
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name","password", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return mapping;
    }

    //@GetMapping("/v2/user/{id}")
    //@GetMapping(value = "/user/{id}", params = "version=2") // 파라미터로 버저닝 처리
    //@GetMapping(value = "/user/{id}", headers = "X-API-VERSION=2") // 헤더 버저닝 처리
    @GetMapping(value = "/user/{id}", produces = "application/vnd.company.appv2+json") // mime 타입으로 버저닝 처리
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("ID [" + id + "] not found");
        }
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); /* 공통된 속성이 있을 경우 객체의 속성을 복사하는 기능*/
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name","joinDate", "grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);
        return mapping;
    }

    @PostMapping("/user")
    public ResponseEntity<User> creatUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        URI lcoation = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        log.info(lcoation.getPath());
        return ResponseEntity.created(lcoation).build();
    }
}