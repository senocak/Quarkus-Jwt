package com.github.senocak.exception;

import com.github.senocak.util.OmaErrorMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import javax.ws.rs.core.Response.Status;

@Getter
@Setter
@AllArgsConstructor
public class ServerException extends Exception {
    private final OmaErrorMessageType omaErrorMessageType;
    private final String[] variables;
    private final Status status;
}
