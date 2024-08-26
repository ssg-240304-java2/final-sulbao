package com.finalproject.sulbao.magazine.controller;

import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.magazine.model.dto.MagazineDTO;
import com.finalproject.sulbao.magazine.service.MagazineService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MagazineController {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final FileService fileService;
    private final MagazineService magazineService;

    public MagazineController(FileService fileService, MagazineService magazineService) {
        this.fileService = fileService;
        this.magazineService = magazineService;
    }

    @GetMapping("/magazine/list")
    public String list(Model model){

        List<MagazineDTO> magazineList = magazineService.findByAll();
        log.info("MagazineList ================ {}", magazineList);

        model.addAttribute("magazineList", magazineList);
        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "mlist");
        return "admin/magazine/list";
    }

    // 매거진 등록 화면 이동
    @GetMapping("/magazine/detail")
    public String detail(Model model){

        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "detail");
        return "admin/magazine/detail";
    }

    // 매거진 등록
    @PostMapping("/magazine/regist")
    public String regist( @ModelAttribute MagazineDTO magazineDTO ){

        //파일업로드
        List<FileDto> fileDtoList = new ArrayList<>();
        for (MultipartFile image : magazineDTO.getMagazineImages()){
            log.info("Controller MultipartFile =============  {}", image.getOriginalFilename());
            FileDto fileDto = fileService.uploadFile(image,uploadDir,"sulbao-file/magazine");
            fileDtoList.add(fileDto);
        }

        //메거진 저장
        magazineService.save(magazineDTO, fileDtoList);

        return "redirect:/magazine/list";
    }

    // 매거진 수정
    @GetMapping("/magazine/update/{magazineNo}")
    public String updatePage(@PathVariable("magazineNo") String magazineNo, Model model){

        log.info("detail ================= {}", magazineNo);

        MagazineDTO magazineDTO = magazineService.findById(Long.parseLong(magazineNo));
        log.info("detail ================= {}", magazineDTO);
        model.addAttribute("magazine", magazineDTO);

        return "admin/magazine/update";
    }

    // 매거진 수정
    @PostMapping("/magazine/update")
    public String update(@ModelAttribute MagazineDTO magazineDTO){

        log.info("Controller update magazineDTO ============== {}", magazineDTO);

        magazineService.update(magazineDTO);

        return "redirect:/magazine/list";
    }

}
