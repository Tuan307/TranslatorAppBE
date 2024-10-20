package com.translator.up.repository.review;

import com.translator.up.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT r FROM ReviewEntity r WHERE r.receiver.id = :id")
    List<ReviewEntity> findReviewsByUserId(@RequestParam("id") Long id);

    @Query("SELECT r FROM ReviewEntity r WHERE r.project.id = :id")
    Optional<ReviewEntity> findReviewByProjectId(@RequestParam("id") Long id);
}
