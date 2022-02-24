package com.appsdeveloperblog.ws.clients.photoappwebclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPageController {

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "index";
    }
}
