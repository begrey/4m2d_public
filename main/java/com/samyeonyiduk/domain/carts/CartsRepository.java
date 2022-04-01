package com.samyeonyiduk.domain.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartsRepository extends JpaRepository<Carts, Long> {
    List<Carts> findByUserId(Long userId);
    Carts findByUser_IdAndPost_Id(Long user_id, Long post_id);
}
