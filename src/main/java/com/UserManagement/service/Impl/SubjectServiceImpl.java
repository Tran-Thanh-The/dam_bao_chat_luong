package com.UserManagement.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.UserManagement.Repository.ScoreRepository;
import com.UserManagement.Repository.SubjectRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.SubjectService;

import jakarta.persistence.EntityNotFoundException;

import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.SubjectDto;
import com.UserManagement.Entity.Score;
import com.UserManagement.Entity.Student;
import com.UserManagement.Entity.Subject;
import com.UserManagement.Entity.User;

@Service
public class SubjectServiceImpl implements SubjectService{

	private SubjectRepository subjectRepo;
	private UserRepository userRepo;
	private ScoreRepository scoreRepo;
	
	public SubjectServiceImpl(SubjectRepository subjectRepository, ScoreRepository scoreRepo, UserRepository userRepo) {
		this.subjectRepo = subjectRepository;
		this.scoreRepo = scoreRepo;
		this.userRepo = userRepo;
	}
	@Override
	public String getSubjectNameById(String subjectId) {
		Optional<Subject> subjectOption = this.subjectRepo.findById(subjectId);
		if (subjectOption.isPresent()) {
			Subject subject = subjectOption.get();
			return subject.getTenMonHoc();
		}
		return null;
	}
	
	@Override
	public SubjectDto getSubjectById(String subjectId) {
		Optional<Subject> subjectOption = this.subjectRepo.findById(subjectId);
		if (subjectOption.isPresent()) {
			Subject subject = subjectOption.get();
			SubjectDto subjectDto = convertToDto(subject);
			return subjectDto;
		}
		return null;
	}
	
	@Override
	public boolean saveSubject(SubjectDto subjectDto) {
		// Nếu đối tượng truyền vào null -> return false
		if (subjectDto == null) {
			return false;
		}
		// Nếu số tín chỉ < 1 -> return fale
		if (subjectDto.getSoTinChi() < 1) {
			return false;
		}
		// Nếu đối tượng không có id -> return false
		if (subjectDto.getId() == null) {
			return false;
		}
		if (subjectDto.getTyLediemChuyenCan() < 0 || subjectDto.getTyLediemKiemTraGiuaKy() < 0
				|| subjectDto.getTyLediemBaiTapLon() < 0 || subjectDto.getTyLediemKiemTraCuoiKy() < 0) {
			return false;
		}
		// Tổng tỷ lệ điểm
		long totalRate =   subjectDto.getTyLediemChuyenCan() + subjectDto.getTyLediemKiemTraGiuaKy()
							+ subjectDto.getTyLediemBaiTapLon()	+ subjectDto.getTyLediemKiemTraCuoiKy();
		if (totalRate != 100) {
			return false;
		}
		
		if (subjectDto.getTenMonHoc().isEmpty() || subjectDto.getTenMonHoc().isBlank()) {
			return false;
		}
			
		Subject existingSubject = subjectRepo.findById(subjectDto.getId()).orElseThrow(() -> new EntityNotFoundException("Môn học không tồn tại"));
		
		existingSubject.setTenMonHoc(subjectDto.getTenMonHoc());
		existingSubject.setTyLediemChuyenCan(subjectDto.getTyLediemChuyenCan());
		existingSubject.setTyLediemKiemTraGiuaKy(subjectDto.getTyLediemKiemTraGiuaKy());
		existingSubject.setTyLediemBaiTapLon(subjectDto.getTyLediemBaiTapLon());
		existingSubject.setTyLediemKiemTraCuoiKy(subjectDto.getTyLediemKiemTraCuoiKy());
		existingSubject.setSoTinChi(subjectDto.getSoTinChi());
		
		subjectRepo.save(existingSubject);
		return true;
	}

	// code cũ, dùng tỷ lệ ở bảng subject 
//	private double tinhDiemTrungBinh(Subject subject, Score score) {
//		double diemTrungBinh = subject.getTyLediemChuyenCan() * score.getDiemChuyenCan()
//								+ subject.getTyLediemBaiTapLon() * score.getDiemBaiTapLon()
//								+ subject.getTyLediemKiemTraGiuaKy() * score.getDiemKiemTraGiuaKy()
//								+ subject.getTyLediemKiemTraCuoiKy() * score.getDiemKiemTraCuoiKy();
//		return Math.round(diemTrungBinh * 10.0) / 1000.0;
// 	}
	
