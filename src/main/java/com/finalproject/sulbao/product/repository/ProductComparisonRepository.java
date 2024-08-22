package com.finalproject.sulbao.product.repository;

import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.entity.ProductComparison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductComparisonRepository extends JpaRepository<ProductComparison, Long> {

    @Query(value = "SELECT c FROM ProductComparison c WHERE c.comparisonNo IN (SELECT p.comparison.comparisonNo FROM Product p GROUP BY p.comparison.comparisonNo HAVING COUNT(p.comparison.comparisonNo) > 0)")
    List<ProductComparison> findAllByComparison();

    @Query(value = "SELECT c FROM ProductComparison c WHERE c.comparisonNo IN (SELECT p.comparison.comparisonNo FROM Product p GROUP BY p.comparison.comparisonNo HAVING COUNT(p.comparison.comparisonNo) > 0) order by c.createdAt desc limit 5")
    List<ProductComparison> findAllByComparisonOrderByCreateAtDesc();
}
