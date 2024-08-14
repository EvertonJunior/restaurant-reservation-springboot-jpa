package com.restauranteexemplo.reserva.dto;

import java.io.Serializable;

import com.restauranteexemplo.reserva.entities.Client;

public class ClientDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public ClientDTO() {
		
	}
	
	public ClientDTO(Client client) {
		this.id = client.getId();
		this.name = client.getName();
	}

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

}
