package com.lidiano.ead.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.lidiano.ead.dtos.UserDto;
import com.lidiano.ead.enums.UserStatus;
import com.lidiano.ead.enums.UserType;
import com.lidiano.ead.models.UserModel;
import com.lidiano.ead.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(
			@RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
		
		log.info("Iniciando o processo de registro para o usuário com nome: {}", userDto.getUsername());
		
		if (userService.existsByUsername(userDto.getUsername())) {
			log.warn("Erro de registro: o nome de usuário '{}' já está em uso.", userDto.getUsername());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
		}
		if (userService.existsByemail(userDto.getEmail())) {
			log.warn("Erro de registro: o email '{}' já está em uso.", userDto.getEmail());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
		}
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);
		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.STUDENT);
		userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		userService.save(userModel);
		log.debug("Usuário '{}' registrado com sucesso.", userModel.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}

	@GetMapping("/")
	public String index() {
		log.trace("Este é um log trace");
		log.info("Este é um log de informação");
		log.debug("Este é um log de debug");
		log.warn("Este é um log de aviso");
		log.error("Este é um log de erro");
        
        return "Logging Spring Boot...";
    }
}