	private double tinhDiemTrungBinh(Subject subject, Score score) {
		double diemTrungBinh = score.getTyLediemChuyenCan() * score.getDiemChuyenCan()
								+ score.getTyLediemBaiTapLon() * score.getDiemBaiTapLon()
								+ score.getTyLediemKiemTraGiuaKy() * score.getDiemKiemTraGiuaKy()
								+ score.getTyLediemKiemTraCuoiKy() * score.getDiemKiemTraCuoiKy();
		return Math.round(diemTrungBinh * 10.0) / 1000.0;
 	}
	// Phương thức hỗ trợ đổi điểm số sang chữ
	private String doiDiemSoSangchu(double score) {
		if (score < 4) 	return "F";
		if (score >= 4 && score <= 4.9) 	return "D";
		if (score >= 5 && score <= 5.4) 	return "D+";
		if (score >= 5.5 && score <= 6.4) 	return "C";
		if (score >= 6.5 && score <= 6.9) 	return "C+";
		if (score >= 7 && score <= 7.9) 	return "B";
		if (score >= 8 && score <= 8.4) 	return "B+";
		if (score >= 8.5 && score <= 8.9) 	return "A";
		return "A+";
	}
	
	// Phương thức hỗ trợ đổi điểm số sang chữ
		private String doiDiemSoSangHe4(double score) {
			if (score < 4) 	return "F";
			if (score >= 4 && score <= 4.9) 	return "1.0";
			if (score >= 5 && score <= 5.4) 	return "1.5";
			if (score >= 5.5 && score <= 6.4) 	return "2.0";
			if (score >= 6.5 && score <= 6.9) 	return "2.5";
			if (score >= 7 && score <= 7.9) 	return "3.0";
			if (score >= 8 && score <= 8.4) 	return "3.5";
			if (score >= 8.5 && score <= 8.9) 	return "3.7";
			return "4";
		}

	private SubjectDto convertToDto(Subject subject) {
		SubjectDto sdto = new SubjectDto();
        sdto.setId(subject.getId());
        sdto.setTenMonHoc(subject.getTenMonHoc());;
        sdto.setTyLediemChuyenCan(subject.getTyLediemChuyenCan());;
        sdto.setTyLediemKiemTraGiuaKy(subject.getTyLediemKiemTraGiuaKy());
        sdto.setTyLediemKiemTraCuoiKy(subject.getTyLediemKiemTraCuoiKy());
        sdto.setTyLediemBaiTapLon(subject.getTyLediemBaiTapLon());
        sdto.setSoTinChi(subject.getSoTinChi());
        return sdto;
    }
	
	private ScoreDto convertToScoreDto(Score score, Subject subject) {
	    ScoreDto scoreDto = new ScoreDto();
	    Student student = score.getStudent();
	    scoreDto.setMaSinhVien(student.getMaSinhVien());
        scoreDto.setTenSinhVien(student.getTenSinhVien());
        scoreDto.setKhoa(student.getKhoa());
        scoreDto.setPhanKhoa(student.getPhanKhoa());
        scoreDto.setDiemChuyenCan(score.getDiemChuyenCan());
        scoreDto.setDiemKiemTraGiuaKy(score.getDiemKiemTraGiuaKy());
        scoreDto.setDiemKiemTraCuoiKy(score.getDiemKiemTraCuoiKy());
        scoreDto.setDiemBaiTapLon(score.getDiemBaiTapLon());
        
        double diemTB = this.tinhDiemTrungBinh(subject, score);
        scoreDto.setDiemTrungBinhHe4(this.doiDiemSoSangHe4(diemTB));
        scoreDto.setDiemTrungBinh(diemTB);
        scoreDto.setDiemDangChu(this.doiDiemSoSangchu(diemTB));
		
	    return scoreDto;
	}
	
	// Cái này lấy môn học không phân trang
//	@Override
	@Override
	public List<SubjectDto> getAllSubject() {
		List<Subject> subjects = subjectRepo.findAll();
		List<SubjectDto> subjectDtos = new ArrayList<>();
		for (Subject subject: subjects) {
			subjectDtos.add(convertToDto(subject));
		}
		return subjectDtos;
	}
	
