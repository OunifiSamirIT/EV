package com.ala.ala.exception.utils;

import com.ala.ala.exception.dto.ApiError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public final class ExceptionUtils {

    public static final String ACCESS_DENIED = "Access denied!";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String LIST_JOIN_DELIMITER = ",";



    private static String getMessageForStatus(HttpStatus status) {
        switch (status) {
            case UNAUTHORIZED:
                return ACCESS_DENIED;
            case BAD_REQUEST:
                return INVALID_REQUEST;
            default:
                return status.getReasonPhrase();
        }
    }

    public static String getErrorMessageForLogger(final List<String> errors, final HttpStatus status) {
        return !errors.isEmpty() ?
                errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(LIST_JOIN_DELIMITER))
                :status.getReasonPhrase();
    }


    public static ApiError getApiError(final Exception exception,
                                       final HttpStatus status,
                                       final String path,
                                       final List<String> errors) {
        ApiError apiError = new ApiError(status);
        apiError.setType(exception.getClass().getSimpleName());
        apiError.setPath(path);
        apiError.setMessage(getMessageForStatus(status));
        if (errors.isEmpty() && exception.getMessage() != null) {
            errors.add(exception.getMessage());
        }

        apiError.setErrors(errors);
        return apiError;
    }
}
