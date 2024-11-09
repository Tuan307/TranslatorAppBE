package com.translator.up.service.review;

import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.ReviewEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ReviewRequest;
import com.translator.up.model.response.review.ReviewDTO;
import com.translator.up.model.response.review.ReviewProjectDTO;
import com.translator.up.model.response.review.ReviewUserDTO;
import com.translator.up.repository.review.ReviewRepository;
import com.translator.up.repository.user.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProjectRepository projectRepository;

    public ApiResponse<Boolean> addReviews(ReviewRequest request, Long userId) {
        Optional<ProjectEntity> project = projectRepository.findByUserIdAndProjectId(userId, request.getProjectId());
        Optional<ReviewEntity> projectReview = reviewRepository.findReviewByProjectId(request.getProjectId());
        if (project.isPresent()) {
            if (projectReview.isPresent()) {
                project.get().setReview(null);
                reviewRepository.deleteById(projectReview.get().getId());
            }
            if (project.get().getTranslator() == null) {
                return new ApiResponse<>("error", "Error", null, "400");
            }
            ReviewEntity review = new ReviewEntity();
            review.setReviewStar(request.getReviewStar());
            review.setFeedBack(request.getFeedBack());
            review.setProject(project.get());
            review.setSender(project.get().getUser());
            review.setReceiver(project.get().getTranslator());
            project.get().setStatus("success");
            projectRepository.save(project.get());
            reviewRepository.save(review);
            return new ApiResponse<>("success", "Successful", true, "");
        } else {
            return new ApiResponse<>("error", "Error", null, "400");
        }
    }

    public ApiResponse<List<ReviewDTO>> getReviews(Long id) {
        List<ReviewEntity> list = reviewRepository.findReviewsByUserId(id);
        List<ReviewDTO> result = new ArrayList<>();
        for (ReviewEntity i : list) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(i.getId());
            dto.setFeedBack(i.getFeedBack());
            dto.setStarReview(i.getReviewStar());
            dto.setSender(new ReviewUserDTO(i.getSender().getId(), i.getSender().getFullName()));
            dto.setProject(new ReviewProjectDTO(i.getProject().getId(), i.getProject().getTitle()));
            result.add(dto);
        }
        return new ApiResponse<>("success", "Successful", result, "");
    }
}
