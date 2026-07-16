package com.team3.coffee_order.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//* QueryDSL을 쓰기 위한 준비물 - JPAQueryFactory를 스프링 빈으로 등록한다.
//* JPAQueryFactory란?
//- QueryDSL로 쿼리를 "시작"하는 공장이다.
//- 내부적으로 JPA의 EntityManager를 이용해 실제 쿼리를 만들고 실행한다.

//* EntityManager는 어디서 오나?
//- 스프링이 이미 관리하는 JPA의 핵심 객체. 파라미터로 선언하면 스프링이 자동으로 주입해줌.

@Configuration
public class QueryDSLConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }
}
