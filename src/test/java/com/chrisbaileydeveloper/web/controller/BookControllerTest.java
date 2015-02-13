package com.chrisbaileydeveloper.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.chrisbaileydeveloper.domain.Book;
import com.chrisbaileydeveloper.service.BookService;
import com.chrisbaileydeveloper.test.config.ControllerTestConfig;
import com.chrisbaileydeveloper.web.controller.BookController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ControllerTestConfig.class})
@ActiveProfiles("test")
public class BookControllerTest {
	
	private final List<Book> books = new ArrayList<Book>();
	
	private BookService bookService;
	
	private MessageSource messageSource;
	
	@Before
	public void initBooks() {
		// Initialize list of books for mocked BookService
		Book book = new Book();
		book.setId(1l);
		book.setName("Effective Java");
		book.setPublisher("Addison-Wesley Professional");
		books.add(book);
	}

	@Test
	public void testList() throws Exception {

		bookService = mock(BookService.class);
		when(bookService.findAll()).thenReturn(books);
		
		BookController bookController = new BookController();
		
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
		
		ExtendedModelMap uiModel = new ExtendedModelMap();

		String result = bookController.list(uiModel);

		assertNotNull(result);
		assertEquals("books/list", result);

		List<Book> modelBooks = (List<Book>) uiModel.get("books");
		
		assertEquals(1, modelBooks.size());
	}
	
	@Test
	public void testCreate() {
		
		final Book newBook = new Book();
		newBook.setId(999l);
		newBook.setName("Design Patterns");
		newBook.setPublisher("Addison-Wesley Professional");	
		
		bookService = mock(BookService.class);
		when(bookService.save(newBook)).thenAnswer( new Answer<Book>() {
			public Book answer(InvocationOnMock invocation) throws Throwable {
				books.add(newBook);
				return newBook;
			}		
		});
		
		messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("book_save_success", new Object[]{}, Locale.ENGLISH)).thenReturn("success");
		
		BookController bookController = new BookController();
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
		ReflectionTestUtils.setField(bookController, "messageSource", messageSource);
		
		BindingResult bindingResult = new BeanPropertyBindingResult(newBook, "book");
		ExtendedModelMap uiModel = new ExtendedModelMap();
		HttpServletRequest httpServletRequest = new MockHttpServletRequest(); 
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		Locale locale = Locale.ENGLISH;
		
		String result = bookController.create(newBook, bindingResult, uiModel, httpServletRequest, redirectAttributes, locale, null);

		assertNotNull(result);
		assertEquals("redirect:/books/999", result);
		assertEquals(2, books.size());
	}
}