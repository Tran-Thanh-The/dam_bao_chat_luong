package com.UserManagement.service;

import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.User;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;


public interface UserService {
		
	boolean saveUser(UserDto userDto);

    List<UserDto> findAllUsers();
    
//    accountType là all, đang hoạt động và không hoạt động
    Page<UserDto> findAllUsers(Pageable pageable, String accountType);

	User findUserByEmail(String email);

	UserDto findUserById(Long userId);

	boolean doesUserExist(Long userId);

	boolean editUser(UserDto updatedUserDto, Long userId);

	boolean deleteUserById(Long userId);
	
	boolean restoreUserById(Long userId);
	
	public Map<String, UserDto> toMap();

}
