package com.github.martinfrank.elitegames.backend.user;

import com.github.martinfrank.elitegames.backend.metrics.MetricsController;
import com.github.martinfrank.elitegames.backend.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //registrierung
    //curl -X POST http://localhost:8080/api/auth/register   -H "Content-Type: application/json"   -d "{\"username\":\"martin\",\"password\":\"geheim123\"}"
    //curl --insecure -X POST https://localhost:8443/api/auth/register   -H "Content-Type: application/json"   -d "{\"username\":\"martin\",\"password\":\"geheim123\"}"

    //anmeldung:
    //curl -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d "{\"username\":\"martin\",\"password\":\"geheim123\"}"

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, AuthenticationManager authManager, JwtService jwtService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        LOGGER.info("POST /register");
        var user = userService.register(
                request.username(),
                request.password(),
//                request.roles() != null ? request.roles() : List.of("ROLE_USER"),
                List.of("ROLE_USER"), //wir erlauben aktuell nur rolle user!
                request.email(),
                request.firstName(),
                request.lastName()
        );
        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        LOGGER.info("POST /login");
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserEntity user = (UserEntity) auth.getPrincipal();
        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }
}
