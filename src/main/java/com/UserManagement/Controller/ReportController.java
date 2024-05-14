package com.UserManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.UserDto;
import com.UserManagement.service.SubjectService;
import com.UserManagement.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.UserManagement.service.Impl.ScoreExcelExporter;
import com.UserManagement.service.Impl.UserExcelExporter;

@Controller
@RequestMapping("/export")
public class ReportController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private SubjectService subjectService;
	
	public ReportController(UserService userService, SubjectService subjectService) {
		this.userService = userService;
		this.subjectService = subjectService;
	}
	@GetMapping("/score-report/{subjectId}")
	public void exportScoreReport(HttpServletResponse response,
									@PathVariable("subjectId") String subjectId,
									@RequestParam(name = "scoreLevel", defaultValue = "all") String scoreLevel,
									@RequestParam(name = "year", defaultValue = "all") String year,
									@RequestParam(name = "dept", defaultValue = "all") String dept) throws IOException{
		
		System.out.println(scoreLevel);
		response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=scores_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        long longYear = 0;
        if (!year.equals("all")) {
        	longYear = Long.parseLong(year);
        }        
        List<ScoreDto> scoreDtos = subjectService.getSubjectScore(subjectId, longYear, dept, scoreLevel);
        
        if (scoreDtos != null) {
        	ScoreExcelExporter excelExporter = new ScoreExcelExporter(scoreDtos);      
            excelExporter.export(response);   
        }           
	}
	
	@GetMapping("/user-report")
	public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
          
        List<UserDto> users = userService.findAllUsers();
        
        if (users != null) {
        	UserExcelExporter excelExporter = new UserExcelExporter(users);      
            excelExporter.export(response);   
        }     
    }  
}
