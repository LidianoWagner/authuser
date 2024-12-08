package com.lidiano.ead.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lidiano.ead.clients.UserClient;
import com.lidiano.ead.dtos.CourseDTO;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

	@Autowired
	UserClient userClient;

	@GetMapping("/users/{userid}/courses")
	public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(
			@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Direction.ASC) Pageable pageable,
			@PathVariable UUID userid) {
		return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllCoursesByUser(userid, pageable));
	}
}
