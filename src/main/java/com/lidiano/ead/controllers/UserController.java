package com.lidiano.ead.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


import com.fasterxml.jackson.annotation.JsonView;
import com.lidiano.ead.dtos.UserDto;
import com.lidiano.ead.models.UserModel;
import com.lidiano.ead.services.UserService;
import com.lidiano.ead.specifications.SpecificationTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "userid", direction = Direction.ASC) Pageable pageable,
			@RequestParam(required = false) UUID courseId) {
		log.info("Iniciando busca de todos os usuários com paginação: {}", pageable);
		Page<UserModel> userModelPage = null;
		if(courseId != null) {
			userModelPage = userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
		} else {
			userModelPage = userService.findAll(spec, pageable);
		}

		if (!userModelPage.isEmpty()) {
			for (UserModel user : userModelPage.toList()) {
				user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserid())).withSelfRel());
			}
		}
		
		log.info("Busca de usuários finalizada. Total de registros encontrados: {}", userModelPage.getTotalElements());
		return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Object> getOneUser(@PathVariable UUID userId) {
		log.info("Buscando usuário com ID: {}", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			log.warn("Usuário com ID {} não encontrado.", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {
			log.info("Usuário encontrado: {}", userModelOptional.get());
			return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
		log.info("Tentativa de exclusão do usuário com ID: {}", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			log.warn("Usuário com ID {} não encontrado para exclusão.", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {
			userService.delete(userModelOptional.get());
			log.info("Usuário com ID {} excluído com sucesso.", userId);
			return ResponseEntity.status(HttpStatus.OK).body("User deleted success!");
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable UUID userId,
			@RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
		
		log.info("Iniciando atualização do usuário com ID: {}", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			log.warn("Usuário com ID {} não encontrado para atualização.", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {
			var userModel = userModelOptional.get();
			userModel.setFullName(userDto.getFullName());
			userModel.setPhoneNumber(userDto.getPhoneNumber());
			userModel.setCpf(userDto.getCpf());
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);
			log.info("Usuário com ID {} atualizado com sucesso.", userId);
			return ResponseEntity.status(HttpStatus.OK).body(userModel);
		}
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<Object> updatePassword(@PathVariable UUID userId,
			@RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
		log.info("Atualizando senha para o usuário com ID: {}", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			log.warn("Usuário com ID {} não encontrado para atualização de senha.", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		}
		if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
			log.warn("Senha antiga inválida para o usuário com ID: {}", userId);
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
		} else {
			var userModel = userModelOptional.get();
			userModel.setPassword(userDto.getPassword());
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);
			log.info("Senha do usuário com ID {} atualizada com sucesso.", userId);
			return ResponseEntity.status(HttpStatus.OK).body("Password update successfully!");
		}
	}

	@PutMapping("/{userId}/image")
	public ResponseEntity<Object> updateImage(@PathVariable UUID userId,
			@RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
		log.info("Atualizando imagem para o usuário com ID: {}", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			log.warn("Usuário com ID {} não encontrado para atualização de imagem.", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {
			var userModel = userModelOptional.get();
			userModel.setImageUrl(userDto.getImageUrl());
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);
			log.info("Imagem do usuário com ID {} atualizada com sucesso.", userId);
			return ResponseEntity.status(HttpStatus.OK).body(userModel);
		}
	}
}
