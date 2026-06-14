package flearn.module.content.service;

import flearn.module.content.dto.request.LearningLessonRequest;
import flearn.module.content.dto.request.MaterialRequest;
import flearn.module.content.dto.request.RoadmapRequest;
import flearn.module.content.dto.response.LessonResponse;
import flearn.module.content.dto.response.MaterialResponse;
import flearn.module.content.dto.response.MaterialTrackingResponse;
import flearn.module.content.dto.response.RoadmapResponse;
import flearn.enums.MaterialType;
import flearn.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface LearningContentService {
    List<RoadmapResponse> getTeacherRoadmaps(Integer classId, User teacher);

    RoadmapResponse getTeacherRoadmap(Integer roadmapId, User teacher);

    RoadmapResponse getTeacherRoadmapWithContent(Integer roadmapId, User teacher);

    void createRoadmap(Integer classId, User teacher, @Valid RoadmapRequest request);

    void updateRoadmap(Integer roadmapId, User teacher, @Valid RoadmapRequest request);

    void deleteRoadmap(Integer roadmapId, User teacher);

    void toggleRoadmapPublished(Integer roadmapId, User teacher);

    void createLesson(Integer roadmapId, User teacher, @Valid LearningLessonRequest request);

    LessonResponse getTeacherLesson(Integer lessonId, User teacher);

    List<MaterialResponse> getTeacherLessonMaterials(Integer lessonId, User teacher);

    void updateLesson(Integer lessonId, User teacher, @Valid LearningLessonRequest request);

    void toggleLessonVisible(Integer lessonId, User teacher);

    void deleteLesson(Integer lessonId, User teacher);

    void createMaterial(Integer lessonId, User teacher, @Valid MaterialRequest request);

    MaterialResponse getTeacherMaterial(Integer materialId, User teacher);

    void updateMaterial(Integer materialId, User teacher, @Valid MaterialRequest request);

    void toggleMaterialPublished(Integer materialId, User teacher);

    void deleteMaterial(Integer materialId, User teacher);

    List<RoadmapResponse> getStudentRoadmaps(Integer classId, User student);

    LessonResponse getStudentLesson(Integer lessonId, User student);

    List<MaterialResponse> getStudentLessonMaterials(Integer lessonId, User student);

    MaterialResponse getStudentMaterial(Integer materialId, User student);

    void markMaterialViewed(Integer materialId, User student);

    List<MaterialTrackingResponse> getLearningHistory(User student);

    MaterialType[] getMaterialTypes();
}
