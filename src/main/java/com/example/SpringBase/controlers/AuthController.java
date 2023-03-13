package com.example.SpringBase.controlers;

import com.example.SpringBase.JwtUtil;
import com.example.SpringBase.dto.LoginRequest;
import com.example.SpringBase.dto.LoginResponse;
import com.example.SpringBase.dto.MessageResponse;
import com.example.SpringBase.dto.RegisterRequest;
import com.example.SpringBase.models.User;
import com.example.SpringBase.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.SpringBase.models.Role.ROLE_USER;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        ResponseEntity<LoginResponse> response = null;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            ).getPrincipal();


            final User user = userService.findByEmail(loginRequest.getEmail());
            final String jwtToken = jwtUtil.generateToken(user);

            if(!user.isVerified()){
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponse(null, null, null, null, null, null, "You are not verified yet!")
                );
            }else{
                response = ResponseEntity.ok(
                        new LoginResponse(jwtToken, user.getId(), user.getEmail(), user.getRoles(), user.getName(), user.getSurname(), "Login successful!")
                );
            }
        } catch (BadCredentialsException badCredentialsException) {
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new LoginResponse(null, null, null, null, null, null,"Incorrect email or password!")
            );
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        if(userService.existsByEmail(registerRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User with that email already exists!"));
        }else{
            userService.save(
                    User.builder()
                            .email(registerRequest.getEmail())
                            .password(passwordEncoder.encode(registerRequest.getPassword()))
                            .name(registerRequest.getName())
                            .surname(registerRequest.getSurname())
                            .roles(Collections.singletonList(ROLE_USER))
                            .verified(false)
                            .build()
            );

            return ResponseEntity.ok(new MessageResponse("User created!"));
        }
    }
}
