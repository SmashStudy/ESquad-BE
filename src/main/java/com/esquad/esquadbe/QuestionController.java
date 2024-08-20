package com.esquad.esquadbe;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    @GetMapping("/api/questions")
    public String questions() {
        return "Question";
    }
}
