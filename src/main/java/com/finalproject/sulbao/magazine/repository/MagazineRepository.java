package com.finalproject.sulbao.magazine.repository;


import com.finalproject.sulbao.product.model.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    @Query(value = "select m from Magazine m where m.displayYn = 'Y' and m.publishDate <= now() order by m.publishDate desc")
    List<Magazine> findByDisplayYn();
}
