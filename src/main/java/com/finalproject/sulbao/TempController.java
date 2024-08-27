package com.finalproject.sulbao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {

    @GetMapping("/ordercomplete")
    public void ordercomplete() {}

    @GetMapping("/presentcomplete")
    public void presentcomplete() {}
}
