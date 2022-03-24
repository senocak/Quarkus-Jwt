package com.github.senocak.service;

import com.github.senocak.exception.ServerException;
import com.github.senocak.util.OmaErrorMessageType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.ValidatorTypeCode;
import lombok.extern.slf4j.Slf4j;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class JsonSchemaValidator {
    private static final String EMPTY_BODY = "Request must have a body";
    private static final String UNABLE_VALIDATE = "Unable to validate json message due to an exception";
    private static final String ATTRIBUTE = "attribute";
    private static final char DOT = '.';
    private static final char COLON = ':';

    private final ObjectMapper objectMapper;
    private final JsonSchemaGenerator jsonSchemaGenerator;

    public JsonSchemaValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);
    }

    public void validateJsonSchema(Object jsonBodyToValidate, Class<?> jsonSchemaClass) throws ServerException {
        if (Objects.isNull(jsonBodyToValidate)) {
            log.error(EMPTY_BODY);
            throw new ServerException(OmaErrorMessageType.JSON_SCHEMA_VALIDATOR,
                    new String[]{EMPTY_BODY}, Response.Status.BAD_REQUEST);
        }

        final Set<ValidationMessage> validationResult;
        try {
            validationResult = validateJsonMsgFromObject(jsonBodyToValidate, jsonSchemaClass);
        } catch (Exception ex) {
            log.error(UNABLE_VALIDATE);
            throw new ServerException(OmaErrorMessageType.JSON_SCHEMA_VALIDATOR,
                    new String[]{UNABLE_VALIDATE}, Response.Status.BAD_REQUEST);
        }
        checkValidationResult(validationResult);
    }

    private Set<ValidationMessage> validateJsonMsgFromObject(final Object objectToValidate,
                                                             final Class<?> jsonSchemaClass) throws IOException {
        final JsonNode jsonNodeForSchema = jsonSchemaGenerator.generateJsonSchema(jsonSchemaClass);
        JsonSchema schema = JsonSchemaFactory.getInstance().getSchema(jsonNodeForSchema);
        JsonNode jsonToValidate;
        if (objectToValidate instanceof String) {
            log.info("objectToValidate is instanceof String");
            jsonToValidate = objectMapper.readTree((String) objectToValidate);
        } else {
            log.info("objectToValidate is not instanceof String");
            jsonToValidate = objectMapper.valueToTree(objectToValidate);
        }
        return schema.validate(jsonToValidate);
    }

    private void checkValidationResult(Set<ValidationMessage> validationResults) throws ServerException {
        boolean result = validationResults.isEmpty();
        log.info("ValidationResult: {}", result);
        if (!result) {
            log.info("As a rule, we'll only notify the client about the first validation error");
            final ValidationMessage errorMessage = validationResults.iterator().next();
            final ValidatorTypeCode failureCode = ValidatorTypeCode.fromValue(errorMessage.getType());
            String failedField;

            // Normally we can get the failedField pretty easily, except if it's a REQUIRED orADDITIONAL_PROPERTIES type failure. We have to process stuff a bit differently if it is one of these two cases.
            switch (failureCode) {
                case REQUIRED:
                case ADDITIONAL_PROPERTIES:
                    String message = errorMessage.getMessage();
                    failedField = message.substring(message.lastIndexOf(DOT) + 1, message.lastIndexOf(COLON));
                    break;
                default:
                    failedField = errorMessage.getPath().substring(errorMessage.getPath().lastIndexOf(DOT) + 1);
                    break;
            }
            OmaErrorMessageType omaErrorMessageType = OmaErrorMessageType.getOmaErrorFromValidationError(failureCode);
            String[] variables = generateRequestErrorVariables(failureCode, failedField, errorMessage);
            log.info("Failures: {}", String.join(",", variables));
            throw new ServerException(omaErrorMessageType, variables, Response.Status.BAD_REQUEST);
        }
    }
    private String[] generateRequestErrorVariables(ValidatorTypeCode failureCode, String failedField, ValidationMessage errorMessage) {
        String validationArgument;
        String validationArgument2;
        try {
            validationArgument = errorMessage.getArguments()[0];
        } catch (Exception ex) {
            validationArgument = "";
        }
        try {
            validationArgument2 = errorMessage.getArguments()[1];
        } catch (Exception ex) {
            validationArgument2 = "";
        }
        switch (failureCode) {
            case MIN_LENGTH:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("field too short; minimum length is %s characters", validationArgument)};
            case MAX_LENGTH:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("field too long; maximum length is %s characters", validationArgument)};
            case MINIMUM:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("value is lower than the system limit of %s", validationArgument)};
            case MAXIMUM:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("value is greater than the system limit of %s", validationArgument)};
            case MAX_ITEMS:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("number of elements exceeds the system limit of %s", validationArgument)};
            case MIN_ITEMS:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("number of elements is below the system limit of %s", validationArgument)};
            case TYPE:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("found datatype %s, but required datatype is %s", validationArgument, validationArgument2)};
            case REQUIRED:
            case ADDITIONAL_PROPERTIES:
                return new String[]{ATTRIBUTE, failedField};
            case PATTERN:
                return new String[]{ATTRIBUTE, failedField,
                        String.format("field does not match the pattern (%s)", validationArgument)};
            case ENUM:
                return new String[]{failedField};
            default:
                return new String[]{"unknown validation error", Response.Status.BAD_REQUEST.toString()};
        }
    }
}
