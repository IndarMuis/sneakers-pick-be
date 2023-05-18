package com.sneakerspick.handler;

import com.sneakerspick.dto.response.APIResponse;
import com.sneakerspick.dto.response.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHanlder extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception) {
		APIResponse<?> response = new APIResponse<>();
		List<ErrorDTO> errors = new ArrayList<>();
		exception.getBindingResult().getFieldErrors()
				.forEach(field -> {
					ErrorDTO error = new ErrorDTO();
					error.setField(field.getField());
					error.setErrorMessage(field.getDefaultMessage());
					errors.add(error);
				});
		response.setMessage("FAILED");
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setErrors(errors);
		return response;
	}
}
