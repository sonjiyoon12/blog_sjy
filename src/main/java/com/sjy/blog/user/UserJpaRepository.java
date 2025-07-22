package com.sjy.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {


    @Query("select u from User u where u.username = :username and u.password = :password")
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);

    @Query(value = "select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
