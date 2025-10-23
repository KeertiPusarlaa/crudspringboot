package com.bezkoder.spring.exception;

public record FieldValidationError(String field, String message) {
}
