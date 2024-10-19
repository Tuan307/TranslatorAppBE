package com.translator.up.repository.user;

import com.translator.up.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity p WHERE p.status = :status AND p.user.id = :userId")
    List<ProjectEntity> findByStatusAndUserId(@Param("status") String status, @Param("userId") Long userId);

}
