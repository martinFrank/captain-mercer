package com.github.martinfrank.elitegames.backend.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    //curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
    //curl http://localhost:8080/api/admin/stats -H "Authorization: Bearer <TOKEN>"

    //geschützt durch SecurityConfig.securityFilterChain -> .requestMatchers("/api/admin/**").hasRole("ADMIN")
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/stats")
    public String getStats() {
        LOGGER.info("GET /stats");
        return "Nur Admins dürfen das sehen!";
    }

    /*
    Du kannst auch auf Methodenebene schützen:

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dangerous")
    public String dangerousStuff() {
        return "Top secret!";
    }

    Dafür brauchst du:

    @EnableMethodSecurity // ist in deiner SecurityConfig bereits aktiviert
     */
}
