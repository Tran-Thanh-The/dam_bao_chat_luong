package com.UserManagement.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreDto {
	private String maSinhVien;
	private String tenSinhVien;
	private Long khoa; // này là khóa,ví dụ 2020, 2021,...
	private String phanKhoa;
	
	private double diemChuyenCan;
	private double diemKiemTraGiuaKy;
	private double diemKiemTraCuoiKy;
	private double diemBaiTapLon;
	
	private double diemTrungBinh;
	private String diemTrungBinhHe4;
	private String diemDangChu; // Đây là điểm dạng chữ: A, A+,...
}
