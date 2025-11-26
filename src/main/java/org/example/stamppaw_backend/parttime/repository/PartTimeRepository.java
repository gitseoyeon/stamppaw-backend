package org.example.stamppaw_backend.parttime.repository;

import org.example.stamppaw_backend.parttime.entity.PartTime;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PartTimeRepository extends JpaRepository<PartTime, Long> {

    Page<PartTime> findAllByUser(Pageable pageable, User user);

    @Query("select p from PartTime p order by p.registeredAt desc ")
    Page<PartTime> findAllOrderByregisteredAt(Pageable pageable);
}
