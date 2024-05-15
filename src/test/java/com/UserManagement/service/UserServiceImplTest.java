package com.UserManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.sun.jdi.event.ExceptionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.User;
import com.UserManagement.Repository.RoleRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.Impl.UserServiceImpl;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserServiceImplTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleRepository mockRoleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private  UserServiceImpl userService;

    UserDto mockUserDto;

    private UserDto mockUser(
            String firstName,
            String lastName,
            String email,
            String password,
            int age,
            String phoneNumber,
            String gender,
            String address,
            String role
    ) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setAge(age);
        userDto.setPhone(phoneNumber);
        userDto.setGender(gender);
        userDto.setAddress(address);
        userDto.setRole(role);

        return userDto;
    }


    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
        mockUserDto = new UserDto();
        mockUserDto.setFirstName("John");
        mockUserDto.setLastName("Doe");
        mockUserDto.setEmail("john.doe123@gmail.com");
        mockUserDto.setPassword("password");
        mockUserDto.setAge(30);
        mockUserDto.setPhone("123456789");
        mockUserDto.setGender("Male");
        mockUserDto.setAddress("123 Street, City");
        mockUserDto.setRole("ROLE_ADMIN");
    }

    // **************************** TEST saveUser() ************************************ //

    @Test
    public void saveUser_testChuan1() {
        // Thêm người dùng với dữ liệu đầy đủ và hợp lệ
        UserDto userDto = mockUser(
                "John",
                "Doe",
                "john.doe123@gmail.com",
                "password",
                30,
                "123456789",
                "Male",
                "123 Street, City",
                "ROLE_ADMIN"
        );

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        Boolean result = userService.saveUser(userDto);

        assertEquals(true, result);

        User justSavedUser = userService.findUserByEmail(userDto.getEmail());
        System.out.print("Id: ");
        System.out.println(justSavedUser.getId());
        assertNotNull(justSavedUser);
        assertEquals(userDto.getEmail(), justSavedUser.getEmail());
        assertEquals(userDto.getAddress(), justSavedUser.getAddress());
        assertEquals(userDto.getPhone(), justSavedUser.getPhone());
        assertEquals(userDto.getGender(), justSavedUser.getGender());
        assertEquals(userDto.getAge(), justSavedUser.getAge());
    }

    @Test
    public void saveUser_testNgoaiLe1() {
        // Dữ liệu người dùng với tất cả các trường trống
        UserDto userDto = new UserDto();
        boolean result = userService.saveUser(null);
        assertFalse(result);
    }

    @Test
    public void saveUser_testNgoaiLe2() {
        // Dữ liệu người dùng với trường mật khẩu trống
        UserDto userDto = new UserDto();

        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe123@gmail.com");
        userDto.setPassword(null);
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole("ROLE_ADMIN");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
        User justSavedUser = userService.findUserByEmail(userDto.getEmail());
        assertNull(justSavedUser);
    }

    @Test
    public void saveUser_testNgoaiLe3() {
        // Dữ liệu người dùng với trường role trống
        UserDto userDto = new UserDto();

        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe123@gmail.com");
        userDto.setPassword("password");
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole(null);

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
        User justSavedUser = userService.findUserByEmail(userDto.getEmail());
        assertNull(justSavedUser);
    }

    @Test
    public void saveUser_testNgoaiLe4() {
        // Dữ liệu người dùng với trường phoneNumber trống
        try {
            UserDto userDto = new UserDto();

            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe123@gmail.com");
            userDto.setPassword("password");
            userDto.setAge(30);
            userDto.setPhone(null);
            userDto.setGender("Male");
            userDto.setAddress("123 Street, City");
            userDto.setRole("ROLE_ADMIN");

            when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
            Boolean result = userService.saveUser(userDto);
            assertEquals(false, result);
            User justSavedUser = userService.findUserByEmail(userDto.getEmail());
            assertNull(justSavedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveUser_testNgoaiLe5() {
        // Dữ liệu người dùng với trường address trống
        try {
            UserDto userDto = new UserDto();

            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe123@gmail.com");
            userDto.setPassword("password");
            userDto.setAge(30);
            userDto.setPhone("123456789");
            userDto.setGender("Male");
            userDto.setAddress(null);
            userDto.setRole("ROLE_ADMIN");

            when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
            Boolean result = userService.saveUser(userDto);
            assertEquals(false, result);
            User justSavedUser = userService.findUserByEmail(userDto.getEmail());
            assertNull(justSavedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveUser_testNgoaiLe6() {
        // Dữ liệu người dùng với trường age trống
        try {
            UserDto userDto = new UserDto();

            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe123@gmail.com");
            userDto.setPassword("password");
            userDto.setAge(null);
            userDto.setPhone("123456789");
            userDto.setGender("Male");
            userDto.setAddress("123 Street, City");
            userDto.setRole("ROLE_ADMIN");

            when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
            Boolean result = userService.saveUser(userDto);
            assertEquals(false, result);
            User justSavedUser = userService.findUserByEmail(userDto.getEmail());
            assertNull(justSavedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveUser_testNgoaiLe7() {
        // Dữ liệu người dùng với trường firstName và lastName trống
        UserDto userDto = new UserDto();

        userDto.setFirstName(null);
        userDto.setLastName(null);
        userDto.setEmail("john.doe123@gmail.com");
        userDto.setPassword("password");
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole("ROLE_ADMIN");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
        User justSavedUser = userService.findUserByEmail(userDto.getEmail());
        assertNull(justSavedUser);
    }

    @Test
    public void saveUser_testNgoaiLe8() {
        // Dữ liệu người dùng với trường gender trống
        try {
            UserDto userDto = new UserDto();

            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe123@gmail.com");
            userDto.setPassword("password");
            userDto.setAge(30);
            userDto.setPhone("123456789");
            userDto.setGender(null);
            userDto.setAddress("123 Street, City");
            userDto.setRole("ROLE_ADMIN");

            when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
            Boolean result = userService.saveUser(userDto);
            assertEquals(false, result);
            User justSavedUser = userService.findUserByEmail(userDto.getEmail());
            assertNull(justSavedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveUser_testNgoaiLe9() {
        // Dữ liệu người dùng với dữ liệu mật khẩu nhỏ hơn 8 ký tự
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe123@gmail.com");
        userDto.setPassword("123");
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole("ROLE_ADMIN");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("123");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
        User justSavedUser = userService.findUserByEmail(userDto.getEmail());
        assertNull(justSavedUser);
    }

    @Test
    public void saveUser_testNgoaiLe10() {
        // Dữ liệu người dùng với dữ liệu email sai định dạng
        try {
            UserDto userDto = new UserDto();
            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe1om");
            userDto.setPassword(null);
            userDto.setAge(30);
            userDto.setPhone("123456789");
            userDto.setGender("Male");
            userDto.setAddress("123 Street, City");
            userDto.setRole("ROLE_ADMIN");

            when(passwordEncoder.encode(userDto.getPassword())).thenReturn("");
            Boolean result = userService.saveUser(userDto);
            assertEquals(false, result);
            User justSavedUser = userService.findUserByEmail(userDto.getEmail());
            assertNull(justSavedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveUser_testNgoaiLe11() {
        // Dữ liệu người dùng với dữ liệu email đã tồn tại trong hệ thống
        String existEmail = "thetran@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail(existEmail);
        userDto.setPassword("password");
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole("ROLE_ADMIN");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
    }

    @Test
    public void saveUser_testNgoaiLe12() {
        // Dữ liệu người dùng với email trùng với email một người dùng đã bị xóa
        String deletedUserEmail = "phonvan130@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail(deletedUserEmail);
        userDto.setPassword("password");
        userDto.setAge(30);
        userDto.setPhone("123456789");
        userDto.setGender("Male");
        userDto.setAddress("123 Street, City");
        userDto.setRole("ROLE_ADMIN");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        Boolean result = userService.saveUser(userDto);
        assertEquals(false, result);
    }

    // **************************** TEST deleteUserById() ************************************ //

    @Test
    public void deleteUserById_testChuan1() {
        // Xóa người dùng tồn tại trong hệ thống
        Long existUserId = Long.valueOf(5);

        Boolean result = userService.deleteUserById(existUserId);

        assertEquals(false, result);
        UserDto justSavedUser = userService.findUserById(existUserId);
        assertNull(justSavedUser);
    }

    @Test
    public void deleteUserById_testNgoaiLe1() {
        // Xóa người dùng chưa tồn tại trong hệ thống
        Long existUserId = Long.valueOf(10000);

        Boolean result = userService.deleteUserById(existUserId);

        assertEquals(false, result);
        UserDto justSavedUser = userService.findUserById(existUserId);
        assertNull(justSavedUser);
    }

    @Test
    public void deleteUserById_testNgoaiLe2() {
        // Xóa người dùng đã bị xóa khỏi hệ thống
        Long existUserId = Long.valueOf(6);

        Boolean result = userService.deleteUserById(existUserId);

        assertEquals(false, result);
        UserDto justSavedUser = userService.findUserById(existUserId);
        assertNull(justSavedUser);
    }

    @Test
    public void deleteUserById_testNgoaiLe3() {
        // userId là null
        Long existUserId = null;

        Boolean result = userService.deleteUserById(existUserId);

        assertEquals(false, result);
    }

    @Test
    public void deleteUserById_testNgoaiLe4() {
        // userId là một số âm
        Long existUserId = Long.valueOf(-5);

        Boolean result = userService.deleteUserById(existUserId);

        assertEquals(false, result);
    }


    @Test
    public void deleteUserById_testNgoaiLe5() {
        // Tự xóa chính mình
    }

    // **************************** TEST editUser() ************************************ //

    @Test
    public void editUser_testChuan1() {
        // userId hợp lệ, dữ liệu mới hợp lệ, người dùng tồn tại
        Long userId = Long.valueOf(4);
//        mockUserDto.setId(userId);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(Long.valueOf(4));
        assertEquals(true, result);
        assertEquals(mockUserDto.getId(), user.getId());
        assertEquals(mockUserDto.getPhone(), user.getPhone());
        assertEquals(mockUserDto.getEmail(), user.getEmail());
        assertEquals(mockUserDto.getAddress(), user.getAddress());
        assertEquals(mockUserDto.getGender(), user.getGender());
        assertEquals(mockUserDto.getRole(), user.getRole());
    }

    @Test
    public void editUser_testChuan2() {
        // userId hợp lệ, dữ liệu mới hợp lệ, người dùng đã bị xóa
        Long userId = Long.valueOf(6);
//        mockUserDto.setId(userId);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(true, result);
        assertEquals(mockUserDto.getId(), user.getId());
        assertEquals(mockUserDto.getPhone(), user.getPhone());
        assertEquals(mockUserDto.getEmail(), user.getEmail());
        assertEquals(mockUserDto.getAddress(), user.getAddress());
        assertEquals(mockUserDto.getGender(), user.getGender());
        assertEquals(mockUserDto.getRole(), user.getRole());
    }

    @Test
    public void editUser_testNgoaiLe1() {
        // userId hợp lệ, dữ liệu mới hợp lệ, người dùng không tồn tại
        Long userId = Long.valueOf(99999);
        boolean result = userService.editUser(mockUserDto, userId);
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe2() {
        // userId hợp lệ, không có dữ liệu mới, người dùng tồn tại
        Long userId = Long.valueOf(4);
        boolean result = userService.editUser(null, userId);
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe3() {
        // userId hợp lệ, không có dữ liệu mới, người dùng không tồn tại
        Long userId = Long.valueOf(999999);
        boolean result = userService.editUser(null, userId);
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe4() {
        // userId hợp lệ, không có dữ liệu mới, người dùng đã bị xóa
        Long deletedUserId = Long.valueOf(6);
        boolean result = userService.editUser(null, deletedUserId);
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe5() {
        // userId hợp lệ, dữ liệu mới chứa 1 trường bằng null, người dùng tồn tại
        Long userId = Long.valueOf(5);
        mockUserDto.setPhone(null);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotNull(user.getPhone());
    }

    @Test
    public void editUser_testNgoaiLe6() {
        // userId hợp lệ, dữ liệu mới chứa 1 trường bằng null, người dùng không tồn tại
        Long userId = Long.valueOf(99999);
        mockUserDto.setPhone(null);
        boolean result = userService.editUser(mockUserDto, userId);
        assertEquals(false, result);
    }


    @Test
    public void editUser_testNgoaiLe7() {
        // userId hợp lệ, dữ liệu mới chứa 1 trường bằng null, người dùng đã bị xóa
        Long userId = Long.valueOf(6);
        mockUserDto.setPhone(null);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotNull(user.getPhone());
    }

    @Test
    public void editUser_testNgoaiLe8() {
        // userId hợp lệ, dữ liệu mới chứa email sai định dạng, người dùng tồn tại
        Long userId = Long.valueOf(5);
        String invalidEmail = "12345678990";
        mockUserDto.setEmail(invalidEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(invalidEmail, user.getEmail());
    }


    @Test
    public void editUser_testNgoaiLe9() {
        // userId hợp lệ, dữ liệu mới chứa email sai định dạng, người dùng không tồn tại
        Long userId = Long.valueOf(999999);
        String invalidEmail = "12345678990";
        mockUserDto.setEmail(invalidEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        assertEquals(false, result);
    }


    @Test
    public void editUser_testNgoaiLe10() {
        // userId hợp lệ, dữ liệu mới chứa email sai định dạng, người dùng đã bị xóa
        Long userId = Long.valueOf(6);
        String invalidEmail = "12345678990";
        mockUserDto.setEmail(invalidEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(invalidEmail, user.getEmail());
    }


    @Test
    public void editUser_testNgoaiLe11() {
        // userId hợp lệ, dữ liệu mới chứa mật khẩu có giá trị nhỏ hơn 8 ký tự, người dùng tồn tại
        Long userId = Long.valueOf(5);
        String invalidPassword = "123";
        mockUserDto.setPassword(invalidPassword);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(invalidPassword, user.getPassword());
    }


    @Test
    public void editUser_testNgoaiLe12() {
        // userId hợp lệ, dữ liệu mới chứa mật khẩu có giá trị nhỏ hơn 8 ký tự, người dùng không tồn tại
        Long userId = Long.valueOf(999999);
        String invalidPassword = "123";
        mockUserDto.setPassword(invalidPassword);
        boolean result = userService.editUser(mockUserDto, userId);
        assertEquals(false, result);
    }


    @Test
    public void editUser_testNgoaiLe13() {
        // userId hợp lệ, dữ liệu mới  chứa mật khẩu có giá trị nhỏ hơn 8 ký tự, người dùng đã bị xóa
        Long userId = Long.valueOf(6);
        String invalidPassword = "123";
        mockUserDto.setPassword(invalidPassword);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(invalidPassword, user.getPassword());
    }

    @Test
    public void editUser_testNgoaiLe14() {
        // userId hợp lệ, dữ liệu mới chứa email trùng với người dùng khác trong hệ thống, người dùng tồn tại
        Long userId = Long.valueOf(5);
        String sameEmail = "123456@gmail.com";
        mockUserDto.setEmail(sameEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(sameEmail, user.getEmail());
    }

    @Test
    public void editUser_testNgoaiLe15() {
        // userId hợp lệ, dữ liệu mới chứa email trùng với người dùng khác trong hệ thống, người dùng không tồn tại
        Long userId = Long.valueOf(99999);
        String sameEmail = "123456@gmail.com";
        mockUserDto.setEmail(sameEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe16() {
        // userId hợp lệ, chứa email trùng với người dùng khác trong hệ thống, người dùng đã bị xóa
        Long userId = Long.valueOf(6);
        String sameEmail = "123456@gmail.com";
        mockUserDto.setEmail(sameEmail);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
        assertNotEquals(sameEmail, user.getEmail());
    }

    @Test
    public void editUser_testNgoaiLe17() {
        // userId là null
        boolean result = true;
        try {

            Long userId = null;
            result = userService.editUser(mockUserDto, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(false, result);
    }

    @Test
    public void editUser_testNgoaiLe18() {
        // userId là một số âm
        Long userId = Long.valueOf(-999);
        boolean result = userService.editUser(mockUserDto, userId);
        UserDto user = userService.findUserById(userId);
        assertEquals(false, result);
    }


    // **************************** TEST findUserByEmail() ************************************ //

    @Test
    public void findUserByEmail_testChuan1() {
        // Email hợp lệ, người dùng tồn tại
        String email = "thetran@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void findUserByEmail_testChuan2() {
        // Email hợp lệ, người dùng đã bị xóa
        String email = "phonvan130@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void findUserByEmail_testNgoaiLe1() {
        // Email rỗng
        String email = "";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    @Test
    public void findUserByEmail_testNgoaiLe2() {
        // Email không hợp lệ
        String email = "xinchao.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    @Test
    public void findUserByEmail_testNgoaiLe3() {
        // Email hợp lệ, người dùng không tồn tại
        String email = "xinchao@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    // **************************** TEST findUserById() ************************************ //

    @Test
    public void findUserById_testChuan1() {
        // Email hợp lệ, người dùng đã bị xóa
        String email = "phonvan130@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    @Test
    public void findUserById_testNgoaiLe1() {
        // Email hợp lệ, người dùng đã bị xóa
        String email = "phonvan130@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    @Test
    public void findUserById_testNgoaiLe2() {
        // Email hợp lệ, người dùng đã bị xóa
        String email = "phonvan130@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    @Test
    public void findUserById_testNgoaiLe3() {
        // Email hợp lệ, người dùng đã bị xóa
        String email = "phonvan130@gmail.com";

        User user = userService.findUserByEmail(email);
        assertNull(user);
    }

    // **************************** TEST findAllUsers() ************************************ //

    @Test
    public void findAllUsers_testChuan1() {
        // Không có dữ liệu phân trang và accountType,  có 2 người dùng “Active” và 3 người dùng “Deactive”
        List<UserDto> result = userService.findAllUsers();
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    public void findAllUsers_testChuan2() {
        // Không có dữ liệu phân trang và accountType, không có dữ liệu người dùng
        userService = new UserServiceImpl(mockUserRepository, roleRepository, passwordEncoder);
        List<User> data =  new ArrayList<>();
        when(mockUserRepository.findAll()).thenReturn(data);
        List<UserDto> result = userService.findAllUsers();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void findAllUsers_testChuan3() {
        // Có dữ liệu phân trang với page index là 1 và size là 3, accountType hợp lệ,  có 2 người dùng “Active” và 3 người dùng “Deactive”

        Page<UserDto> result = userService.findAllUsers(PageRequest.of(0, 3), "all");
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getSize());
    }

    @Test
    public void findAllUsers_testChuan4() {
        // Có dữ liệu phân trang với page index là 0 và size là 3, accountType hợp lệ, không dữ liệu người dùng
        Page<UserDto> result = null;
        try {
            userService = new UserServiceImpl(mockUserRepository, roleRepository, passwordEncoder);
            List<User> data =  new ArrayList<>();
            when(mockUserRepository.findAll()).thenReturn(data);
            result = userService.findAllUsers(PageRequest.of(0, 3), "all");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(3, result.getSize());
    }

    @Test
    public void findAllUsers_testChuan5() {
        // Có dữ liệu phân trang với page index là 0 và size là 3, accountType là “ALL”, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = userService.findAllUsers(PageRequest.of(0, 3), "all");
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getSize());
    }

    @Test
    public void findAllUsers_testChuan6() {
        // Có dữ liệu phân trang với page index là 0 và size là 3, accountType là “Active”, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = userService.findAllUsers(PageRequest.of(0, 3), "active");
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(3, result.getSize());
        for (int i = 0; i < result.getContent().size(); i++) {
            assertEquals(true, result.getContent().get(i).getIsActivate());
        }
    }

    @Test
    public void findAllUsers_testChuan7() {
        // Có dữ liệu phân trang với page index là 0 và size là 3, accountType là “Deactive”, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = userService.findAllUsers(PageRequest.of(0, 3), "de-active");
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getSize());
        for (int i = 0; i < result.getContent().size(); i++) {
            assertEquals(false, result.getContent().get(i).getIsActivate());
        }
    }

    @Test
    public void findAllUsers_testChuan8() {
        // Có dữ liệu phân trang với page index là 0 và size là 3, accountType là null, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = null;
        try {
            result = userService.findAllUsers(PageRequest.of(0, 3), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(3, result.getSize());
    }

    @Test
    public void findAllUsers_testChuan9() {
        // Có dữ liệu phân trang với page index là 2 và size là 5,  accountType là “ALL”, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = null;

        try {
            userService = new UserServiceImpl(mockUserRepository, roleRepository, passwordEncoder);
            List<User> data =  new ArrayList<>();
            when(mockUserRepository.findAll()).thenReturn(data);
            result = userService.findAllUsers(PageRequest.of(2, 5), "all");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(5, result.getSize());
    }

    @Test
    public void findAllUsers_testChuan10() {
        // Có dữ liệu phân trang với page index là -1 và size là 3, contactType hợp lệ, có 2 người dùng “Active” và 3 người dùng “Deactive”
        Page<UserDto> result = null;

        try {
            result = userService.findAllUsers(PageRequest.of(-1, 3), "all");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(3, result.getSize());
    }

    // **************************** TEST doesUserExist() ************************************ //

    @Test
    public void doesUserExist_testChuan1() {
        // userId hợp lệ, người dùng tồn tại
        Long userId = Long.valueOf(5);

        boolean result = userService.doesUserExist(userId);

        assertTrue(result);
    }

    @Test
    public void doesUserExist_testChuan2() {
        // userId hợp lệ, người dùng đã bị xóa
        Long userId = Long.valueOf(5);

        boolean result = userService.doesUserExist(userId);

        assertTrue(result);
    }

    @Test
    public void doesUserExist_testNgoaiLe1() {
        // userId hợp lệ, người dùng không tồn tại
        Long userId = Long.valueOf(999);

        boolean result = userService.doesUserExist(userId);

        assertFalse(result);
    }

    @Test
    public void doesUserExist_testNgoaiLe2() {
        // userId là null
        Long userId = null;
        boolean result = true;
        try {
            result = userService.doesUserExist(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(result);
    }

    @Test
    public void doesUserExist_testNgoaiLe3() {
        // userId là một số âm
        Long userId = Long.valueOf(-555);
        boolean result = true;
        try {
            result = userService.restoreUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(result);
    }

    // **************************** TEST restoreUserById() ************************************ //

    @Test
    public void restoreUserById_testChuan1() {
        // userId hợp lệ, người dùng đã bị xóa
        Long userId = Long.valueOf(6);

        boolean result = userService.restoreUserById(userId);

        assertTrue(result);
    }

    @Test
    public void restoreUserById_testNgoaiLe1() {
        // userId hợp lệ,  người dùng tồn tại và chưa bị xóa
        Long userId = Long.valueOf(5);

        boolean result = userService.restoreUserById(userId);

        assertFalse(result);
    }

    @Test
    public void restoreUserById_testNgoaiLe2() {
        // userId hợp lệ, người dùng không tồn tại trong hệ thống
        Long userId = Long.valueOf(999);
        boolean result = true;
        try {
            result = userService.restoreUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(result);
    }

    @Test
    public void restoreUserById_testNgoaiLe3() {
        // userId là null
        Long userId = null;
        boolean result = true;

        try {
            result = userService.restoreUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(result);
    }

    @Test
    public void restoreUserById_testNgoaiLe4() {
        // userId là một số âm
        Long userId = Long.valueOf(-555);

        boolean result = userService.restoreUserById(userId);

        assertFalse(result);
    }
}
