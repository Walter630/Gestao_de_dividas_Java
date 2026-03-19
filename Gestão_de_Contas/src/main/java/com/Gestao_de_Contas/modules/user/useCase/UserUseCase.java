package com.Gestao_de_Contas.modules.user.useCase;

import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserUseCase {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User execute(User user) {
        try {
            this.userRepository.findByName(user.getUsername())
                .ifPresent(user1 -> {throw new RuntimeException("Nome ja existe");
            });

            var password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);

            return userRepository.save(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
