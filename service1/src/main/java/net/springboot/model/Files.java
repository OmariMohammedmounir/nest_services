package net.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Files")
public class Files{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Files(Long id, String name, String size) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
	}
	public Files() {
		super();
	}
	private String name ;
	public Files(String name, String size) {
		super();
		this.name = name;
		this.size = size;
	}
	private String size ;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	private String UserId ;
	
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public Files(Long id, String name, String size, String userId) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
		UserId = userId;
	}
	public Files(String name, String size, String userId) {
		super();
		this.name = name;
		this.size = size;
		UserId = userId;
	}
	
}
