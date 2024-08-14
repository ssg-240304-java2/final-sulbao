package com.finalproject.sulbao.product.service;

import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.model.vo.ProductImage;
import com.finalproject.sulbao.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ProductRepository productRepository;
    private final FileService fileService;
    private final LoginRepository loginRepository;

    public ProductService(ProductRepository productRepository, FileService fileService, LoginRepository loginRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.loginRepository = loginRepository;
    }


    //상품정보 등록
    @Transactional
    public void saveProduct(ProductDTO productDTO){

        //상품이미지 업로드
        FileDto fileDto = fileService.uploadFile(productDTO.getImage(),uploadDir,"sulbao-file/product");


        //상품이미지 리스트 생성
        List<ProductImage> productImageList = List.of(new ProductImage(fileDto.getOriginalFileName(),fileDto.getUploadFileName(),fileDto.getUploadFileUrl()));
        productDTO.setProductImages(productImageList);

        //판매자정보
        Optional<Login> loginInfo = loginRepository.findById(productDTO.getUserNo());
        productDTO.setSellerInfo(loginInfo.get());

        // product 엔티티 생성
        Product product = productDTO.toEntity();

        // 상품등록
        productRepository.save(product);

    }

    // 판매자별 상품 목록조회
    public List<ProductDTO> findByUserNo(ProductDTO productDTO) {

        List<Product> productList = productRepository.findBySellerInfo_userNo(productDTO.getUserNo());

        return productList.stream()
                .map(product -> new ProductDTO().toDTO(product))
                .collect(Collectors.toList());
    }

    // 상품 상세페이지
    public ProductDTO findByProductNo(Long productNo) {

        Product productEntity = productRepository.findByProductNo(productNo);
        return new ProductDTO().toDTO(productEntity);

    }

    //상품수정
    @Transactional
    public void updateProduct(ProductDTO productDTO) {

        if(productDTO.getImage() != null){
            FileDto fileDto = fileService.uploadFile(productDTO.getImage(),uploadDir,"sulbao-file/product");
            ProductImage productImage = new ProductImage();
            productImage.setOriginName(fileDto.getOriginalFileName());
            productImage.setSaveName(fileDto.getUploadFileName());
            productImage.setSaveImgUrl(fileDto.getUploadFileUrl());
            productDTO.setProductImages(List.of(productImage));
        }

        Product productEntity = productRepository.findByProductNo(productDTO.getProductNo());
        productEntity.update(productDTO);

    }

    //상품삭제
    public void delete(String productNoList) {

        String[] productNoArray = productNoList.split(",");

        for (String productNo : productNoArray) {
            productRepository.deleteById(Integer.valueOf(productNo));
        }

    }

    @Transactional
    public void updateStatus(String productNoList, String type, String status) {
        String[] productNoArray = productNoList.split(",");
        for (String productNo : productNoArray) {
            Optional<Product> productInfo = productRepository.findById(Integer.valueOf(productNo));

            ProductDTO productDTO = new ProductDTO();
            productDTO.setDisplayYn(status);
            productDTO.setSellYn(status);

            if(productInfo.isPresent()){
                if(type.equals("display")){
                    productInfo.get().updateDisplay(productDTO);
                }else{
                    productInfo.get().updateSellYn(productDTO);
                }
            }

        }
    }

    public List<ProductDTO> findBySearchInfo(ProductDTO productDTO) {

        List<Product> productList = new ArrayList<>();

        if(productDTO.getProductCategory() != null){
            //카테고리 전체선택아닐때

            if(productDTO.getDisplayYn() != null && !productDTO.getDisplayYn().isBlank()){
                //진열여부 전체 선택아닐때
                if(productDTO.getSellYn() != null && !productDTO.getSellYn().isBlank()){
                    //판매여부가 전체 선택이 아닐때
                    productList = productRepository.findBySearchProductCategoryAndDisplayAndSell(productDTO);
                }else {
                    //판매여부가 전체 선택일때
                    productList = productRepository.findBySearchProductCategoryAndDisplayAndAllSell(productDTO);
                }

            }else{
                //진열여부 전체 선택일때
                if(productDTO.getSellYn() != null && !productDTO.getSellYn().isBlank()){
                    //판매여부가 전체 선택이 아닐때
                    productList = productRepository.findBySearchProductCategoryAndAllDisplayAndSell(productDTO);
                }else {
                    //판매여부가 전체 선택일때
                    productList = productRepository.findBySearchProductCategoryAndAllDisplayAndAllSell(productDTO);

                }
            }

        }else{
            //카테고리 전체선택
            if(productDTO.getDisplayYn() != null&& !productDTO.getDisplayYn().isBlank()){
                //진열여부 전체 선택아닐때

                if(productDTO.getSellYn() != null && !productDTO.getSellYn().isBlank()){
                    //판매여부가 전체 선택이 아닐때
                    productList= productRepository.findBySearchProductAllCategoryAndDisplayAndSell(productDTO);
                }else {
                    //판매여부가 전체 선택일때
                    productList= productRepository.findBySearchProductAllCategoryAndDisplayAndAllSell(productDTO);
                }
            }else{
                //진열여부 전체 선택일때

                if(productDTO.getSellYn() != null && !productDTO.getSellYn().isBlank()){
                    //판매여부가 전체 선택이 아닐때
                    productList = productRepository.findBySearchProductAllCategoryAndAllDisplayAndSell(productDTO);
                }else {
                    //판매여부가 전체 선택일때
                    productList = productRepository.findBySearchProductAllCategoryAndAllDisplayAllSell(productDTO);
                }
            }
        }

        return productList.stream()
                .map(product -> new ProductDTO().toDTO(product))
                .collect(Collectors.toList());
    }
}
