package com.library.book_stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.book_stock.dto.AdminResponse;
import com.library.book_stock.model.Admin;
import com.library.book_stock.repository.AdminRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    public final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    public AdminController(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdminResponse> removeAdmin(@PathVariable Long id) {
        Optional<Admin> existingAdmin = adminRepository.findById(id);

        if (existingAdmin.isPresent()) {
            adminRepository.deleteById(id);
            AdminResponse response = new AdminResponse("Administrador removido.", null);
            return ResponseEntity.status(200).body(response);
        } else {
            AdminResponse response = new AdminResponse("Administrador não encontrado", null);
            return ResponseEntity.status(404).body(response);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<AdminResponse> registerAdmin(@RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminRepository.findByUsername(admin.getUsername());
        
        if (existingAdmin.isPresent()) {
            AdminResponse response = new AdminResponse("Administrador já cadastrado", null);
            return ResponseEntity.status(409).body(response);
        } else {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin newAdmin = adminRepository.save(admin);
            AdminResponse response = new AdminResponse("Administrador cadastrado com sucesso!", newAdmin);
            return ResponseEntity.status(201).body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AdminResponse> loginAdmin(@RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminRepository.findByUsername(admin.getUsername());
        
        if (existingAdmin.isPresent()) {
            String storedPassword = existingAdmin.get().getPassword();
            if (passwordEncoder.matches(admin.getPassword(), storedPassword)) {
                AdminResponse response = new AdminResponse("Administrador logado", existingAdmin.get());
                return ResponseEntity.status(200).body(response);
            } else {
                AdminResponse response = new AdminResponse("Credenciais inválidas.", null);
                return ResponseEntity.status(401).body(response);
            }
        } else {
            AdminResponse response = new AdminResponse("Administrador não encontrado.", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
