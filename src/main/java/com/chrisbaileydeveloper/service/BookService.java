package com.chrisbaileydeveloper.service;

import java.util.List;

import com.chrisbaileydeveloper.domain.Book;

public interface BookService {

	public List<Book> findAll();

	public Book findById(Long id);

	public Book save(Book book);
	
	public void delete(Book book);

}
