package com.UserManagement.service;


import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.SubjectDto;
import com.UserManagement.Dto.UserDto;
import com.UserManagement.Entity.Subject;
import com.UserManagement.Entity.User;
import com.UserManagement.Repository.ScoreRepository;
import com.UserManagement.Repository.SubjectRepository;
import com.UserManagement.Repository.UserRepository;
import com.UserManagement.service.Impl.SubjectServiceImpl;
import com.UserManagement.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class SubjectServiceImplTest {
    @Mock
    private SubjectRepository mockSubjectRepository;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ScoreRepository scoreRepo;

    @Autowired
    private SubjectRepository subjectRepository;

    private SubjectServiceImpl subjectService;
    private SubjectDto subjectData;

    @BeforeEach
    public void setup() {
        subjectService = new SubjectServiceImpl(subjectRepository, scoreRepo, userRepo);
        subjectData = subjectService.getSubjectById("1");
    }


    // **************************** TEST getAllSubject() ************************************ //

    @Test
    public void getAllSubject_testChuan1() {
        // Không có môn học nào trong csdl
        subjectService = new SubjectServiceImpl(mockSubjectRepository, scoreRepo, userRepo);
        List<Subject> mockData = new ArrayList<>();
        when(mockSubjectRepository.findAll()).thenReturn(mockData);
        List<SubjectDto> result = subjectService.getAllSubject();

        assertNotNull(result);
        assertEquals(0, result.size());
    }


    @Test
    public void getAllSubject_testChuan2() {
        // Có 1 môn học trong csdl
        subjectService = new SubjectServiceImpl(mockSubjectRepository, scoreRepo, userRepo);
        Subject subject = new Subject();
        subject.setId("fake");
        subject.setTyLediemChuyenCan(10);
        subject.setTyLediemBaiTapLon(10);
        subject.setTyLediemKiemTraCuoiKy(70);
        subject.setTyLediemKiemTraGiuaKy(0);
        subject.setSoTinChi(3);

        List<Subject> mockData = new ArrayList<>();
        mockData.add(subject);
        when(mockSubjectRepository.findAll()).thenReturn(mockData);
        List<SubjectDto> result = subjectService.getAllSubject();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void getAllSubject_testChuan3() {
        // Có nhiều môn học trong csdl
        List<SubjectDto> result = subjectService.getAllSubject();

        assertNotNull(result);
        assertEquals(true, result.size() > 1);
    }

    @Test
    public void getAllSubject_testNgoaiLe1() {
        // Có các môn học chứa các giá trị null
        subjectService = new SubjectServiceImpl(mockSubjectRepository, scoreRepo, userRepo);
        Subject subject = new Subject();
        List<SubjectDto> result;
        try {
            List<Subject> mockData = new ArrayList<>();
            mockData.add(subject);
            when(mockSubjectRepository.findAll()).thenReturn(mockData);
            result = subjectService.getAllSubject();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        assertNull(result);
    }

    // **************************** TEST getSubjectScore() ************************************ //

    @Test
    public void getSubjectScore_testChuan1() {
        // Môn học không có điểm nào
        String subjectId = "3";

        List<ScoreDto> result = subjectService.getSubjectScore(subjectId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void getSubjectScore_testChuan2() {
        // Môn học có 1 điểm
        String subjectId = "2";

        List<ScoreDto> result = subjectService.getSubjectScore(subjectId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void getSubjectScore_testChuan3() {
        // Môn học có nhiều điểm
        String subjectId = "1";

        List<ScoreDto> result = subjectService.getSubjectScore(subjectId);

        assertNotNull(result);
        assertEquals(true, result.size() > 1);
    }

    @Test
    public void getSubjectScore_testNgoaiLe1() {
        // Môn học có id không tồn tại trong csdl
        String subjectId = "9999";

        List<ScoreDto> result;
        try {
            result = subjectService.getSubjectScore(subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        assertNull(result);
    }

    @Test
    public void getSubjectScore_testNgoaiLe2() {
        // Môn học có id null
        String subjectId = null;
        List<ScoreDto> result = subjectService.getSubjectScore(subjectId);

        assertNull(result);
    }

    // **************************** TEST saveSubject() ************************************ //
    @Test
    public void saveSubject_testChuan1() {
        // Dữ liệu mới hợp lệ, môn học tồn tại
        subjectData.setTenMonHoc("new name");
        subjectData.setSoTinChi(4);
        subjectData.setTyLediemChuyenCan(25);
        subjectData.setTyLediemBaiTapLon(25);
        subjectData.setTyLediemKiemTraGiuaKy(25);
        subjectData.setTyLediemKiemTraCuoiKy(25);

        boolean result = subjectService.saveSubject(subjectData);

        assertEquals(true, result);

        SubjectDto justUpdatedSubject = subjectService.getSubjectById(subjectData.getId());
        assertEquals("new name", justUpdatedSubject.getTenMonHoc());
        assertEquals(4, justUpdatedSubject.getSoTinChi());
        assertEquals(25, justUpdatedSubject.getTyLediemChuyenCan());
        assertEquals(25, justUpdatedSubject.getTyLediemBaiTapLon());
        assertEquals(25, justUpdatedSubject.getTyLediemKiemTraGiuaKy());
        assertEquals(25, justUpdatedSubject.getTyLediemKiemTraCuoiKy());
    }

    @Test
    public void saveSubject_testChuan2() {
        // Dữ liệu mới hợp lệ, môn học tồn tại, đã tồn tại một bảng điểm của môn học trước khi sửa cấu hình.

        subjectData.setTenMonHoc("new name");
        subjectData.setSoTinChi(4);
        subjectData.setTyLediemChuyenCan(25);
        subjectData.setTyLediemBaiTapLon(25);
        subjectData.setTyLediemKiemTraGiuaKy(25);
        subjectData.setTyLediemKiemTraCuoiKy(25);

        List<ScoreDto> listScoreBeforeChaneSubject = subjectService.getSubjectScore(subjectData.getId());

        boolean result = subjectService.saveSubject(subjectData);
        assertEquals(true, result);

        List<ScoreDto> listScoreAfterChaneSubject = subjectService.getSubjectScore(subjectData.getId());

        for (int i = 0; i < listScoreBeforeChaneSubject.size(); i++) {
            for (int j = 0; j < listScoreAfterChaneSubject.size(); j++) {
                if (
                    listScoreBeforeChaneSubject.get(i).getMaSinhVien() == listScoreAfterChaneSubject.get(j).getMaSinhVien() &&
                    listScoreBeforeChaneSubject.get(i).getKhoa() == listScoreAfterChaneSubject.get(j).getKhoa()
                ) {
                    assertEquals(listScoreBeforeChaneSubject.get(i).getDiemTrungBinh(), listScoreAfterChaneSubject.get(j).getDiemTrungBinh());
                    break;
                }
            }
         }
    }

    @Test
    public void saveSubject_testNgoaiLe1() {
        // Môn học không tồn tại
        subjectData.setId("9999");

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe2() {
        // Dữ liệu mới là rỗng
        subjectData = null;

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe3() {
        // Dữ liệu môn học mới có trường tín chỉ là 0, môn học tồn tại
        subjectData.setSoTinChi(0);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe4() {
        // Dữ liệu môn học mới có trường tín chỉ là một số âm, môn học tồn tại
        subjectData.setSoTinChi(-4);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe5() {
        // Dữ liệu môn học mới tổng tỷ lệ phần trăm các đầu điểm khác 100%, môn học tồn tại
        subjectData.setTyLediemChuyenCan(20);
        subjectData.setTyLediemBaiTapLon(20);
        subjectData.setTyLediemKiemTraGiuaKy(200);
        subjectData.setTyLediemKiemTraCuoiKy(111);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe6() {
        // Dữ liệu môn học mới có trường tên môn học là rỗng, môn học tồn tại
        subjectData.setTenMonHoc("");

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe7() {
        // Dữ liệu môn học mới có trường tỷ lệ điểm chuyên cần nhỏ hơn 0, môn học tồn tại
        subjectData.setTyLediemChuyenCan(-10);
        subjectData.setTyLediemBaiTapLon(10);
        subjectData.setTyLediemKiemTraGiuaKy(100);
        subjectData.setTyLediemKiemTraCuoiKy(0);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe8() {
        // Dữ liệu môn học mới có trường tỷ lệ điểm kiểm tra giữa kỳ nhỏ hơn 0, môn học tồn tại
        subjectData.setTyLediemChuyenCan(10);
        subjectData.setTyLediemBaiTapLon(-10);
        subjectData.setTyLediemKiemTraGiuaKy(100);
        subjectData.setTyLediemKiemTraCuoiKy(0);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe9() {
        // Dữ liệu môn học mới có trường tỷ lệ điểm bài tập lớn nhỏ hơn 0, môn học tồn tại
        subjectData.setTyLediemChuyenCan(10);
        subjectData.setTyLediemBaiTapLon(0);
        subjectData.setTyLediemKiemTraGiuaKy(-10);
        subjectData.setTyLediemKiemTraCuoiKy(100);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    @Test
    public void saveSubject_testNgoaiLe10() {
        // Dữ liệu môn học mới có trường tỷ lệ điểm kiểm tra cuối kỳ nhỏ hơn 0, môn học tồn tại
        subjectData.setTyLediemChuyenCan(10);
        subjectData.setTyLediemBaiTapLon(0);
        subjectData.setTyLediemKiemTraGiuaKy(100);
        subjectData.setTyLediemKiemTraCuoiKy(-10);

        boolean result;
        try {
            result = subjectService.saveSubject(subjectData);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        assertEquals(false, result);
    }

    // **************************** TEST getSubjectById() ************************************ //

    @Test
    public void getSubjectById_testChuan1() {
        // subjectId hợp lệ, môn học tồn tại

        String subjectId = "1";
        SubjectDto result = subjectService.getSubjectById(subjectId);

        assertNotNull(result);
        assertEquals(subjectId, result.getId());
    }

    @Test
    public void getSubjectById_testChuan2() {
        // subjectId hợp lệ, môn học không tồn tại
        String subjectId = "99999";
        SubjectDto result = subjectService.getSubjectById(subjectId);

        assertNull(result);
    }

    @Test
    public void getSubjectById_testNgoaiLe1() {
        // subjectId là null
        String subjectId = null;

        SubjectDto result;
        try {
            result = subjectService.getSubjectById(subjectId);
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
    }

    @Test
    public void getSubjectById_testNgoaiLe2() {
        // subjectId là một số âm
        String subjectId = "-4";

        SubjectDto result;
        try {
            result = subjectService.getSubjectById(subjectId);
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
    }

    // **************************** TEST getSubjectNameById() ************************************ //

    @Test
    public void getSubjectNameById_testChuan1() {
        // subjectId hợp lệ, môn học tồn tại

        String subjectId = subjectData.getId();
        String result = subjectService.getSubjectNameById(subjectId);

        assertNotNull(result);
        assertEquals(subjectData.getTenMonHoc(), result);
    }

    @Test
    public void getSubjectNameById_testChuan2() {
        // subjectId hợp lệ, môn học không tồn tại
        String subjectId = "99999";
        String result = subjectService.getSubjectNameById(subjectId);

        assertNull(result);
    }

    @Test
    public void getSubjectNameById_testNgoaiLe1() {
        // subjectId là null
        String subjectId = null;

        String result;
        try {
            result = subjectService.getSubjectNameById(subjectId);
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
    }

    @Test
    public void getSubjectNameById_testNgoaiLe2() {
        // subjectId là một số âm
        String subjectId = "-4";

        String result;
        try {
            result = subjectService.getSubjectNameById(subjectId);
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
    }

}
