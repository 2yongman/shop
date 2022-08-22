package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "item")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob // 사진을 저장하는 칼럼으로 사용할 수 있기 때문에 @Lob 어노테이션을 붙힌것
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private LocalDateTime regTime; // 등록시간

    private LocalDateTime updateTime; // 수정시간

}
