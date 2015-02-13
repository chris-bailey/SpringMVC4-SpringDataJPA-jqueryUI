package com.chrisbaileydeveloper.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "book")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int version;
	private String name;
	private String publisher;
	private DateTime dateOfPublication;
	private String description;
	private byte[] photo;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@NotEmpty(message = "{validation.name.NotEmpty.message}")
	@Size(min = 5, max = 100, message = "{validation.name.Size.message}")
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message = "{validation.publisher.NotEmpty.message}")
	@Column(name = "PUBLISHER")
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Column(name = "DATE_OF_PUBLICATION")
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE)
	public DateTime getDateOfPublication() {
		return dateOfPublication;
	}

	public void setDateOfPublication(DateTime dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "PHOTO")
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Transient
	public String getDateOfPublicationString() {
		String dateOfPublicationString = "";
		if (dateOfPublication != null)
			dateOfPublicationString = org.joda.time.format.DateTimeFormat
					.forPattern("yyyy-MM-dd").print(dateOfPublication);
		return dateOfPublicationString;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", version=" + version + ", name=" + name
				+ ", publisher=" + publisher + ", dateOfPublication="
				+ dateOfPublication + ", description=" + description;
	}
}
