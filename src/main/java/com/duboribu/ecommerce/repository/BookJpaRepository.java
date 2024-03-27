package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository  extends JpaRepository<Book, Long> {
}
