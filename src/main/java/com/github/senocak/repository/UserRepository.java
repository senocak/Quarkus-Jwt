package com.github.senocak.repository;

import com.github.senocak.entity.User;
import com.github.senocak.exception.ServerException;
import com.github.senocak.util.OmaErrorMessageType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public List<User> findAllUsers() {
        return getUsers(null);
    }
    public List<User> findAllByName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        return getUsers(parameters);
    }

    public Optional<User> findByUsername(String username) {
        return find("username", username).singleResultOptional();
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) throws ServerException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        List<User> getUsers = getUsers(parameters);
        if (getUsers.isEmpty())
            throw new ServerException(OmaErrorMessageType.NOT_FOUND,
                    new String[]{"Username: " + username}, Response.Status.NOT_FOUND);
        if (getUsers.size() > 1)
            throw new ServerException(OmaErrorMessageType.GENERIC_SERVICE_ERROR,
                    new String[]{"Multiple records found."}, Response.Status.CONFLICT);
        return getUsers.stream().findFirst();
    }


    /**
     *
     * @param parameters
     * @return
     */
    private List<User> getUsers(Map<String, Object> parameters){
        if (parameters == null) {
            return listAll();
        }

        Map<String, Object> nonNullParams = parameters.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (nonNullParams.isEmpty()) {
            return listAll();
        }

        String query = nonNullParams.keySet().stream()
                .map(o -> o + "=:" + o)
                .collect(Collectors.joining(" and "));

        return find(query, nonNullParams).list();
    }
}
