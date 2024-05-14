package com.UserManagement.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.UserManagement.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    Page<User> findByIsActivateTrue(Pageable pageable);
    Page<User> findByIsActivateFalse(Pageable pageable);
}