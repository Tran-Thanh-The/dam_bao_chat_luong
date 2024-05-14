package com.UserManagement.Controller;

import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.User;
import com.UserManagement.service.UserService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String home() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginForm() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			return "redirect:/users";
		}
		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		if (authentication != null
				&& authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			return "redirect:/users";
		} else {

			return "register";
		}
	}

	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto userDto,	BindingResult result, Model model) {

		User existingUser = userService.findUserByEmail(userDto.getEmail());

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null, "Email đã tồn tại, vui lòng chọn email khác");
		}

		if (!userDto.getPassword().isEmpty()) {
			if (userDto.getPassword().length() < 8) {
				result.rejectValue("password", "field.min.length", "Mật khẩu phải có tối thiểu 8 ký tự");
			}
		}else{
			result.rejectValue("password", "field.min.length", "Mật khẩu không được để trống.");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "register";
		}

		userService.saveUser(userDto);
		return "redirect:/register?success=true";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add/save")
	public String addUser(
			@Valid @ModelAttribute("user") UserDto userDto,
			BindingResult result,
			Model model) {
		User existingUser = userService.findUserByEmail(userDto.getEmail());

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null, "Email này đã tồn tại, vui lòng chọn email khác");
		}

		if (!userDto.getPassword().isEmpty()) {
			if (userDto.getPassword().length() < 8) {
				result.rejectValue("password", "field.min.length", "Mật khẩu phải có tối đa 8 ký tự");
			}
		}else{
			result.rejectValue("password", "field.min.length", "Mật khẩu không được để trống.");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "add";
		}

		userService.saveUser(userDto);
		return "redirect:/users?success=true";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/add")
	public String showUserAddForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "add";
	}

//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/users")
//	public String users(Model model) {
//		List<UserDto> users = userService.findAllUsers();
//		model.addAttribute("users", users);
//		return "user";
//	}
	
	// get user có phân trang
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public String users(Model model,
	                     @RequestParam(name = "page", defaultValue = "1") int page,
	                     @RequestParam(name = "size", defaultValue = "3") int size,
	                     @RequestParam(name = "accountType", defaultValue = "all") String accountType	                     
	                     ) {
	    // Số trang bắt đầu từ 0 trong PageRequest, nên trừ đi 1 từ tham số page
	    Page<UserDto> userPage = userService.findAllUsers(PageRequest.of(page - 1, size),accountType);
	    
	    // Danh sách người dùng của trang hiện tại
	    List<UserDto> users = userPage.getContent();
	    
	    // Tổng số trang
	    int totalPages = userPage.getTotalPages();
	    
	    // Tổng số người dùng
	    long totalUsers = userPage.getTotalElements();
	    
	    // Truyền danh sách người dùng và thông tin phân trang vào mô hình
	    model.addAttribute("users", users);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalUsers", totalUsers);
	    model.addAttribute("accountType", accountType);
	    
	    return "user";
	}
	
	// Chỉnh sửa user
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/edit/{id}")
	public String editUser(
			@PathVariable Long id,
			Model model) {
		UserDto user = userService.findUserById(id);
		model.addAttribute("user", user);
		return "edit";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/edit/{id}")
	public String updateUserById(
			@Valid @ModelAttribute("user") UserDto updatedUserDto,
			BindingResult result,
			@PathVariable Long id,
			Model model) {
		
		if (!updatedUserDto.getPassword().isEmpty()) {
			if (updatedUserDto.getPassword().length() < 8) {
				result.rejectValue("password", "field.min.length", "Mật khẩu phải có tối thiểu 8 ký tự");
			}
		}

		if (result.hasErrors()) {
			model.addAttribute("user", updatedUserDto);
			return "edit";
		}

		userService.editUser(updatedUserDto, id);
		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/delete/{id}")
	public String deleteUser(
			@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "success", required = false) String success,
			RedirectAttributes redirectAttributes,
			@PathVariable Long id,
			Principal principal,
			Model model) {

		String loggedInUsername = principal.getName();

		User loggedInUser = userService.findUserByEmail(loggedInUsername);

		if (loggedInUser != null && loggedInUser.getId().equals(id)) {
			if (error != null) {
				redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa bản thân.");
			}
		} else {
			if (userService.doesUserExist(id)) {
				userService.deleteUserById(id);
				if (success != null) {
					redirectAttributes.addFlashAttribute("success", "User đã được xóa thành công");
				}
			} else {
				if (error != null) {
					redirectAttributes.addFlashAttribute("error", "User không tồn tại");
				}
			}
		}
		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/restore/{id}")
	public String restoreUser(
			@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "success", required = false) String success,
			RedirectAttributes redirectAttributes,
			@PathVariable Long id,
			Model model) {


			if (userService.doesUserExist(id)) {
				userService.restoreUserById(id);
				if (success != null) {
					redirectAttributes.addFlashAttribute("success", "User đã được khôi phục thành công");
				}
			} else {
				if (error != null) {
					redirectAttributes.addFlashAttribute("error", "User không tồn tại");
				}
			}
		
		return "redirect:/users";
	}
}
