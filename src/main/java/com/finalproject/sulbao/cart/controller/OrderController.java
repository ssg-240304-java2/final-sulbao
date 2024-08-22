package com.finalproject.sulbao.cart.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.cart.dto.*;

import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.cart.service.OrderService;
import com.finalproject.sulbao.cart.service.OrderProductService;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.repository.ProductRepository;
import com.finalproject.sulbao.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final OrderProductService orderProductService;
    private final SessionHandler sessionHandler;
    private final LoginRepository loginRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public OrderController(OrderService orderService,
                           CartService cartService,
                           OrderProductService orderProductService,
                           SessionHandler sessionHandler,
                           LoginRepository loginRepository, ProductRepository productRepository, ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderProductService = orderProductService;
        this.sessionHandler = sessionHandler;
        this.loginRepository = loginRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }


    @GetMapping("/validateOrder")
    public String validateOrder(@RequestParam("token") String token, Model model, HttpServletRequest request) {
        // 토큰에 해당하는 orderCodeList를 조회
        HttpSession session = request.getSession();
        List<OrderDTO> orderCodeList = orderService.findByToken(token);
        List<CartDTO> cartLists = cartService.findCartByToken(token);
        if (orderCodeList != null) {
            int len = orderCodeList.size();

            session.setAttribute("len", len);

            model.addAttribute("orderCodeList", orderCodeList);
            session.setAttribute("token", orderCodeList.get(0).getToken());
            // orderCodeList를 사용해 필요한 작업 수행
            // 예: 주문 처리, 상태 업데이트 등
            // 작업 완료 후 토큰과 orderCodeList 매핑 삭제
            System.out.println("cartLists = " + cartLists);
            System.out.println("-------------------->>>>>>>>>>>>>>>>>>"+orderCodeList);
            model.addAttribute("carts", cartLists);

            return "cart/success"; // 성공 페이지로 리다이렉션
        }
        else {
            return "error"; // 유효하지 않은 토큰 처리
        }
    }

    @PostMapping("/submitOrder")
    public String submitOrder(
        @RequestParam("orderName") String orderName,
        @RequestParam("orderPhone") String orderPhone,
        @RequestParam("postcode") String postcode,
        @RequestParam("address") String address,
        @RequestParam("detailAddress") String detailAddress,
        Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();

        String token = (String) session.getAttribute("token");
        try {
            orderService.updateOrderNameBytoken(orderName, token);
            orderService.updateOrderPhoneBytoken(orderPhone, token);
            orderService.updatePostCodeBytoken(postcode, token);
            orderService.updateAddress1Bytoken(address, token);
            orderService.updateAddress2Bytoken(detailAddress, token);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

        return "redirect:/presentcomplete"; // 주문 확인 페이지로 리다이렉트 또는 포워드
    }

    @GetMapping("/orderlist")
    public String orderlist(Model model, HttpSession session, Authentication authentication) {
//        if(session.getAttribute("userNo") == null){
//            return "redirect:/login";
//        }

        UserDto userdto = sessionHandler.getUser(authentication);

        if(!sessionHandler.isLogin(authentication)){
            return "redirect:/login";
        }

        String role = "";
        List<ProductDTO> productLists;
        if(userdto.getRoleType() == RoleType.ADMIN){
            productLists = orderProductService.findAll();
            role = "ROLE_ADMIN";
        }else if(userdto.getRoleType() == RoleType.SELLER){
            Long userNo = (Long) session.getAttribute("userNo");
            // 2. 1번을 이용해 판매자의 상품 정보 조회 -> 상품 코드, 상품명
            productLists = orderProductService.findByUserNo(userNo);
            role = "ROLE_SELLER";
        }else{
            return "error";
        }

//        String role = session.getAttribute("role").toString();
        // 1. 판매자 아이디 정보를 조회

        List<Long>productIdList = new ArrayList<>();
        for (ProductDTO productDTO : productLists) {
            productIdList.add(productDTO.getProductNo());
        }
        // 3. order_item 테이블의 2번의 리스트 목록을 조회 -> amount랑 토탈 프라이스, 오더 코드
        List<OrderItemDTO> orderItemList = orderProductService.findByProductNo(productIdList);
        List<OrderDTO> orderList = orderService.findByProductNo(productIdList);
        List<OrderProductDTO> orderProductList = new ArrayList<>();

        System.out.println(orderList);

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderProductDTO orderProductDTO = new OrderProductDTO();

            orderProductDTO.setCode(orderList.get(i).getOrderCode());

            orderProductDTO.setName(orderProductService.findByProductNoName(orderItemList.get(i).getProductNo()));

            orderProductDTO.setOrderName(orderList.get(i).getNames());
            orderProductDTO.setQuantity(orderItemList.get(i).getAmount());
            orderProductDTO.setProductStatus(orderList.get(i).getDelivery());
            orderProductDTO.setTotalPrice(orderItemList.get(i).getTotalPrice());
            if(orderList.get(i).isPresent()){
                orderProductDTO.setPresent("선물결제");
            }else{
                orderProductDTO.setPresent("일반결제");
            }
            orderProductList.add(orderProductDTO);
        }

        model.addAttribute("menu", "order");
        model.addAttribute("submenu", "option");
        model.addAttribute("orderProductList", orderProductList);
        model.addAttribute("role", role);
        return "cart/sellerorder";
    }


    @GetMapping("/searchorderlist")
    public String search(Model model, HttpSession session, @RequestParam(name = "searchInput", required = false) String searchInput,
                @RequestParam(name = "shippingStatus", required = false) String shippingStatus,
                @RequestParam(name = "orderType", required = false) String orderType, Authentication authentication) {

        System.out.println("searchInput = "+searchInput);
        System.out.println("shippingStatus = "+shippingStatus);
        System.out.println("orderType = "+orderType);


//        if(session.getAttribute("userNo") == null){
//            return "redirect:/login";
//        }

        UserDto userdto = sessionHandler.getUser(authentication);

        if(!sessionHandler.isLogin(authentication)){
            return "redirect:/login";
        }


//        List<ProductDTO> productLists;
//        if(session.getAttribute("role").equals("ROLE_ADMIN")){
//            productLists = orderProductService.findAll();
//        }else{
//            Long userNo = (Long) session.getAttribute("userNo");
//            // 2. 1번을 이용해 판매자의 상품 정보 조회 -> 상품 코드, 상품명
//            productLists = orderProductService.findByUserNo(userNo);
//        }
//        String role = session.getAttribute("role").toString();

        String role = "";
        List<ProductDTO> productLists;
        if(userdto.getRoleType() == RoleType.ADMIN){
            productLists = orderProductService.findAll();
            role = "ROLE_ADMIN";
        }else if(userdto.getRoleType() == RoleType.SELLER){
            Long userNo = (Long) session.getAttribute("userNo");
            // 2. 1번을 이용해 판매자의 상품 정보 조회 -> 상품 코드, 상품명
            productLists = orderProductService.findByUserNo(userNo);
            role = "ROLE_SELLER";
        }else{
            return "error";
        }

        List<Long>productIdList = new ArrayList<>();
        for (ProductDTO productDTO : productLists) {
            productIdList.add(productDTO.getProductNo());
        }
        // 3. order_item 테이블의 2번의 리스트 목록을 조회 -> amount랑 토탈 프라이스, 오더 코드
        List<OrderItemDTO> orderItemList = orderProductService.findByProductNo(productIdList);
        List<OrderDTO> orderList = orderService.findByProductNo(productIdList);
        List<OrderProductDTO> orderProductList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderProductDTO orderProductDTO = new OrderProductDTO();
            orderProductDTO.setCode(orderList.get(i).getOrderCode());
            orderProductDTO.setName(orderProductService.findByProductNoName(orderItemList.get(i).getProductNo()));
            orderProductDTO.setOrderName(orderList.get(i).getNames());
            System.out.println("list 추가 =======> "+orderList.get(i).getNames());
            orderProductDTO.setQuantity(orderItemList.get(i).getAmount());
            orderProductDTO.setProductStatus(orderList.get(i).getDelivery());
            orderProductDTO.setTotalPrice(orderItemList.get(i).getTotalPrice());
            if(orderList.get(i).isPresent()){
                orderProductDTO.setPresent("선물결제");
            }else{
                orderProductDTO.setPresent("일반결제");
            }
            if(searchInput == null){

            }else if(!orderProductDTO.getName().contains(searchInput)){
                continue;
            }

            if(shippingStatus.equals("전체")){
            }else if(!orderProductDTO.getProductStatus().equals(shippingStatus)){
                continue;
            }

            if(orderType.equals("전체")){

            }else if(!orderProductDTO.getPresent().equals(orderType)){
                continue;
            }


            orderProductList.add(orderProductDTO);
        }
        System.out.println(orderProductList);
        model.addAttribute("searchInput", searchInput);
        model.addAttribute("shippingStatus", shippingStatus);
        model.addAttribute("orderType", orderType);
        model.addAttribute("orderProductList", orderProductList);
        model.addAttribute("role", role);
        return "cart/sellerorder";
    }

    @PostMapping("/changeStatus")
    public String changeOrderStatus(@RequestParam("status") String status,
                @RequestParam("orderCodes") String orderCodesJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> orderCodes = null;
        try {
            orderCodes = mapper.readValue(orderCodesJson, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        System.out.println("orderCodes = " + orderCodes);
        // 처리 성공 시
        orderService.updateDeliveryByOrderCode(orderCodes, status);
        // 업데이트문
        return "redirect:/orderlist";
    }

    @GetMapping("/myorder")
    public String myOrder(Model model, HttpSession session, Authentication authentication) {

        if(!sessionHandler.isLogin(authentication)){
            return "redirect:/login";
        }

        Long userNo = sessionHandler.getUserId(authentication);
        Login login = loginRepository.findById(userNo).orElseThrow();
        String userId = login.getUserId();
        // 1. 내 오더 리스트 출력
        List<OrderDTO> orderDTOList = orderService.findByUserId(userId);

        System.out.println("orderDTOList = " + orderDTOList);

        List<ProductDTO> productDTOList = new ArrayList<>();
        for(OrderDTO orderDTO : orderDTOList){
            Long productNo = orderDTO.getOrderItems().iterator().next().getProductNo();
            log.info("ProductNo ======== {}" , productNo);
            productDTOList.add(productService.findByProductNo(productNo));

        }
        log.info("productDTOList = " + productDTOList);

        // 2. 제품 이름, 사진 정보 가지고 오기
        List<OrderListDTO> orderListDTOList = new ArrayList<>();
        for(int i = orderDTOList.size() - 1; i > -1; i--){
            OrderListDTO orderListDTO = new OrderListDTO();
            for(int j = 0; j < productDTOList.size(); j++){
                if(Objects.equals(orderDTOList.get(i).getOrderItems().iterator().next().getProductNo(), productDTOList.get(j).getProductNo())){
                    orderListDTO.setPicture(productDTOList.get(j).getProductImages().get(0).getSaveImgUrl());
                    orderListDTO.setCompany(productDTOList.get(j).getSellerInfo().getSellerInfo().getBusinessName());
                    orderListDTO.setName(productDTOList.get(j).getProductName());
                    break;
                }
            }
            orderListDTO.setAmount(orderDTOList.get(i).getOrderItems().iterator().next().getAmount());
            orderListDTO.setTotalPirce(orderDTOList.get(i).getOrderItems().iterator().next().getTotalPrice());
            orderListDTO.setStatus(orderDTOList.get(i).getDelivery());
            orderListDTO.setOrderDate(orderDTOList.get(i).getOrderDate());
            orderListDTOList.add(orderListDTO);
        }

        System.out.println("orderListDTOList = " + orderListDTOList);
        model.addAttribute("orders", orderListDTOList);

        return "cart/memberorder";
    }

}
