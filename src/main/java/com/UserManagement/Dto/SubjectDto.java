package com.UserManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {

	private String id;
	private String tenMonHoc;
	private long tyLediemChuyenCan;
	private long tyLediemKiemTraGiuaKy;
	private long tyLediemKiemTraCuoiKy;
	private long tyLediemBaiTapLon;
	private long soTinChi;
}
