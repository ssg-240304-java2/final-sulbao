package com.finalproject.sulbao.magazine.controller;

import com.finalproject.sulbao.magazine.model.dto.MagazineDTO;
import com.finalproject.sulbao.magazine.service.MagazineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class UserMagazineController {

    private final MagazineService magazineService;

    public UserMagazineController(MagazineService magazineService) {
        this.magazineService = magazineService;
    }


    @GetMapping("/magazine")
    public String magazineList(Model model){
        List<MagazineDTO> magazineList =  magazineService.findByDisplayYn();
        model.addAttribute("magazineList", magazineList);
        log.info("magazineList: {}", magazineList);
        return "magazine/list";
    }

    @GetMapping("/magazine/user/{magazineNo}")
    public String hansoolList(Model model, @PathVariable Long magazineNo){

        MagazineDTO magazineDTO= magazineService.findById(magazineNo);
        log.info("magazineNo: {}", magazineDTO);
        model.addAttribute("magazineDTO", magazineDTO);
        return "magazine/detail";
    }
}