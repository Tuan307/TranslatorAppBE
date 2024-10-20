package com.translator.up.repository.user;

import com.translator.up.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity p WHERE p.status = :status AND p.user.id = :userId")
    List<ProjectEntity> findByStatusAndUserId(@Param("status") String status, @Param("userId") Long userId);

    @Query("SELECT p FROM ProjectEntity p WHERE p.sourceLanguage = :sourceLanguage AND p.targetLanguage = :targetLanguage")
    List<ProjectEntity> findBySourceAndTargetLanguage(@Param("sourceLanguage") String sourceLanguage, @Param("targetLanguage") String targetLanguage);

    @Query("SELECT p FROM ProjectEntity p WHERE p.user.id = :userId AND p.id = :id")
    Optional<ProjectEntity> findByUserIdAndProjectId(@Param("userId") Long userId, @Param("id") Long id);

}
