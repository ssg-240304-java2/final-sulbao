package com.finalproject.sulbao.product.repository;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findBySellerInfo_userNo(long userNo);

    Product findByProductNo(Long productNo);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.displayYn = :#{#product.displayYn} AND p.sellYn = :#{#product.sellYn}" )
    List<Product> findBySearchProductAllCategoryAndDisplayAndSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.productCategory = :#{#product.productCategory} AND p.displayYn = :#{#product.displayYn} AND p.sellYn = :#{#product.sellYn}" )
    List<Product> findBySearchProductCategoryAndDisplayAndSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.productCategory = :#{#product.productCategory} AND p.displayYn = :#{#product.displayYn}" )
    List<Product> findBySearchProductCategoryAndDisplayAndAllSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.productCategory = :#{#product.productCategory} AND p.sellYn = :#{#product.sellYn}" )
    List<Product> findBySearchProductCategoryAndAllDisplayAndSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.productCategory = :#{#product.productCategory}")
    List<Product> findBySearchProductCategoryAndAllDisplayAndAllSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.displayYn = :#{#product.displayYn}")
    List<Product> findBySearchProductAllCategoryAndDisplayAndAllSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%') AND p.sellYn = :#{#product.sellYn}" )
    List<Product> findBySearchProductAllCategoryAndAllDisplayAndSell(@RequestParam("product") ProductDTO product);

    @Query(value = "select p from Product p where p.sellerInfo.userNo= :#{#product.userNo} and p.productName like concat('%', :#{#product.searchInput},'%')" )
    List<Product> findBySearchProductAllCategoryAndAllDisplayAllSell(@RequestParam("product") ProductDTO product);
}
