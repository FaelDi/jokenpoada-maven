package tech.ada.games.jokenpo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ada.games.jokenpo.dto.LoginDto;
import tech.ada.games.jokenpo.response.AuthResponse;
import tech.ada.games.jokenpo.service.AuthService;

@RestController
@RequestMapping("/api/v1/jokenpo/login")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

}
