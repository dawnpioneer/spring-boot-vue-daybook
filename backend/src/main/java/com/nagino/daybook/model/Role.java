package com.nagino.daybook.model;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole authority;

	public Role() {

	}

	public Role(ERole authority) {
		this.authority = authority;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getAuthority() {
		return authority;
	}

	public void setAuthority(ERole authority) {
		this.authority = authority;
	}
}