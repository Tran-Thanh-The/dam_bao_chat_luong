package com.UserManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.Role;
import com.UserManagement.Entity.User;
import com.UserManagement.Repository.RoleRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.Impl.UserServiceImpl;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import com.UserManagement.service.UserService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private  UserServiceImpl userService;

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
    public void saveUser_testNgoaiLe4() {
        // Dữ liệu người dùng với dữ liệu email sai định dạng
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
    }

    @Test
    public void saveUser_testNgoaiLe5() {
        String existEmail = "thetran@gmail.com";

        // Dữ liệu người dùng với dữ liệu email đã tồn tại trong hệ thống
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail(existEmail);
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
    public void saveUser_testNgoaiLe6() {
        // Dữ liệu người dùng với email trùng với email một người dùng đã bị xóa
        String deletedUserEmail = "phonvan130@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail(deletedUserEmail);
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
        // Dữ liệu mới hợp lệ, người dùng không tồn tại

    }

    @Test
    public void editUser_testChuan2() {
        // Dữ liệu mới hợp lệ, người dùng tồn tại
    }

    @Test
    public void editUser_testChuan3() {
        // Không có dữ liệu mới, người dùng không tồn tại
    }

    @Test
    public void editUser_testChuan4() {
        // Dữ liệu mới với một trường bằng rỗng, người dùng tồn tại
    }

    @Test
    public void editUser_testChuan5() {
        // Dữ liệu mới với trường email định dạng không hợp lệ, người dùng tồn tại
    }

    @Test
    public void editUser_testChuan6() {
        // Dữ liệu mới với trường email thành một email đã tồn tại trong hệ thống, người dùng tồn tại
    }

    @Test
    public void editUser_testChuan7() {
        // Dữ liệu mới với trường mật khẩu thành một chuỗi có độ dài nhỏ hơn 8 ký tự, người dùng tồn tại
    }

    @Test
    public void editUser_testChuan8() {
        // Dữ liệu mới hợp lệ, người dùng đã bị xóa
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

        boolean result = userService.doesUserExist(userId);

        assertFalse(result);
    }

    @Test
    public void doesUserExist_testNgoaiLe3() {
        // userId là một số âm
        Long userId = Long.valueOf(-555);

        boolean result = userService.restoreUserById(userId);

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

        boolean result = userService.restoreUserById(userId);

        assertFalse(result);
    }

    @Test
    public void restoreUserById_testNgoaiLe3() {
        // userId là null
        Long userId = null;

        boolean result = userService.restoreUserById(userId);

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
