package ru.spring.education.TgBot.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.spring.education.TgBot.Model.UserRole;

import java.util.List;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);
}