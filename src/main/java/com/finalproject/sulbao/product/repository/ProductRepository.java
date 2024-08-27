package com.finalproject.sulbao.product.repository;

import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
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

    @Query(value = "select p.productName from Product p where p.productNo = :productNo")
    String findByProductNoName(Long productNo);

    @Query(value = "select min(p.productPrice) from Product p where p.comparison.comparisonNo = :comparisonNo")
    Integer findByMinProductPrice(Long comparisonNo);

    List<Product> findByComparison_comparisonNoOrderByProductPriceAsc(long comparisonNo);

    @Modifying
    @Transactional
    @Query(value = "update Product P Set P.productStock = P.productStock - :amount where P.productNo = :productNo")
    void updateProductStock(Long productNo, int amount);

    @Modifying
    @Transactional
    @Query(value = "update Product p set p.productStock = p.productStock + :integer where p.productNo = :aLong")
    void updateProductRefund(Integer integer, Long aLong);
}
