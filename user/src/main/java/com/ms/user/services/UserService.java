package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producer.UserProducer;
import com.ms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserProducer userProducer;
    private final UserRepository userRepository;

    public UserService(UserProducer userProducer, UserRepository userRepository) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserModel save(UserModel userModel) {
        // Salva o usuário
        userModel = userRepository.save(userModel);

        // Envia o email para o usuário informando o cadastro bem sucedido
        userProducer.publishMessageEmail(userModel);

        // retorna o usuário salvo
        return userModel;
    }

}
