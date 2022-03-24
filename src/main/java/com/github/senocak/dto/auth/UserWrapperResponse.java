package com.github.senocak.dto.auth;

import com.github.senocak.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"user", "token"})
public class UserWrapperResponse extends BaseDto {
	@JsonProperty("user")
	private UserResponse userResponse;
	private String token;
}