package com.UserManagement.service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.Role;
import com.UserManagement.Entity.User;
import com.UserManagement.Repository.RoleRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Đổi lại, hàm savaUser sẽ phải trả về boole
    @Override
    public boolean saveUser(UserDto userDto) { 		
    	if (userDto == null) {
    		return false;
    	}
    	
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAge(userDto.getAge());
        user.setPhone(userDto.getPhone());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
        
        return true;
    }

    // Biến thành hàm boole
    public boolean deleteUserById(Long userId) {
    	 Optional<User> userOptional = userRepository.findById(userId);
    	    if (userOptional.isPresent()) {
    	        User user = userOptional.get();
    	        user.setActivate(false);
    	        userRepository.save(user);
    	        return true;
    	    }
    	    return false;
    }
    
 // Biến thành hàm boole
    public boolean restoreUserById(Long userId) {
    	 Optional<User> userOptional = userRepository.findById(userId);
    	    if (userOptional.isPresent()) {
    	        User user = userOptional.get();
    	        user.setActivate(true);
    	        userRepository.save(user);
    	        return true;
    	    }
    	    return false;
    }

    public boolean doesUserExist(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.isPresent();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto findUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            return mapToUserDto(userOptional.get());
        }
        return null;
    }

    public boolean editUser(UserDto updatedUserDto, Long userId) {
    	if (userId == null || updatedUserDto == null) {
    		return false;
    	}
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));
        existingUser.setName(updatedUserDto.getFirstName() + " " + updatedUserDto.getLastName());
        existingUser.setAge(updatedUserDto.getAge());
        existingUser.setPhone(updatedUserDto.getPhone());
        existingUser.setGender(updatedUserDto.getGender());
        existingUser.setAddress(updatedUserDto.getAddress());
        if (!updatedUserDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }
        userRepository.save(existingUser);
        return true;
    }


    // Cái này không có phân trang
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }
    
    // Cái này có phân trang
    @Override
    public Page<UserDto> findAllUsers(Pageable pageable, String accountType) {
    	 Page<User> userPage;
    	    if (accountType.equals("all")) {
    	        userPage = userRepository.findAll(pageable);
    	    } else if (accountType.equals("activate")) {
    	        userPage = userRepository.findByIsActivateTrue(pageable);
    	    } else {
    	        userPage = userRepository.findByIsActivateFalse(pageable);
    	    }
        
        // Sử dụng mapToUserDto để ánh xạ từ User sang UserDto
        Page<UserDto> userDtoPage = userPage.map(this::mapToUserDto);
        
        return userDtoPage;
    }

    // Hàm này để map từ user sang userDTo để hiển thị
    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        
        String fullName = user.getName().trim(); // Xóa bỏ khoảng trắng ở đầu và cuối
        int lastSpaceIndex = fullName.lastIndexOf(" ");

        if (lastSpaceIndex != -1) {
            String firstName = fullName.substring(0, lastSpaceIndex).trim();
            String lastName = fullName.substring(lastSpaceIndex + 1).trim();

            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
        } else {
            // Trường hợp không có khoảng trắng, coi tất cả là firstName và không có lastName
            userDto.setFirstName(fullName);
            userDto.setLastName(""); // hoặc có thể gán null tùy theo yêu cầu của bạn
        }
        
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setPhone(user.getPhone());
        userDto.setGender(user.getGender());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRoles().get(0).getName());
        userDto.setIsActivate(user.isActivate());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

	@Override
	public Map<String, UserDto> toMap() {
		List<User> users = userRepository.findAll();
		Map<String, UserDto> map = new HashMap<>();
//        map.put("name", this.name);
//        map.put("email", this.email);
        return map;
	}   
}