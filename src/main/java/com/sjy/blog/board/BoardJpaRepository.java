package com.sjy.blog.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.user u order by b.id")
    Page<Board> findAllUser(Pageable pageable);

    @Query("select b from Board b join fetch b.user where b.id = :id")
    Optional<Board> findByIdUser(@Param("id") Long id);

}
