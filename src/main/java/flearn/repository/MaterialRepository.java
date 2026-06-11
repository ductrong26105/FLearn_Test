package flearn.repository;

import flearn.entity.Lesson;
import flearn.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findByLessonOrderByCreatedAtAsc(Lesson lesson);

    List<Material> findByLessonAndPublishedTrueOrderByCreatedAtAsc(Lesson lesson);
}