	// cái này lấy môn học có phân trang
	@Override
	public Page<SubjectDto> getAllSubject(Pageable pageable) {
		 Page<Subject> subjectPage = subjectRepo.findAll(pageable);
	     return subjectPage.map(this::convertToDto);
	}

	
	@Override
	public List<ScoreDto> getSubjectScore(String subjectId) {
		 List<ScoreDto> scores = new ArrayList<>();
		 if (subjectId == null) {
			 return scores;
		 }
		 
		    Optional<Subject> subjectOptional = this.subjectRepo.findById(subjectId);
		    
		    if (subjectOptional.isPresent()) {
		        Subject subject = subjectOptional.get();
		        if (subject.getScores() != null && !subject.getScores().isEmpty()) {
		        	List<Score> scoreEntitys = subject.getScores();
		            for (Score score: scoreEntitys) {
		            	scores.add(this.convertToScoreDto(score, subject));
		            }
		            }
		    } else {
		        throw new RuntimeException("Không tìm thấy môn học với ID: " + subjectId);
		    }
		
		return scores;
	}
	// Cái này là phân trang
	@Override
	public Page<ScoreDto> getSubjectScore(String subjectId, Pageable pageable, long year, String dept, String scoreLevel) {
	    if (subjectId == null) {
	        return Page.empty(); // Trả về một trang trống nếu subjectId là null
	    }
	    
	    Optional<Subject> subjectOptional = subjectRepo.findById(subjectId);
	    
	    if (subjectOptional.isPresent()) {
	        Subject subject = subjectOptional.get();
	        List<ScoreDto> scores = new ArrayList<>();
	        if (subject.getScores() != null && !subject.getScores().isEmpty()) {
	            List<Score> scoreEntities = subject.getScores();
	            for (Score score : scoreEntities) {
	            	Student student = score.getStudent();
	            	double diemTB = this.tinhDiemTrungBinh(subject, score);
	            	// Đây là điểm dạng chữ: A, A+,...
	            	String diemChu = this.doiDiemSoSangchu(diemTB);
	            	// 0 0 0
	            	if (year == 0 && dept.equals("all") && scoreLevel.equals("all")) {
	            		 scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 0 1
	            	if (year == 0 && dept.equals("all") && scoreLevel.equals(diemChu)) {
	            		 scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 1 0
	            	else if (year == 0 && dept.equals(student.getPhanKhoa()) && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 1 1
	            	else if (year == 0 && dept.equals(student.getPhanKhoa()) && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 0 0
	            	else if (year == student.getKhoa() && dept.equals("all") && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}	
	            	// Nếu dept = all, và scoreLevel thì chỉ chọn những ai có năm học là year gửi lên
	            	else if (year == student.getKhoa() && dept.equals("all") && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 1 0
	            	else if (year == student.getKhoa() && dept.equals(student.getPhanKhoa()) && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 1 1
	            	else if (year == student.getKhoa() && dept.equals(student.getPhanKhoa()) && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            }
	        }
	     // Tính tổng số lượng dữ liệu
	        int totalScores = scores.size();
	        // Tạo một trang từ danh sách scores và Pageable
	        int start = Math.toIntExact(pageable.getOffset());
	        int end = Math.min((start + pageable.getPageSize()), totalScores);
	        return new PageImpl<>(scores.subList(start, end), pageable, totalScores);
	    } else {
	        throw new RuntimeException("Không tìm thấy môn học với ID: " + subjectId);
	    }
	}

	// Cái này phục vụ cho việc export file score
	@Override
	public List<ScoreDto> getSubjectScore(String subjectId, long year, String dept, String scoreLevel) {	    
	    Optional<Subject> subjectOptional = subjectRepo.findById(subjectId);
	    if (subjectOptional.isPresent()) {
	        Subject subject = subjectOptional.get();
	        List<ScoreDto> scores = new ArrayList<>();
	        if (subject.getScores() != null && !subject.getScores().isEmpty()) {
	            List<Score> scoreEntities = subject.getScores();
	            for (Score score : scoreEntities) {
	            	Student student = score.getStudent();
	            	double diemTB = this.tinhDiemTrungBinh(subject, score);
	            	// Đây là điểm dạng chữ: A, A+,...
	            	String diemChu = this.doiDiemSoSangchu(diemTB);
	            	// 0 0 0
	            	if (year == 0 && dept.equals("all") && scoreLevel.equals("all")) {
	            		 scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 0 1
	            	if (year == 0 && dept.equals("all") && scoreLevel.equals(diemChu)) {
	            		 scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 1 0
	            	else if (year == 0 && dept.equals(student.getPhanKhoa()) && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 0 1 1
	            	else if (year == 0 && dept.equals(student.getPhanKhoa()) && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 0 0
	            	else if (year == student.getKhoa() && dept.equals("all") && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}	
	            	// Nếu dept = all, và scoreLevel thì chỉ chọn những ai có năm học là year gửi lên
	            	else if (year == student.getKhoa() && dept.equals("all") && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 1 0
	            	else if (year == student.getKhoa() && dept.equals(student.getPhanKhoa()) && scoreLevel.equals("all")) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            	// 1 1 1
	            	else if (year == student.getKhoa() && dept.equals(student.getPhanKhoa()) && scoreLevel.equals(diemChu)) {
	            		scores.add(convertToScoreDto(score, subject));
	            	}
	            }
	        }
	        return scores;
	    } else {
	        throw new RuntimeException("Không tìm thấy môn học với ID: " + subjectId);
	    }
	}

}
