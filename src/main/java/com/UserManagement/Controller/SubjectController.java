package com.UserManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.SubjectDto;
import com.UserManagement.Entity.Subject;
import com.UserManagement.Repository.SubjectRepository;
import com.UserManagement.service.SubjectService;
import com.UserManagement.service.Impl.SubjectServiceImpl;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/subject")
public class SubjectController {
	@Autowired
	private SubjectService subjectService;

	public SubjectController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}
	@PreAuthorize("hasRole('ADMIN')")	
	@GetMapping("/get-subject-score/{subjectId}")
	public String getSubjectScore(Model model, 
								   @PathVariable("subjectId") String subjectId,
	                               @RequestParam(name = "page", defaultValue = "1") int page,
	                               @RequestParam(name = "size", defaultValue = "3") int size,
	                               @RequestParam(name = "ScoreLevel", defaultValue = "all") String scoreLevel,
	                               @RequestParam(name = "year", defaultValue = "all") String year,
	                               @RequestParam(name = "dept", defaultValue = "all") String dept) {
        long longYear = 0;
        if (!year.equals("all")) {
        	longYear = Long.parseLong(year);
        }
	    // Lấy dữ liệu từ service, trả về một trang phân trang
	    Page<ScoreDto> scorePage = subjectService.getSubjectScore(subjectId, PageRequest.of(page - 1, size), longYear, dept, scoreLevel);
	    
	    // Truyền các thông tin cần thiết vào mô hình
		String tenMonHoc = subjectService.getSubjectNameById(subjectId);
		model.addAttribute("subjectName", tenMonHoc);
	    model.addAttribute("scores", scorePage.getContent()); // Danh sách điểm cho trang hiện tại
	    model.addAttribute("totalPages", scorePage.getTotalPages()); // Tổng số trang
	    model.addAttribute("currentPage", page); // Trang hiện tại
	    model.addAttribute("subjectId", subjectId); // ID của môn học
	    
	    model.addAttribute("dept", dept);
	    model.addAttribute("scoreLevel", scoreLevel);
	    model.addAttribute("year", year);
	    return "score";
	}
	
	// Mở trang cấu hình tỷ lệ điểm, số tín chỉ,...
	@PreAuthorize("hasRole('ADMIN')")	
	@GetMapping("/config/{subjectId}")
	public String configSubject(Model model, @PathVariable("subjectId") String subjectId) {
		SubjectDto subjectDto = subjectService.getSubjectById(subjectId);
		if (subjectDto == null) {
			return "subject";
		}
		model.addAttribute("subject", subjectDto);
	    return "config";
	}
	
	// Đây là trang cấu hình tỷ lệ điểm
	@PreAuthorize("hasRole('ADMIN')")	
	@PostMapping("/config/{subjectId}")
	public String changeSubject(@Valid @ModelAttribute("subject") SubjectDto subjectDto, 
								BindingResult result, 
								Model model) {		
		
		if (subjectDto.getTenMonHoc().isEmpty() || subjectDto.getTenMonHoc().isBlank()) {
			result.rejectValue("tenMonHoc", "field.min.length", "Tên môn học không được để trống.");
		}
		boolean status = subjectService.saveSubject(subjectDto);
		
		if (result.hasErrors()) {
			model.addAttribute("subject", subjectDto);
			return "config";
		}
		model.addAttribute("subject", subjectDto);
		return "config";
		
		// Chỗ này mà trả về subject là lỗi vì không có phân trang, nếu muốn thì redirect lại và thêm phân trang vào
//		return "subject";		
	}
	
	// Cải tiến controller để phân trang danh sách các môn học
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all")
	public String getAllSubjects(Model model,
	                             @RequestParam(name = "page", defaultValue = "1") int page,
	                             @RequestParam(name = "size", defaultValue = "3") int size) {
	    // Sử dụng Pageable để phân trang
	    Page<SubjectDto> subjectPage = subjectService.getAllSubject(PageRequest.of(page - 1, size));
	    
	    // Danh sách các môn học của trang hiện tại
	    List<SubjectDto> subjects = subjectPage.getContent();
	    
	    // Tổng số trang
	    int totalPages = subjectPage.getTotalPages();
	    
	    // Tổng số môn học
	    long totalSubjects = subjectPage.getTotalElements();
	    
	    // Truyền danh sách môn học và thông tin phân trang vào mô hình
	    model.addAttribute("subjects", subjects);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalSubjects", totalSubjects);
	    
	    return "subject";
	}

}
