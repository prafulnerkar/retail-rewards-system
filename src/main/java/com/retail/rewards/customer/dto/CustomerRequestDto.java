package com.retail.rewards.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Customer creation request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {

    /**
     * Customer first name.
     */
    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    /**
     * Customer last name.
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    /**
     * Customer email.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
}