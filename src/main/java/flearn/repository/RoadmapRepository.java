package flearn.repository;

import flearn.entity.Classroom;
import flearn.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Integer> {
    List<Roadmap> findByClassRoomOrderByCreatedAtDesc(Classroom classRoom);

    List<Roadmap> findByClassRoomAndPublishedTrueOrderByCreatedAtDesc(Classroom classRoom);
}
