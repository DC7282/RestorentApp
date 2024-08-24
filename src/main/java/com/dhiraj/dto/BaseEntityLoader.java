package com.dhiraj.dto;

import java.io.Serializable;
import java.util.Date;

public class BaseEntityLoader implements Serializable{

	private Long id;
    private Date createdAt;
    private Date updatedAt;

	public BaseEntityLoader() {
		super();
	}

	public BaseEntityLoader(Date createdAt, Date updatedAt) {
		super();
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
