package com.UserManagement.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.UserManagement.Entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String>{
	Page<Subject> findAll(Pageable pageable);
}
