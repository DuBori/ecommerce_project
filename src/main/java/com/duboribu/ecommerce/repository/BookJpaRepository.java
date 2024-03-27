package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookJpaRepository  extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
}
