package com.github.senocak.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequest {
	@Size(min = 1, max = 25)
	@NotBlank(message="can not be blank")
	public String username;

	@Size(min = 1, max = 25)
	@NotBlank(message="can not be blank")
	public String password;
}
