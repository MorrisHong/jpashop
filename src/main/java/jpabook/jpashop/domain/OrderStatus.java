package jpabook.jpashop.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER, READY, CANCEL
}
