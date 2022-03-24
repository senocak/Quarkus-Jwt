package com.github.senocak.dto.auth;

import com.github.senocak.dto.BaseDto;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@JsonPropertyOrder({"name", "username", "email", "roles", "resourceUrl"})
public class UserResponse extends BaseDto {
    @JsonProperty("name")
    private String name;
    private String email;
    private String username;
    private Set<RoleResponse> roles;
    private String resourceUrl;
}
