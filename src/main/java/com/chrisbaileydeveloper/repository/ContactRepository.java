package com.chrisbaileydeveloper.repository;

import org.springframework.data.repository.CrudRepository;

import com.chrisbaileydeveloper.domain.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {

}
