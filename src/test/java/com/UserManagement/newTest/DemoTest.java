package com.UserManagement.newTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;

import com.UserManagement.Repository.RoleRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.UserService;
import com.UserManagement.service.Impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class DemoTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    
//    @Autowired
//    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Tạo và lưu một vài đối tượng User vào cơ sở dữ liệu
//        userRepository.save(new User("user1", "password1"));
    	System.out.println("============ start ===============");
    }

    @AfterEach
    public void tearDown() {
        // Xóa tất cả dữ liệu test sau mỗi test case
//        userRepository.deleteAll();
    	System.out.println("============ down ===============");
//    	System.out.println(userRepository.findAll().size());
    }

    @Test
    public void testFindAll() {
        // Kiểm tra số lượng đối tượng User đã được lưu trong cơ sở dữ liệu
//        assertEquals(2, userRepository.findAll().size());
    	UserServiceImpl ipl = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    	System.out.println("=========== test 1: Đây là test user service ================");
    	System.out.println(ipl.findAllUsers().size());
        System.out.println("=========== test 2 ================");
    }
}