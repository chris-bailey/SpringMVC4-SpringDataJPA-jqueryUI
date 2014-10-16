package com.chrisbaileydeveloper;

import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.chrisbaileydeveloper.domain.Book;
import com.chrisbaileydeveloper.service.BookService;

public class BookServiceTest {

	public static void main(String[] args) {

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:jpa-app-context.xml");
		ctx.refresh();
		
		System.out.println("App context initialized successfully");
		
		BookService bookService = ctx.getBean("bookService", BookService.class);
		
		List<Book> books = bookService.findAll();
		
		for (Book book: books) {
			System.out.println(book);
		}

	}

}
