package ru.spring.education.TgBot.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.spring.education.TgBot.Model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
