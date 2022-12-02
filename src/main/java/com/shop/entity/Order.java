package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    //주문 상품 엔티티와 일대다 매핑.외래키(order_id)가 order_item 테이블에 있으므로 연관 관계의 주인은
    //OrderItem 엔티티이다. order 엔티티가 주인이 아니기에 'mappedBy'속성으로 연관 관계의 주인을 설정.
    //속성의 값으로 'order'를 적어준 이유는 orderItem에 있는 order에 의해 관리된다.
    //casecade : 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTyoeAll 옵션 설정
    //orphanRemoval = true 고아 객체 제거를 사용하기 위함
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //하나의 주문이 여러 개의 주문 상품을 갖으므로 List 자료형을 사용해 매핑
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
