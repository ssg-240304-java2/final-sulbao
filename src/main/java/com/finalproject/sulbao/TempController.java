package com.finalproject.sulbao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {

    @GetMapping("/index")
    public void index() {}

    @GetMapping("/about")
    public void about() {}

    @GetMapping("/contact")
    public void contact() {}

    @GetMapping("/elements")
    public void elements() {}

    @GetMapping("/services")
    public void services() {}
}
