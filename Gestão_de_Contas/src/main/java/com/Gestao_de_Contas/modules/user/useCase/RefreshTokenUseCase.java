package com.Gestao_de_Contas.modules.user.useCase;

import com.Gestao_de_Contas.modules.user.repository.RefreshTokenRepository;
import com.Gestao_de_Contas.providers.JWTProviders;
import com.Gestao_de_Contas.security.TokenHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Instant;

@Service
public class RefreshTokenUseCase {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JWTProviders jwtProviders;

    public String execute(String token) throws AuthenticationServiceException {
        try{
            var tokenHash = TokenHashUtil.hashUtil(token);
            var refreshToken = refreshTokenRepository.findByToken(tokenHash)
                    .orElseThrow(() -> new AuthenticationServiceException("Invalid token"));
            if(Instant.now().isAfter(refreshToken.getExpiryDate())) {
                this.refreshTokenRepository.delete(refreshToken);
                throw new AuthenticationServiceException("Invalid token");
            }
            var newAccessToken = this.jwtProviders.generateToken(
                    refreshToken.getUser().getUsername()
            );
            return newAccessToken;
        }catch(Exception e){
            throw new AuthenticationServiceException("Invalid token");
        }
    }
}
