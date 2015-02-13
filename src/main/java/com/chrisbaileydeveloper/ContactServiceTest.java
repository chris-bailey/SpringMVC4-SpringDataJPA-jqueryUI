package com.chrisbaileydeveloper;

import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.chrisbaileydeveloper.domain.Contact;
import com.chrisbaileydeveloper.service.ContactService;

public class ContactServiceTest {
	public static void main(String[] args) {

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:jpa-app-context.xml");
		ctx.refresh();

		System.out.println("App context initialized successfully");

		ContactService contactService = ctx.getBean("contactService", ContactService.class);

		List<Contact> contacts = contactService.findAll();
		
		for (Contact c: contacts) {
			System.out.println(c);
		}

	}

}
