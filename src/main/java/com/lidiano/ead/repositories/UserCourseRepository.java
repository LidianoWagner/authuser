package com.lidiano.ead.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lidiano.ead.models.UserCourseModel;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {

}
