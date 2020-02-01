package jpabook.jpashop;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    @Builder
    public Member(String username) {
        this.username = username;
    }

    public Member() {
    }
}
