package com.library.book_stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.book_stock.dto.UserResponse;
import com.library.book_stock.model.User;
import com.library.book_stock.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

    public final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> removeUser(@PathVariable Long id) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            userRepository.deleteById(id);
            UserResponse response = new UserResponse("Usuário removido.", null);
            return ResponseEntity.status(200).body(response);
        } else {
            UserResponse response = new UserResponse("Usuário não encontrado", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            UserResponse response = new UserResponse("Usuário já cadastrado", null);
            return ResponseEntity.status(409).body(response);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            String token = UUID.randomUUID().toString();
            user.setRecoveryCode(token);
            User newUser = userRepository.save(user);
            UserResponse response = new UserResponse("Usuário cadastrado com sucesso!", newUser);
            return ResponseEntity.status(201).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            String storedPassword = existingUser.get().getPassword();
            if (passwordEncoder.matches(user.getPassword(), storedPassword)) {
                UserResponse response = new UserResponse("Usuário logado", existingUser.get());
                return ResponseEntity.status(200).body(response);
            } else {
                UserResponse response = new UserResponse("Credenciais inválidas.", null);
                return ResponseEntity.status(401).body(response);
            }
        } else {
            UserResponse response = new UserResponse("Usuário não encontrado.", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping("/recovery")
    public ResponseEntity<UserResponse> passwordRecovery(@RequestBody User modifiedUser) {
        Optional<User> existingUser = userRepository.findByRecoveryCode(modifiedUser.getRecoveryCode());
        System.out.println(modifiedUser.getRecoveryCode());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setPassword(passwordEncoder.encode(modifiedUser.getPassword()));
            userRepository.save(user);
            UserResponse response = new UserResponse("Senha atualizada. Realize o login.", user);
            return ResponseEntity.status(200).body(response);
        } else {
            UserResponse response = new UserResponse("Código de recuperação não encontrado.", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
