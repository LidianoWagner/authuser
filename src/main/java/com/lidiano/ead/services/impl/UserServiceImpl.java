package com.lidiano.ead.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lidiano.ead.models.UserModel;
import com.lidiano.ead.repositories.UserRepository;
import com.lidiano.ead.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<UserModel> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Optional<UserModel> findById(UUID userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId);
	}

	@Override
	public void delete(UserModel userModel) {
		// TODO Auto-generated method stub
		userRepository.delete(userModel);
	}

	@Override
	public void save(UserModel userModel) {
		// TODO Auto-generated method stub
		userRepository.save(userModel);
	}

	@Override
	public boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByemail(String email) {
		// TODO Auto-generated method stub
		return userRepository.existsByEmail(email);
	}

	@Override
	public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findAll(spec, pageable);
	}
}
