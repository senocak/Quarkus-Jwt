package com.github.senocak.exception;

import com.github.senocak.util.AppConstants;
import com.github.senocak.util.OmaErrorMessageType;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class ServerExceptionHandler implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable e) {
        log.error("Exception handled: {}", e.getMessage());
        // TODO: not working
        //return AppConstants.generateResponseEntity(e.getStatus(), e.getOmaErrorMessageType(), e.getVariables());
        return AppConstants.generateResponseEntity(Response.Status.BAD_REQUEST, OmaErrorMessageType.GENERIC_SERVICE_ERROR,
                new String[]{});
    }
}
