package ru.spring.education.TgBot.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.education.TgBot.Model.User;
import ru.spring.education.TgBot.Model.UserAuthority;
import ru.spring.education.TgBot.Model.UserRole;
import ru.spring.education.TgBot.Repository.UserRepository;
import ru.spring.education.TgBot.Repository.UserRolesRepository;
import ru.spring.education.TgBot.exceptions.UsernameAlreadyExistsException;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registration(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = userRepository.save(
                    new User()
                            .setId(null)
                            .setUsername(username)
                            .setPassword(passwordEncoder.encode(password))
                            .setLocked(false)
                            .setExpired(false)
                            .setEnabled(true)
            );
            userRolesRepository.save(new UserRole(null, UserAuthority.USER, user));
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public List<UserRole> getUserRoles(Long userId) {
        return userRolesRepository.findByUserId(userId);
    }

    @Transactional
    public void addRole(Long userId, UserAuthority authority) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRolesRepository.save(new UserRole(null, authority, user));
    }

    @Transactional
    public void removeRole(Long userId, UserAuthority authority) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserRole> roles = userRolesRepository.findByUserId(userId);
        roles.stream()
                .filter(role -> role.getUserAuthority() == authority)
                .forEach(userRolesRepository::delete);
    }
}

