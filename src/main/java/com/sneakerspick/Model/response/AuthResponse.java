package com.sneakerspick.Model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthResponse {

	private Integer userId;
	private String username;
	private String token;

}
