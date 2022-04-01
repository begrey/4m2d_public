package com.samyeonyiduk.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByApiId(@Param(value = "api_id") Long apiId);
    Users findByIntraId(@Param(value = "intra_id") String intraId);

    @Query(value = "select * from user where intra_id = :intraId", nativeQuery = true)
    Optional<Users> findByIntraId2(@Param("intraId") String intraId);
    Optional<Users> findByEmail(String email);
}