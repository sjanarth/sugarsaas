package com.sugarsaas.api.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignupParams
{
    private String firstName;

    private String lastName;

    private String accountName;

    private String email;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String passWord;
}
