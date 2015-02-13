package com.chrisbaileydeveloper.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chrisbaileydeveloper.domain.Book;
import com.chrisbaileydeveloper.repository.BookRepository;
import com.chrisbaileydeveloper.service.BookService;
import com.google.common.collect.Lists;

@Service("bookService")
@Repository
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Transactional(readOnly=true)
	public List<Book> findAll() {
		return Lists.newArrayList(bookRepository.findAll());
	}

	@Transactional(readOnly=true)
	public Book findById(Long id) {
		return bookRepository.findOne(id);
	}

	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void delete(Book book) {
		bookRepository.delete(book);		
	}
	
	

}
