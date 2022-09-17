package com.crud.latihansecurity.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.latihansecurity.DTO.ResponseDTO;
import com.crud.latihansecurity.DTO.auth.JwtResponseDTO;
import com.crud.latihansecurity.DTO.auth.LoginRequestDTO;
import com.crud.latihansecurity.DTO.auth.RegisterRequestDTO;
import com.crud.latihansecurity.models.RoleEntity;
import com.crud.latihansecurity.models.UserEntity;
import com.crud.latihansecurity.models.enums.RoleTypes;
import com.crud.latihansecurity.repositories.RoleRepo;
import com.crud.latihansecurity.repositories.UserRepo;
import com.crud.latihansecurity.security.JwtUtils;
import com.crud.latihansecurity.services.UserDetails.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    AuthenticationManager authenticationManager;
    UserRepo userRepo;
    RoleRepo roleRepo;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, RoleRepo roleRepo,
            PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponseDTO(jwt,userDetails.getId(), userDetails.getUsername(), roles));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
		ResponseDTO<?> response = new ResponseDTO<>();
		if (userRepo.existsByUsername(registerRequest.getUsername())) {
			
			response.setStatus("400");
			response.setMessages(new ArrayList<String>(){{
				add("Error: Username is already taken!");
			}});
			response.setEntity(null);

			return ResponseEntity.badRequest().body(response);
		}

		UserEntity user = new UserEntity(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
		Set<String> strRoles = registerRequest.getRoles();
		Set<RoleEntity> roles = new HashSet<>();
		if (strRoles == null) {
			RoleEntity userRole = roleRepo.findByName(RoleTypes.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
				RoleEntity adminRole = roleRepo.findByName(RoleTypes.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
				RoleEntity userRole = roleRepo.findByName(RoleTypes.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepo.save(user);
		response.setStatus("200");
		response.setMessages(new ArrayList<String>(){{
			add("User registered successfully!");
		}});
		response.setEntity(null);
		return ResponseEntity.ok(response);
	}

    
    
    
}
