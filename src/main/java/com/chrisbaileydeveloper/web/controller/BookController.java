package com.chrisbaileydeveloper.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chrisbaileydeveloper.domain.Book;
import com.chrisbaileydeveloper.service.BookService;
import com.chrisbaileydeveloper.web.form.Message;
import com.chrisbaileydeveloper.web.util.UrlUtil;

@RequestMapping("/")
@Controller
public class BookController {

	final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * List all books.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel) {
		logger.info("Listing books");

		List<Book> books = bookService.findAll();
		uiModel.addAttribute("books", books);

		logger.info("No. of books: " + books.size());

		return "books/list";
	}

	/**
	 * Retrieve the book with the specified id.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		logger.info("Listing book with id: " + id);

		Book book = bookService.findById(id);
		uiModel.addAttribute("book", book);

		return "books/show";
	}

	/**
	 * Retrieve the book with the specified id for the update form.
	 */
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("book", bookService.findById(id));
		return "books/update";
	}

	/**
	 * Update the book with the specified id.
	 */
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String update(@Valid Book book, @PathVariable("id") Long id,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale,
			@RequestParam(value = "file", required = false) Part file) {
		logger.info("Updating book");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"book_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("book", book);
			return "books/update";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute(
				"message",
				new Message("success", messageSource.getMessage(
						"book_save_success", new Object[] {}, locale)));

		// Process upload file
		if (file != null && file.getSize() != 0) {
			logger.info("File name: " + file.getName());
			logger.info("File size: " + file.getSize());
			logger.info("File content type: " + file.getContentType());

			byte[] fileContent = null;

			try {
				InputStream inputStream = file.getInputStream();
				if (inputStream == null)
					logger.info("File inputstream is null");
				fileContent = IOUtils.toByteArray(inputStream);
				book.setPhoto(fileContent);
			} catch (IOException ex) {
				logger.error("Error saving uploaded file");
			}
		} else {
			// Set the photo to the one already uploaded
			Book savedBook = bookService.findById(id);
			book.setPhoto(savedBook.getPhoto());
		}

		bookService.save(book);
		return "redirect:/"
				+ UrlUtil.encodeUrlPathSegment(book.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("book", new Book());
		return "books/create";
	}

	/**
	 * Create a new book.
	 */
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String create(@Valid Book book, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale,
			@RequestParam(value = "file", required = false) Part file) {
		logger.info("Creating book");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"book_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("book", book);
			return "books/create";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute(
				"message",
				new Message("success", messageSource.getMessage(
						"book_save_success", new Object[] {}, locale)));

		// Process upload file
		if (file != null) {
			logger.info("File name: " + file.getName());
			logger.info("File size: " + file.getSize());
			logger.info("File content type: " + file.getContentType());

			byte[] fileContent = null;

			try {
				InputStream inputStream = file.getInputStream();
				if (inputStream == null)
					logger.info("File inputstream is null");
				fileContent = IOUtils.toByteArray(inputStream);
				book.setPhoto(fileContent);
			} catch (IOException ex) {
				logger.error("Error saving uploaded file");
			}
			book.setPhoto(fileContent);
		}

		bookService.save(book);
		return "redirect:/"
				+ UrlUtil.encodeUrlPathSegment(book.getId().toString(),
						httpServletRequest);
	}

	/**
	 * Returns the photo for the book with the specified id.
	 */
	@RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public byte[] downloadPhoto(@PathVariable("id") Long id) {

		Book book = bookService.findById(id);

		if (book.getPhoto() != null) {
			logger.info("Downloading photo for id: {} with size: {}",
					book.getId(), book.getPhoto().length);
		}

		return book.getPhoto();
	}

	/**
	 * Deletes the book with the specified id.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, Model uiModel, Locale locale) {
		logger.info("Deleting book with id: " + id);
		Book book = bookService.findById(id);

		if (book != null) {
			bookService.delete(book);
			logger.info("Book deleted successfully");

			uiModel.addAttribute(
					"message",
					new Message("success", messageSource.getMessage(
							"book_delete_success", new Object[] {}, locale)));
		}

		List<Book> books = bookService.findAll();
		uiModel.addAttribute("books", books);

		return "books/list";
	}
}
