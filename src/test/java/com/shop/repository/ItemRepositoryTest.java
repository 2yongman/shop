package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest // 통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션
@TestPropertySource(locations = "classpath:application-test.properties") //
class ItemRepositoryTest {

    //영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입
    @PersistenceContext
    EntityManager em;

    // ItemRepository를 사용하기 위해 @Autowire 어노테이션을 이용해 Bean 주입
    @Autowired
    ItemRepository itemRepository;

    @Test // 테스트 할 메소드 위에 선언해야 테스트 대상으로 지젖ㅇ
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList() { // 1
        for (int i = 0; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1"); // 2
        for (Item item : itemList) {
            System.out.println(item.toString()); // 3
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetail() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail("테스트 상품11", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    //현재 데이터 베이스에 저장된 가격은 10001~10010이므로 10005보다 작은 1~4가 출력될 것
    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThan() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    //
    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        createItemTest();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

}