package flearn.repository;

import flearn.entity.Classroom;
import flearn.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    // Lấy danh sách bài giảng theo lớp học, sắp xếp bài mới nhất lên đầu
    List<Lesson> findByClassroomOrderByCreatedAtDesc(Classroom classroom);
}