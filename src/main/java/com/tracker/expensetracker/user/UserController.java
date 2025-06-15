package com.tracker.expensetracker.user;

import java.net.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    
    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
    	UserResponse lResponse= new UserResponse();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null && existingAuth.isAuthenticated()&& !(existingAuth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(lResponse);
        }

        // Authenticate the user using your CustomAuthenticationProvider
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticated = customAuthenticationProvider.authenticate(authentication);
        if (authenticated.isAuthenticated()) {
            // The user is authenticated, you can continue with the user login process
            // ...
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        	User user = userRepository.findByUsername(username);

            // Generate JWT token
            String token = jwtUtil.generateToken(username);
            lResponse.setToken(token);
            lResponse.setUser(user);
            System.out.println(token);

            return ResponseEntity.status(HttpStatus.OK).body(lResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(lResponse);
        }
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user logged in");
    }
}
