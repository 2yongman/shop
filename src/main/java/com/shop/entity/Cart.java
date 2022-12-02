package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //엔티티를 조회할 때 해당 엔티티와 매핑된 엔티티도 한번에 조회하는 것을 '즉시 로딩'이라고 한다.
    //일대일,다대일로 매핑할 경우 즉시 로딩을 기본 Fetch 전략으로 설정 된다.
    //매핑관계를 맺어줄 때 따로 옵션을 주지 않으면 아래 코드와 같이 FetchType.EAGER(즉시 로딩)로 설정
    @OneToOne(fetch = FetchType.LAZY)
    //OneToOne 어노테이션을 잉ㅇ해 회원 엔티티와 일대일로 매핑
    //JoinColumn 어노테이션을 이용해 매핑할 외래키 지정. name 속성에는 매핑할 외래키의 이름을 설정
    //JounColumn의 name을 명시하지 않으면 JPA가 알아서 ID를 찾지만 컬럼명이 원하는 대로 생성되지 않을
    //수 있기 때문에 직접 지정
    @JoinColumn(name = "member_id")
    private Member member;
}
