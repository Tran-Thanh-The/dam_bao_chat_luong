package com.UserManagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.SubjectDto;


public interface SubjectService {
	String getSubjectNameById(String subjectId);
	SubjectDto getSubjectById(String subjectId);
	List<SubjectDto> getAllSubject();
	Page<SubjectDto> getAllSubject(Pageable pageable);
	boolean saveSubject(SubjectDto subjectDto); // Hàm này để cấu hình môn học
	
	// Cái này là lấy ra danh sách điểm từ môn học nào đó
	List<ScoreDto> getSubjectScore(String subjectId);
	List<ScoreDto> getSubjectScore(String subjectId, long year, String dept, String scoreLevel);
	Page<ScoreDto> getSubjectScore(String subjectId, Pageable pageable, long year, String dept, String scoreLevel);
}
