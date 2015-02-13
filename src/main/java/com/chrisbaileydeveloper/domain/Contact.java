package com.chrisbaileydeveloper.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "contact")
public class Contact implements Serializable {

	private Long id;
	private int version;
	private String firstName;
	private String lastName;
	private Set<Book> books = new HashSet<Book>();

	public Contact() {
	}

	public Contact(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Contact(String firstName, String lastName, Set<Book> books) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.books = books;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "contact")
	public Set<Book> getBooks() {
		return this.books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public void addBook(Book book) {
		book.setContact(this);
		getBooks().add(book);
	}

	public void removeBook(Book book) {
		getBooks().remove(book);
	}

	/**
	 * Only attempt to print out the list of Books if it is not an empty Set
	 * which is possible since the 'books' field is nullable.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Contact [id=" + id + ", version=" + version + ", firstName="
				+ firstName + ", lastName=" + lastName);

		if (books.size() != 0) {
			sb.append(", books=" + books);
		}

		sb.append("]");

		return sb.toString();
	}

}
