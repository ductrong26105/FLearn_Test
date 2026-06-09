package flearn.service;

import flearn.entity.ClassMember;
import flearn.entity.Classroom;
import flearn.entity.User;
import flearn.repository.ClassMemberRepository;
import flearn.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final ClassMemberRepository classMemberRepository;
    private final ClassroomRepository classroomRepository;

    public List<ClassMember> getJoinedClasses(User student) {
        return classMemberRepository.findByStudent(student);
    }

    public void joinClass(String inviteCode, User student) {
        // 1. Tìm lớp học có mã code này và đang mở
        Classroom classroom = classroomRepository.findByInviteCodeAndIsActiveTrue(inviteCode)
                .orElseThrow(() -> new RuntimeException("Mã lớp không hợp lệ hoặc lớp đã bị đóng!"));

        // 2. Kiểm tra xem sinh viên đã join chưa
        if (classMemberRepository.existsByClassroomAndStudent(classroom, student)) {
            throw new RuntimeException("Bạn đã tham gia lớp học này rồi!");
        }

        // Tìm thấy đoạn lưu vào database ở cuối hàm joinClass và sửa thành thế này:
        ClassMember newMember = ClassMember.builder()
                .classroom(classroom)
                .student(student)
                .status(0) // ĐÂY LÀ DÒNG THAY ĐỔI: Mặc định là chờ duyệt
                .build();
        classMemberRepository.save(newMember);
    }
}