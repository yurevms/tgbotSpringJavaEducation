package ru.spring.education.TgBot.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spring.education.TgBot.Model.UserAuthority;
import ru.spring.education.TgBot.Model.UserRole;
import ru.spring.education.TgBot.Service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserServiceImpl userService;

    @PostMapping("/add")
    public ResponseEntity<Void> addRole(
            @RequestParam("userId") Long userId,
            @RequestParam("role") UserAuthority authority) {
        userService.addRole(userId, authority);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeRole(
            @RequestParam("userId") Long userId,
            @RequestParam("role") UserAuthority authority) {
        userService.removeRole(userId, authority);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserRole>> getUserRoles(@PathVariable Long userId) {
        List<UserRole> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}
