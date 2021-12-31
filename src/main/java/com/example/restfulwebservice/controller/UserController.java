package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.domain.User;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.service.UserDaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("ID [" + id + "] not found");
        }
        // HATEOAS
        EntityModel<User> entityModel = EntityModel.of(user);
        Link link = linkTo(
                methodOn(this.getClass()).retrieveAllUsers()
        ).withRel("all-user");
        entityModel.add(link);

        return entityModel;
    }
    // 전체 사용자 목록
//    @GetMapping("/users2")
//    public ResponseEntity<CollectionModel<EntityModel<User>>> retrieveUserList2() {
//        List<EntityModel<User>> result = new ArrayList<>();
//        List<User> users = service.findAll();
//
//        for (User user : users) {
//            EntityModel entityModel = EntityModel.of(user);
//            entityModel.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());
//
//            result.add(entityModel);
//        }
//
//        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel()));
//    }


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
            throw new UserNotFoundException("ID [" + id + "] not found");
        }

    }
}