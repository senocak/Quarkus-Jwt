package com.github.senocak.util;

import com.github.senocak.dto.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.ws.rs.core.Response;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public interface AppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "10";
    String MAIL_REGEX = "^\\S+@\\S+\\.\\S+$";
    String TOKEN_HEADER_NAME = "Authorization";
    String TOKEN_PREFIX = "Bearer ";

    String ADMIN = "ADMIN";
    String USER = "USER";

    @Getter
    @AllArgsConstructor
    enum RoleName {
        ROLE_USER(USER),
        ROLE_ADMIN(ADMIN);
        private final String role;
    }


    @Getter
    @AllArgsConstructor
    enum Direction {
        ASC("asc"),
        DESC("desc");
        private final String text;
    }

    /**
     * @param input -- string variable to make it sluggable
     * @return -- sluggable string variable
     */
    static String toSlug(String input) {
        Pattern non_latin = Pattern.compile("[^\\w-]");
        Pattern white_space = Pattern.compile("[\\s]");
        String no_white_space = white_space.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(no_white_space, Normalizer.Form.NFD);
        String slug = non_latin.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * @param status - HttpStatus code
     * @param omaErrorMessageType - an enum variable
     * @param variables - array of strings
     * @return - returned body
     */
    static Response generateResponseEntity(Response.Status status, OmaErrorMessageType omaErrorMessageType, String[] variables){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatusCode(status.getStatusCode());
        ExceptionDto.OmaErrorMessageTypeDto omaErrorMessageTypeDto = new ExceptionDto.OmaErrorMessageTypeDto();
        omaErrorMessageTypeDto.setMessageId(omaErrorMessageType.getMessageId());
        omaErrorMessageTypeDto.setText(omaErrorMessageType.getText());
        exceptionDto.setOmaErrorMessageType(omaErrorMessageTypeDto);
        exceptionDto.setVariables(variables);
        return Response.status(status).entity(exceptionDto).build();
    }

    static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
