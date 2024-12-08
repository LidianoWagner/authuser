package com.lidiano.ead.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameConstraint, String> {

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (username == null || username.trim().isEmpty() || username.contains(" ")) {
			return false;
		}
		return true;
	}

}
