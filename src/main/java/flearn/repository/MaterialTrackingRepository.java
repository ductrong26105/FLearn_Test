package flearn.repository;

import flearn.entity.Material;
import flearn.entity.MaterialTracking;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialTrackingRepository extends JpaRepository<MaterialTracking, Integer> {
    Optional<MaterialTracking> findByStudentAndMaterial(User student, Material material);

    List<MaterialTracking> findByMaterial(Material material);

    List<MaterialTracking> findByStudentOrderByViewedAtDesc(User student);
}
