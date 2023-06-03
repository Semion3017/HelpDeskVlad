package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-controller")
public class TestController {

    @GetMapping
    @CrossOrigin(origins = {"*"})
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<CategoryDto> sayHello(){
        return ResponseEntity.ok(CategoryDto.builder().name("HELLO FROM SECURED ENDPOINT").build());
    }

}
