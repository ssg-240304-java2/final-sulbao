package com.finalproject.sulbao.magazine.service;

import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.magazine.model.dto.MagazineDTO;
import com.finalproject.sulbao.magazine.repository.MagazineRepository;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Magazine;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.model.vo.MagazineImage;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MagazineService {

    private final MagazineRepository magazineRepository;

    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }


    public void save(MagazineDTO magazineDTO, List<FileDto> fileDtoList) {

        List<MagazineImage> magazineImageList = new ArrayList<>();
        for(FileDto fileDto : fileDtoList){
            MagazineImage magazineImage = new MagazineImage();
            magazineImage.setSaveName(fileDto.getUploadFileName());
            magazineImage.setSaveImgUrl(fileDto.getUploadFileUrl());
            magazineImage.setOriginName(fileDto.getOriginalFileName());
            magazineImageList.add(magazineImage);
        }
        magazineDTO.setMagazineImagesList(magazineImageList);

        Magazine magazine = magazineDTO.toEntity();

        magazineRepository.save(magazine);
    }

    public List<MagazineDTO> findByAll() {
        List<Magazine> magazineList = magazineRepository.findAll();
        return magazineList.stream()
                .map(magazine -> new MagazineDTO().toDTO(magazine))
                .collect(Collectors.toList());
    }

    public void update(MagazineDTO magazineDTO) {

        log.info("Service DTO : {}", magazineDTO);
        Magazine magazineEntity = magazineRepository.findById(magazineDTO.getMagazineNo()).get();
        log.info("Service Entity: {}", magazineEntity);
        magazineEntity.update(magazineDTO);

    }

    public MagazineDTO findById(Long magazineNo) {

        Magazine magazine = magazineRepository.findById(magazineNo).get();
        return new MagazineDTO().toDTO(magazine);

    }

    public List<MagazineDTO> findByDisplayYn() {

        List<Magazine> magazineList = magazineRepository.findByDisplayYn();
        return magazineList.stream()
                .map(magazine -> new MagazineDTO().toDTO(magazine))
                .collect(Collectors.toList());
    }
}
