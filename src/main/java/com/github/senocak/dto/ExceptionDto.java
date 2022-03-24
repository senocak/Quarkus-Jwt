package com.github.senocak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"statusCode", "error", "variables"})
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("exception")
public class ExceptionDto extends BaseDto {
	private int statusCode;
	@JsonProperty("error")
	private OmaErrorMessageTypeDto omaErrorMessageType;
	private String[] variables;

	@Getter
	@Setter
	@JsonPropertyOrder({"id", "text"})
	public static class OmaErrorMessageTypeDto{
		@JsonProperty("id")
		private String messageId;
		private String text;
	}
}
