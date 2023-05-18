package com.sneakerspick.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

	@NotBlank(message = "username field can't blank")
	private String username;

	@Size(min = 5, max = 100, message = "value must be between 5 and 100 characters")
	private String password;



}
