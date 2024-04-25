package com.example.omnichannelfinal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PasswordUpdateDto {
    @JsonProperty("password")
    private String password;
}
