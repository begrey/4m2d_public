package com.samyeonyiduk.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List findByUser_Id(@Param(value = "user_id") Long user_id);
    List findByCategory_Id(@Param(value = "category_id") Long category_id);

    // 판매중읜 상품의 탑 10 조회 (찜 순)
    @Query(value = "select * from post where status = 0 order by subscribes desc limit 10", nativeQuery = true)
    List<Posts> findTop10ByOrderBySubscribesDesc();
    // 판매중인 상품의 탑 10 조회(최신 순)
    @Query(value = "select * from post where status = 0 order by created_at desc limit 10", nativeQuery = true)
    List<Posts> findTop10ByOrderByCreatedAtDesc();
    List<Posts> findTop10ByOrderByViewDesc();
    // 검색어로 post 찾기
    @Query(value = "select * from post where title like %:word% or content like %:word%", nativeQuery = true)
    List<Posts> findByWordLike(@Param("word") String word);

    // 생성일 최신순
    List<Posts> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    @Query(value = "select * from post order by created_at desc", nativeQuery = true)
    List<Posts> findAllOrderByCreatedAtDesc();

    // 생성일 오래된 순
    List<Posts> findByCategoryIdOrderByCreatedAtAsc(Long categoryId);

    @Query(value = "select * from post order by created_at asc", nativeQuery = true)
    List<Posts> findAllOrderByCreatedAtAsc();

    // 좋아요 많은 순서
    List<Posts> findByCategoryIdOrderBySubscribesDesc(Long categoryId);

    @Query(value = "select * from post order by subscribes desc", nativeQuery = true)
    List<Posts> findAllOrderBySubscribesDesc();

    // 조회수 많은 순서
    List<Posts> findByCategoryIdOrderByViewDesc(Long categoryId);

    @Query(value = "select * from post order by view desc", nativeQuery = true)
    List<Posts> findAllOrderByViewDesc();

    // 가격 높은 순서
    List<Posts> findByCategoryIdOrderByPriceDesc(Long categoryId);

    @Query(value = "select * from post order by price desc", nativeQuery = true)
    List<Posts> findAllOrderByPriceDesc();

    // 가격 낮은 순서
    List<Posts> findByCategoryIdOrderByPriceAsc(Long categoryId);

    @Query(value = "select * from post order by price asc", nativeQuery = true)
    List<Posts> findAllOrderByPriceAsc();

    // 검색어로 찾은 것을 다시 정렬
    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.created_at desc", nativeQuery = true)
    List<Posts> findByWordLikeOrderByCreatedAtDesc(@Param("word") String word);

    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.created_at asc", nativeQuery = true)
    List<Posts> findByWordLikeOrderByCreatedAtAsc(@Param("word") String word);

    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.view desc", nativeQuery = true)
    List<Posts> findByWordLikeOrderByViewDesc(@Param("word") String word);

    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.subscribes desc", nativeQuery = true)
    List<Posts> findByWordLikeOrderBySubscribesDesc(@Param("word") String word);

    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.price desc", nativeQuery = true)
    List<Posts> findByWordLikeOrderByPriceDesc(@Param("word") String word);

    @Query(value = "select * from (select * from post where title like %:word% or content like %:word%) as p order by p.price asc", nativeQuery = true)
    List<Posts> findByWordLikeOrderByPriceAsc(@Param("word") String word);
}
