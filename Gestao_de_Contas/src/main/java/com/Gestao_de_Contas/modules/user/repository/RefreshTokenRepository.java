package com.Gestao_de_Contas.modules.user.repository;

import com.Gestao_de_Contas.modules.user.entity.RefreshToken;
import com.Gestao_de_Contas.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);

    // No seu RefreshTokenRepository — adicione esse método
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiryDate < :now")
    void deleteByExpiryDateBefore(@Param("now") Instant now);
}
