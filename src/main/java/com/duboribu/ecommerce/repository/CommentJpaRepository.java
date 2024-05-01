package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
