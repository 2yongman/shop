package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>{
    List<Item> findByItemNm(String itemNM);

    //상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드, 여러 필드 검생
    List<Item> findByItemNmOrItemDetail(String itemNM, String itemDetail);

    //파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드
    List<Item> findByPriceLessThan(Integer price);

    //상품의 가격이 높은순으로 조회
    List<Item> findByPriceOrderByPriceDesc(Integer price);

//    JPQL
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc ")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

//    기존에 작성한 통계용 쿼리처럼 사용할 때 nativeQuery 속성을 사용한다.
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);


}
