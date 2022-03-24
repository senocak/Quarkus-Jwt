package com.github.senocak.util;

import com.networknt.schema.ValidatorTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OmaErrorMessageType {
    BASIC_INVALID_INPUT("SVC0001", "Invalid input value for message part %1"),
    GENERIC_SERVICE_ERROR("SVC0002", "The following service error occurred: %1. Error code is %2"),
    DETAILED_INVALID_INPUT("SVC0003", "Invalid input value for %1 %2: %3"),
    EXTRA_INPUT_NOT_ALLOWED("SVC0004", "Input %1 %2 not permitted in request"),
    MANDATORY_INPUT_MISSING("SVC0005", "Mandatory input %1 %2 is missing from request"),
    UNAUTHORIZED("SVC0006", "UnAuthorized Endpoint"),
    JSON_SCHEMA_VALIDATOR("SVC0007", "Schema failed."),
    NOT_FOUND("SVC0008", "Entry is not found");

    private final String messageId;
    private final String text;

    /**
     * @param failureCode -- a Schema code to generate the failure type
     * @return -- an Enum failure object
     */
    public static OmaErrorMessageType getOmaErrorFromValidationError(ValidatorTypeCode failureCode) {
        switch (failureCode) {
            case MIN_LENGTH:
            case MAX_LENGTH:
            case MINIMUM:
            case MAXIMUM:
            case MAX_ITEMS:
            case MIN_ITEMS:
            case TYPE:
            case PATTERN:
                return DETAILED_INVALID_INPUT;
            case ENUM:
                return BASIC_INVALID_INPUT;
            case REQUIRED:
                return MANDATORY_INPUT_MISSING;
            case ADDITIONAL_PROPERTIES:
                return EXTRA_INPUT_NOT_ALLOWED;
            default:
                return GENERIC_SERVICE_ERROR;
        }
    }
}

