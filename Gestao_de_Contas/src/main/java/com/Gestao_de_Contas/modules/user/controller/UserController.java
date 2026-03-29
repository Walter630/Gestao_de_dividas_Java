package com.Gestao_de_Contas.modules.user.controller;

import com.Gestao_de_Contas.modules.user.dto.AuthUserDTO;
import com.Gestao_de_Contas.modules.user.dto.TokenDTO;
import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.modules.user.useCase.AuthUserUseCase;
import com.Gestao_de_Contas.modules.user.useCase.LoginAttemptService;
import com.Gestao_de_Contas.modules.user.useCase.RefreshTokenUseCase;
import com.Gestao_de_Contas.modules.user.useCase.UserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final LoginAttemptService loginAttemptService;
    private final AuthUserUseCase authUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final UserUseCase userUseCase;

    // ─── Cadastro ────────────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            var created = userUseCase.execute(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    // ─── Login ───────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthUserDTO request,
            HttpServletRequest httpRequest) {

        String ip       = getClientIp(httpRequest);
        String username = request.getUsername();

        // 1. Checa bloqueio antes de qualquer operação
        if (loginAttemptService.isBlocked(username, ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Conta temporariamente bloqueada. Tente novamente em breve.");
        }

        try {
            // 2. Tenta autenticar passando o DTO corretamente
            TokenDTO token = authUserUseCase.execute(request);

            // 3. Sucesso: zera o contador no Redis
            loginAttemptService.loginSucceeded(username, ip);

            return ResponseEntity.ok(token);

        } catch (UsernameNotFoundException ex) {
            // ✅ 4. Falha: incrementa contador e informa tentativas restantes
            loginAttemptService.loginFailed(username, ip);
            int restantes = loginAttemptService.remainingAttempts(username);
            String msg = restantes > 0
                    ? "Invalid credentials. Tentativas restantes: " + restantes
                    : "Conta temporariamente bloqueada.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(msg);

        } catch (AuthenticationServiceException ex) {
            loginAttemptService.loginFailed(username, ip);
            int restantes = loginAttemptService.remainingAttempts(username);
            String msg = restantes > 0
                    ? "Senha incorreta. Tentativas restantes: " + restantes
                    : "Conta bloqueada por 1 minuto.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(msg);
        }
    }

    // ─── Refresh Token ───────────────────────────────────────────────────────
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        try {
            var novoToken = refreshTokenUseCase.execute(refreshToken);
            return ResponseEntity.ok(novoToken);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}