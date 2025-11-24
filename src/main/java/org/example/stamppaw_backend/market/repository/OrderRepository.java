package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Order;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.entity.ShippingStatus;
import org.example.stamppaw_backend.market.repository.projection.OrderListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o.id AS orderId,
               o.totalAmount AS totalAmount,
               o.status AS status,
               o.shippingFee AS shippingFee,
               o.shippingName AS shippingName,
               o.shippingMobile AS shippingMobile,
               o.shippingAddress AS shippingAddress,
               o.shippingStatus AS shippingStatus,
               o.registeredAt AS registeredAt,
               u.nickname AS username
        FROM Order o
        JOIN o.user u
        WHERE (:status IS NULL OR o.status = :status)
        Order by o.id DESC
    """)
    Page<OrderListRow> findAllSummaries(
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT o.id AS orderId,
               o.totalAmount AS totalAmount,
               o.status AS status,
               o.shippingFee AS shippingFee,
               o.shippingName AS shippingName,
               o.shippingMobile AS shippingMobile,
               o.shippingAddress AS shippingAddress,
               o.shippingStatus AS shippingStatus,
               o.registeredAt AS registeredAt
        FROM Order o
        WHERE o.user.id = :userId AND o.status = :orderStatus
        Order by o.id DESC
    """)
    Page<OrderListRow> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                       @Param("orderStatus") OrderStatus orderStatus,
                                       Pageable pageable);



    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :orderStatus WHERE o.id = :orderId")
    void updateOrderStatus(
            @Param("orderId") Long orderId,
            @Param("orderStatus") OrderStatus orderStatus
    );

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.shippingStatus = :shippingStatus WHERE o.id = :orderId")
    void updateShippingStatus(
            @Param("orderId") Long orderId,
            @Param("shippingStatus")ShippingStatus shippingStatus
    );

    @Query("""
        SELECT o FROM Order o
        left join fetch o.orderItems oi
        left join fetch oi.product p
        left join fetch o.payment pay
        where o.id = :orderId
        Order by o.id DESC
    """)
    Optional<Order> findDetailByOrderId(@Param("orderId") Long orderId);

}
