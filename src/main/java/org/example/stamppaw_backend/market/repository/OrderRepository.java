package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Order;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.repository.projection.OrderListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository  extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o.id AS orderId,
               o.totalAmount AS totalAmount,
               o.status AS status,
               o.shippingStatus AS shippingStatus,
               o.createdAt AS createdAt,
               u.nickname AS username
        FROM Order o
        JOIN o.user u
        WHERE (:status IS NULL OR o.status = :status)
        ORDER BY o.createdAt DESC
    """)
    Page<OrderListRow> findAllSummaries(
            @Param("status") OrderStatus status,
            Pageable pageable
    );


    @Query("""
        SELECT o.id AS orderId,
               o.totalAmount AS totalAmount,
               o.status AS status,
               o.shippingStatus AS shippingStatus,
               o.createdAt AS createdAt
        FROM Order o
        WHERE o.user.id = :userId
          AND (:status IS NULL OR o.status = :status)
        ORDER BY o.createdAt DESC
    """)
    Page<OrderListRow> findUserOrders(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :orderStatus WHERE o.id = :orderId")
    void updateOrderStatus(
            @Param("orderId") Long orderId,
            @Param("orderStatus") Enum<?> orderStatus
    );

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.shippingStatus = :shippingStatus WHERE o.id = :orderId")
    void updateShippingStatus(
            @Param("orderId") Long orderId,
            @Param("shippingStatus") Enum<?> shippingStatus
    );
}
