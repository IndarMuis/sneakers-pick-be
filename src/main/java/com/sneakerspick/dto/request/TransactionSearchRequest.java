package com.sneakerspick.dto.request;

import com.sneakerspick.enums.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSearchRequest {

    private Long id;

    private TransactionStatus status;

    @NotBlank
    private Integer size;

    @NotBlank
    private Integer page;

}
