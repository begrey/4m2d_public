package com.samyeonyiduk.domain.titles;

import com.samyeonyiduk.domain.carts.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitlesRepository extends JpaRepository<Titles, Long> {
    List<Titles> findByUserId(Long userId);
}
