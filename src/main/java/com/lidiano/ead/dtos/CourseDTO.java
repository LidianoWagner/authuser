package com.lidiano.ead.dtos;

import java.util.UUID;

import com.lidiano.ead.enums.CourseLevel;
import com.lidiano.ead.enums.CourseStatus;

import lombok.Data;

@Data
public class CourseDTO {

	private UUID courseId;
	private String name;
	private String description;
	private String imageUrl;
	private CourseStatus courseStatus;
	private UUID userInstructor;
	private CourseLevel courseLevel;
}
