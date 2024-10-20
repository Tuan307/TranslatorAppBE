package com.translator.up.model.response.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewDTO {
    private Long id;
    private String feedBack;
    private Double starReview;
    private ReviewUserDTO sender;
    private ReviewProjectDTO project;
}

