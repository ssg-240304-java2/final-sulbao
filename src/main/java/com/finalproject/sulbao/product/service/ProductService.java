package com.finalproject.sulbao.product.service;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.finalproject.sulbao.cart.domain.Carts;
import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.cart.repository.CartRepository;
import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.model.entity.ProductComparison;
import com.finalproject.sulbao.product.model.vo.ProductImage;
import com.finalproject.sulbao.product.repository.ProductComparisonRepository;
import com.finalproject.sulbao.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ProductComparisonRepository productComparisonRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, FileService fileService, LoginRepository loginRepository, ProductComparisonRepository productComparisonRepository, ModelMapper modelMapper, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.loginRepository = loginRepository;
        this.productComparisonRepository = productComparisonRepository;
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
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

    public List<ProductComparisonDTO> findByComparison() {
        return productComparisonRepository.findAll().stream()
                .map(comparison -> modelMapper.map(comparison, ProductComparisonDTO.class))
                .collect(Collectors.toList());
    }

    public void addCart(CartDTO cartDTO) {
        log.info("Service CartDTO ============== {}",cartDTO.toString());
        Carts carts = Carts.builder()
                .products(cartDTO.getProducts())
                .totalPrice(cartDTO.getTotalPrice())
                .userId(cartDTO.getUserId())
                .amount(cartDTO.getAmount())
                .build();
        cartRepository.save(carts);
    }

    public List<ProductComparisonDTO> findByComparisonList(String category) {

        List<ProductComparison> comparisonList = new ArrayList<>();

        if(category == null || category.isBlank()){
            comparisonList = productComparisonRepository.findAllByComparison();
        }else{
            comparisonList = productComparisonRepository.findByComparisonCategory(category);
        }
        log.info("Service comparisonList ============== {}",comparisonList.toString());

        List<ProductComparisonDTO> productComparisonList = new ArrayList<>();

        for (ProductComparison comparison : comparisonList) {
            Integer minPrice = productRepository.findByMinProductPrice(comparison.getComparisonNo());
            ProductComparisonDTO productComparisonDTO = ProductComparisonDTO.builder()
                    .comparisonNo(comparison.getComparisonNo())
                    .comparisonName(comparison.getComparisonName())
                    .comparisonDescription(comparison.getComparisonDescription())
                    .comparisonCategory(comparison.getComparisonCategory())
                    .minPrice(minPrice)
                    .productImages(comparison.getComparisonImages())
                    .build();
            productComparisonList.add(productComparisonDTO);
        }

        return productComparisonList;
    }

    //최저가 상품 정보
    public List<ProductDTO> findByProductPriceToComparisonNo(Long comparisonNo) {

        List<Product> productList = productRepository.findByComparison_comparisonNoOrderByProductPriceAsc(comparisonNo);

        return productList.stream()
                .map(product -> new ProductDTO().toDTO(product))
                .collect(Collectors.toList());
    }

    public ProductComparisonDTO findByComparisonNo(Long comparisonNo) {

        // 최저가 비교 상품 정보 취득
        ProductComparison productComparison = productComparisonRepository.findById(comparisonNo).get();

        // 최저 금액 정보 취득
        Integer minPrice = productRepository.findByMinProductPrice(productComparison.getComparisonNo());

        ProductComparisonDTO productComparisonDTO = new ProductComparisonDTO();
        productComparisonDTO.setComparisonNo(productComparison.getComparisonNo());
        productComparisonDTO.setComparisonName(productComparison.getComparisonName());
        productComparisonDTO.setComparisonDescription(productComparison.getComparisonDescription());
        productComparisonDTO.setComparisonCategory(productComparison.getComparisonCategory());
        productComparisonDTO.setProductImages(productComparison.getComparisonImages());
        productComparisonDTO.setMinPrice(minPrice);

        return productComparisonDTO;

    }

    public List<ProductComparisonDTO> findByProductComparsionOrderByDesc() {

        List<ProductComparison> comparisonList = productComparisonRepository.findAllByComparisonOrderByCreateAtDesc();
        log.info("최근 등록된 최저가 비교 상품   comparisonList ============== {}",comparisonList.toString());

        List<ProductComparisonDTO> productComparisonList = new ArrayList<>();

        for (ProductComparison comparison : comparisonList) {
            Integer minPrice = productRepository.findByMinProductPrice(comparison.getComparisonNo());
            ProductComparisonDTO productComparisonDTO = ProductComparisonDTO.builder()
                    .comparisonNo(comparison.getComparisonNo())
                    .comparisonName(comparison.getComparisonName())
                    .comparisonDescription(comparison.getComparisonDescription())
                    .comparisonCategory(comparison.getComparisonCategory())
                    .minPrice(minPrice)
                    .productImages(comparison.getComparisonImages())
                    .build();
            productComparisonList.add(productComparisonDTO);
        }

        return productComparisonList;

    }

    @Transactional
    public void updateProductStock(Long productNo, int amount) {
        productRepository.updateProductStock(productNo, amount);
    }

    @Transactional
    public void updateProductRefund(Integer integer, Long aLong) {
        productRepository.updateProductRefund(integer, aLong);
    }
}
