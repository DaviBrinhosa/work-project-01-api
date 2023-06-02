package com.work.project01.userdata.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.project01.userdata.dto.UserDataDTO;
import com.work.project01.userdata.entity.UserData;
import com.work.project01.userdata.service.UserDataService;

@RestController
@RequestMapping(value = "/userData")
public class UserDataController {
	
	@Autowired
	private final UserDataService userService;
	
	public UserDataController(UserDataService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<UserDataDTO> findAll() {
		List<UserDataDTO> result = userService.findAll();
		return result;
	}
	
	@GetMapping(value = "/{id}")
	public UserDataDTO findById(@PathVariable Long id) {
		UserDataDTO result = userService.findById(id);
		return result;
	}	
	
	@PostMapping(value = "/register")
	public ResponseEntity<String> createUser(@RequestBody UserData user) {
		userService.createUser(user);
		return ResponseEntity.ok("Usuário cadastrado com sucesso! Efetue seu login.");
	}
	
	@PostMapping(value = "/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody UserDataDTO request) {
        
        String email = request.getEmail();
        String password = request.getPassword();
           
        try {
            UserDataDTO user = userService.findByEmail(email);
            
            // Verificar se a senha fornecida corresponde à senha armazenada para o usuário
            if (user.getPassword().equals(password)) {
                return ResponseEntity.ok(email);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado com o e-mail: " + email);
        }
    }
	
}
