package com.Gestao_de_Contas.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JWTProviders {
    @Value("${security.token}")
    private String secretToken;

    public String validateToken(String token){
        if(token == null || token.isBlank()) return null;
        token = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.HMAC256(secretToken);

        try{

            var subject = JWT.require(algorithm)
                    .withIssuer("Gestao de Contas")
                    .build()
                    .verify(token)
                    .getSubject();
            return subject;
        }catch(JWTVerificationException e){
            return null;
        }
    }

    public String generateToken(String subject) {
        Algorithm  algorithm = Algorithm.HMAC256(secretToken);
        var tokenCreate = JWT.create()
                .withSubject(subject)
                .withIssuer("Gestao de Contas")
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(5)))
                .sign(algorithm);
        return tokenCreate;
    }
}
