package flearn.service;

import flearn.entity.Classroom;
import flearn.entity.ClassMember;
import flearn.entity.User;
import flearn.repository.ClassroomRepository;
import flearn.repository.ClassMemberRepository; // Import thêm
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassMemberRepository classMemberRepository; // Tiêm thêm repo thành viên lớp

    public List<Classroom> getClassesByTeacher(User teacher) {
        return classroomRepository.findByTeacher(teacher);
    }

    public Classroom getClassById(Integer classId) {
        return classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
    }

    public void createClass(String className, User teacher) {
        Classroom newClass = Classroom.builder()
                .className(className)
                .teacher(teacher)
                .inviteCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .isActive(true)
                .build();
        classroomRepository.save(newClass);
    }

    public void toggleClassStatus(Integer classId) {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
        classroom.setIsActive(!classroom.getIsActive());
        classroomRepository.save(classroom);
    }

    // === CÁC HÀM MỚI PHỤC VỤ TÍNH NĂNG DUYỆT HỌC SINH ===
    public List<ClassMember> getPendingMembers(Integer classId) {
        Classroom classroom = getClassById(classId);
        return classMemberRepository.findByClassroomAndStatus(classroom, 0); // Lấy học sinh có status = 0
    }

    public void approveMember(Integer memberId) {
        ClassMember member = classMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tham gia."));
        member.setStatus(1); // Đổi sang Đã duyệt
        classMemberRepository.save(member);
    }

    public void rejectMember(Integer memberId) {
        ClassMember member = classMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tham gia."));
        member.setStatus(2); // Đổi sang Từ chối
        classMemberRepository.save(member);
    }
}