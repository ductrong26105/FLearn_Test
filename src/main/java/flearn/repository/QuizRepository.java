// QuizRepository.java
package flearn.repository;
import flearn.entity.Lesson;
import flearn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByLessonOrderByCreatedAtDesc(Lesson lesson);

    List<Quiz> findByLessonAndPublishedTrueOrderByCreatedAtDesc(Lesson lesson);

    boolean existsByLesson(Lesson lesson);

    Optional<Quiz> findByLesson(Lesson lesson);

    @org.springframework.data.jpa.repository.Query("SELECT q FROM Quiz q WHERE q.published = true AND q.deadline BETWEEN :start AND :end")
    List<Quiz> findUpcomingDeadlines(@org.springframework.data.repository.query.Param("start") java.util.Date start, @org.springframework.data.repository.query.Param("end") java.util.Date end);
}
