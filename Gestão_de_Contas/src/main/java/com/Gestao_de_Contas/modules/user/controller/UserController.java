package com.Gestao_de_Contas.modules.user.controller;

import com.Gestao_de_Contas.modules.user.dto.AuthUserDTO;
import com.Gestao_de_Contas.modules.user.dto.UserDTO;
import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.modules.user.useCase.AuthUserUseCase;
import com.Gestao_de_Contas.modules.user.useCase.RefreshTokenUseCase;
import com.Gestao_de_Contas.modules.user.useCase.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;
    @Autowired
    private AuthUserUseCase  authUserUseCase;
    @Autowired
    private RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> generateToken(@RequestBody String refreshToken){
        try {
            var refreshTokenValid = this.refreshTokenUseCase.execute(refreshToken);
            return ResponseEntity.ok().body(refreshTokenValid);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthUserDTO user){
        try {
            var token = this.authUserUseCase.execute(user);
            return ResponseEntity.ok().body(token);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
