package com.wootube.ioi.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VerifyKey extends BaseEntity {
	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String verifyKey;

	public VerifyKey(String inActiveUserEmail, String key) {
		this.email = inActiveUserEmail;
		this.verifyKey = key;
	}
}
