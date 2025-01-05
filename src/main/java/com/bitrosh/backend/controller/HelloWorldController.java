package com.bitrosh.backend.controller;


import com.bitrosh.backend.dto.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "hello-world", description = "Hello World Controller")
@RestController
public class HelloWorldController {
    @Operation(summary = "Hello, World!")
    @GetMapping("/hello-world")
    public MessageDto helloWorld() {
        return new MessageDto("Hello, World!");
    }
}
