package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception {
        //given
        Member member = createMember("회원1");

        Item book = createBook("토비의 스프링", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(1, getOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }



    @Test
    void 주문취소() throws Exception {
        //given
        Member member = createMember("grace");
        Item book = createBook("토비스프링", 20000, 10);

        int orderCount = 9;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        assertEquals(1, book.getStockQuantity());
        orderService.cancelOrder(orderId);


        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals(10, book.getStockQuantity());
    }


    @Test
    void 상품주문_재고수량_초과() throws Exception {
        //given
        Member member = createMember("회원1");
        Item item = createBook("토비스프링", 10000, 10);

        int orderCount = 11;

        //when
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(),item.getId(), orderCount));

        //then
    }

    private Item createBook(String name, int price, int quantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("용인시", "처인구", "111-111"));
        em.persist(member);
        return member;
    }

}