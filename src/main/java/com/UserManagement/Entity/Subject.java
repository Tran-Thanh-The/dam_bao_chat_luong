package com.UserManagement.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name="subject")
public class Subject {
	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
	
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
	
	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Score> scores;
}
