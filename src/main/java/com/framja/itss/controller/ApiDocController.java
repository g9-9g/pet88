package com.framja.itss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiDocController {

    /**
     * Redirects to the API documentation page
     */
    @GetMapping("/api/docs")
    public String getApiDocs() {
        return "redirect:/docs/index.html";
    }
} 