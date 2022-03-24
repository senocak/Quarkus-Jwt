package com.github.senocak.dto.auth;

import com.github.senocak.dto.BaseDto;
import com.github.senocak.util.AppConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse extends BaseDto {
    private AppConstants.RoleName name;
}
