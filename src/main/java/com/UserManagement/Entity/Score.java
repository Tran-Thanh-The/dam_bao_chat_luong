package com.UserManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="score")
public class Score {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
//	@Column(nullable=false, unique=true)
//    private String name;
	
	@Column(nullable=false)
    private double diemChuyenCan;
	
	@Column(nullable=false)
    private double diemKiemTraGiuaKy;
	
	@Column(nullable=false)
    private double diemKiemTraCuoiKy;
	
	@Column(nullable=false)
    private double diemBaiTapLon;
	
	// Đây là thông tin của subject, thêm vào để sau này thay đổi thì thông tin cũ không bị thay đổi
	@Column(nullable=false)
    private String tenMonHoc;
	
	@Column(nullable=false)
    private long tyLediemChuyenCan;
	
	@Column(nullable=false)
    private long tyLediemKiemTraGiuaKy;
	
	@Column(nullable=false)
    private long tyLediemKiemTraCuoiKy;
	
	@Column(nullable=false)
    private long tyLediemBaiTapLon;
	
	@Column(nullable=false)
    private long soTinChi;
	// ==============
	
	@ManyToOne
    private Subject subject;
	
	@ManyToOne
    private Student student;
}
