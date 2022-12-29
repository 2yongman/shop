package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    //하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 다대일 단방향 매핑 설정
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    //한번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 수량

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        //주문할 상품과 주문 수량을 세팅
        orderItem.setItem(item);
        //주문할 상품과 주문 수량을 세팅
        orderItem.setCount(count);
        //현재 시간 기준으로 상품 가격을 주문 가격으로 세팅. 상품 가격은 시간에 따라서 달라질 수 있다.
        //또한 쿠폰이나 할인을 적용하는 케이스들도 있지만 여기선 고려X
        orderItem.setOrderPrice(item.getPrice());

        //주문 수량 만큼 상품의 재고 수량을 감소 시킴
        item.removeStock(count);
        return orderItem;
    }

    //주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드드
   public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancle(){
        this.getItem().addStock(count);
    }
}
