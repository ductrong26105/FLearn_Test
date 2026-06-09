package flearn.repository;

import flearn.entity.Classroom;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    // Tìm danh sách lớp học do một giáo viên cụ thể phụ trách
    List<Classroom> findByTeacher(User teacher);
    //
    Optional<Classroom> findByInviteCodeAndIsActiveTrue(String inviteCode);
}