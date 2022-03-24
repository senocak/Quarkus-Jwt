package com.github.senocak.service;

import com.github.senocak.entity.User;
import com.github.senocak.exception.ServerException;
import com.github.senocak.repository.UserRepository;
import com.github.senocak.util.OmaErrorMessageType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;
    @Inject EntityManager entityManager;

    public User findByUsername(String username) throws ServerException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new ServerException(OmaErrorMessageType.BASIC_INVALID_INPUT,
                        new String[]{"User: " + username + " already exist"}, Response.Status.CONFLICT)
        );
    }

    public User findByUsernameAndPassword(String username, String password) throws ServerException {
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(() ->
                new ServerException(OmaErrorMessageType.GENERIC_SERVICE_ERROR,
                        new String[]{"Unexpected error."}, Response.Status.INTERNAL_SERVER_ERROR));
    }

    public List<User> getAll() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }
}
