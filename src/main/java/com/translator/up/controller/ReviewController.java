package com.translator.up.controller;

import com.translator.up.aop.SessionRequired;
import com.translator.up.entity.ReviewEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.SessionNotFoundException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ReviewRequest;
import com.translator.up.model.response.review.ReviewDTO;
import com.translator.up.service.review.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @SessionRequired
    @GetMapping("")
    public ApiResponse<List<ReviewDTO>> getAllPersonalReviews(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new SessionNotFoundException("Login required");
        }
        return reviewService.getReviews(user.getId());
    }

    @SessionRequired
    @PostMapping("/create")
    public ApiResponse<Boolean> addReviews(@RequestBody ReviewRequest request, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new SessionNotFoundException("Login required");
        }
        return reviewService.addReviews(request, user.getId());
    }
}
