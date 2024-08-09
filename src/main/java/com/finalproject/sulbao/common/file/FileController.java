package com.finalproject.sulbao.common.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class FileController {

    private final FileService fileService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    public String getUpload() {
        return "testEditor";
    }

    @PostMapping("/image-upload")
    @ResponseBody
    public String uploadImage(@RequestParam MultipartFile file) throws IOException {

        log.debug("check");
        // 1. 파일업로드 처리

        if (!file.isEmpty()) {
            FileDto fileDto = fileService.uploadFiles(file, uploadDir);
            // 2. 업로드한 파일명/저장된 파일명 정보를 DB 등록
            return fileDto.getUploadFileName();
        } else {
            return "error";
        }
    }

}
