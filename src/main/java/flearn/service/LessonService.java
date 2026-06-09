package flearn.service;

import flearn.entity.Classroom;
import flearn.entity.Lesson;
import flearn.repository.ClassroomRepository;
import flearn.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ClassroomRepository classroomRepository;

    public List<Lesson> getLessonsByClass(Integer classId) {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
        return lessonRepository.findByClassroomOrderByCreatedAtDesc(classroom);
    }

    public void createLesson(Integer classId, String title, String content, String videoUrl) {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        // Xử lý thông minh: Chuyển link YouTube thường thành link Embed
        if (videoUrl != null && videoUrl.contains("youtube.com/watch?v=")) {
            videoUrl = videoUrl.replace("watch?v=", "embed/");
            // Cắt bỏ các tham số thừa phía sau (như &t=12s) nếu có
            if (videoUrl.contains("&")) {
                videoUrl = videoUrl.substring(0, videoUrl.indexOf("&"));
            }
        }

        Lesson newLesson = Lesson.builder()
                .title(title)
                .content(content)
                .videoUrl(videoUrl)
                .classroom(classroom)
                .build();

        lessonRepository.save(newLesson);
    }
}