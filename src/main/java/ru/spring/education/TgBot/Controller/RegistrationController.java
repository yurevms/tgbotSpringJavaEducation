package ru.spring.education.TgBot.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spring.education.TgBot.Service.UserService;
import ru.spring.education.TgBot.Service.UserServiceImpl;


@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<Void> registration(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        userService.registration(username, password);
        return ResponseEntity.ok().build();
    }

}
