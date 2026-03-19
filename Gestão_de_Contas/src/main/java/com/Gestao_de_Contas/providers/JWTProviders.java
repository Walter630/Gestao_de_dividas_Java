package com.Gestao_de_Contas.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JWTProviders {
    @Value("${security.token}")
    private String secretToken;

    public String validateToken(String token){
        token = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.HMAC256(secretToken);

        try{
            var subject = JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
            return subject;
        }catch(JWTVerificationException e){
            e.printStackTrace();
            return "";
        }
    }
}
