package com.lidiano.ead.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lidiano.ead.repositories.UserCourseRepository;
import com.lidiano.ead.services.UserCourseService;

@Service
public class UserCourseServiceImpl implements UserCourseService {

	@Autowired
	private UserCourseRepository userCourseRepository;
}
