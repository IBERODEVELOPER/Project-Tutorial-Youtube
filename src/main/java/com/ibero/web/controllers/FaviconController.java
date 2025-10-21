package com.ibero.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FaviconController {

    @RequestMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // No devuelve nada, así se evita el error de recurso no encontrado
    }
}
