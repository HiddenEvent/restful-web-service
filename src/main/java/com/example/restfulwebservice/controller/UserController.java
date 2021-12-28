package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.domain.User;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.service.UserDaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserDaoService service;

    @GetMapping("")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }
    @GetMapping("/{id}")
    public User retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("ID ["+id+"] not found");
        }
        return user;
    }

    @PostMapping("")
    public ResponseEntity<User> creatUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        URI lcoation = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        log.info(lcoation.getPath());
        return ResponseEntity.created(lcoation).build();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);
        if (user == null) {
            throw new UserNotFoundException("ID ["+id+"] not found");
        }

    }
}