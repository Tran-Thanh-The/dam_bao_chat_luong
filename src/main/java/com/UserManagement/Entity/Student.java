package com.UserManagement.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student")
public class Student {
	@Id
	private String maSinhVien;
	
	@Column(nullable=false)
    private String tenSinhVien;
	
	// khóa học 2022...
	@Column(nullable=false)
    private long khoa;
	
	// Cái này là phân khoa, tức là khoa CNTT, ATTT,...
	@Column(nullable=false)
	private String phanKhoa;
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Score> scores;
}
