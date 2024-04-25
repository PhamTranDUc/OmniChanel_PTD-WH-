package com.example.omnichannelfinal.dto;

import com.example.omnichannelfinal.entity.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserDto {
    private Long id;


    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role_id")
    private Long roleId;

    @JsonProperty("full_name")
    private String fullName;


}