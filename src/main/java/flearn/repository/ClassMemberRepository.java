package flearn.repository;

import flearn.entity.ClassMember;
import flearn.entity.Classroom;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMember, Integer> {
    // Lấy danh sách lớp mà sinh viên đã tham gia
    List<ClassMember> findByStudent(User student);
    // Kiểm tra xem sinh viên đã ở trong lớp này chưa
    boolean existsByClassroomAndStudent(Classroom classroom, User student);

    // Thêm dòng này vào bên trong interface ClassMemberRepository
    List<ClassMember> findByClassroomAndStatus(Classroom classroom, Integer status);
}