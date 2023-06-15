package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class CourseDto {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String imageUrl;
    @NonNull
    private CourseStatus courseStatus;
    @NonNull
    private UUID userInstructor;
    @NonNull
    private CourseLevel courseLevel;
}
