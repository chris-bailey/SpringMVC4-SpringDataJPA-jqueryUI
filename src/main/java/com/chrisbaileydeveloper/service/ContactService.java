package com.chrisbaileydeveloper.service;

import java.util.List;

import com.chrisbaileydeveloper.domain.Contact;

public interface ContactService {

		public List<Contact> findAll();

		public Contact findById(Long id);

		public Contact save(Contact contact);
		
		public void delete(Contact contact);

}
