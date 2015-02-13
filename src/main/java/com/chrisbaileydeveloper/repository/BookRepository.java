package com.chrisbaileydeveloper.repository;

import org.springframework.data.repository.CrudRepository;

import com.chrisbaileydeveloper.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
