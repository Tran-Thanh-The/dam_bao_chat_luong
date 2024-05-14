package com.UserManagement.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Long id;

    @NotBlank(message = "Họ và tên đệm không được để trống")
    @Size(min = 2, max = 50, message = "Họ và tên đệm có từ 2 đến 50 ký tự")
    private String firstName;

    @NotEmpty(message = "Tên không được để trống")
    private String lastName;

    @NotEmpty(message = "Email không được để trống")
    @Email
    private String email;

    @NotNull(message = "Mật khẩu không được để trống")
    private String password;

    @Positive(message = "Tuổi phải là một số nguyên")
    @Max(value = 200, message = "Tuổi phải bé hơn hoặc bằng 200")
    @Min(value = 18, message = "Tuổi phải lớn hơn hoặc bằng 18")
    private Integer age;

    @NotEmpty(message="Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại phải có đúng 10 chữ số")
    private String phone;

    @NotEmpty(message="Giới tính không được để trống")
    @Pattern(regexp = "^(Nam|Nữ)$", message = "Giới tính phải là Nam hoặc Nữ")
    private String gender;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 100, message = "Địa chỉ có từ 5 đến 100 ký tự")
    private String address;
    
    private Boolean isActivate;
	
	private String role;
}
