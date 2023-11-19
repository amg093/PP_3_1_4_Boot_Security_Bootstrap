package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final String REDIRECT_USERS = "redirect:users";
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("currentUser", userService.findUserByUsername(principal.getName()));
        return "user";
    }


    @GetMapping(value = "/users")
    public String showUserListPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/addUser")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "addUser";
    }

    @PostMapping(value = "/addUser")
    public String addUser(@ModelAttribute("user") User user) {
        if (userService.findUserByUsername(user.getUsername()) != null) {
            return REDIRECT_USERS;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return REDIRECT_USERS;
    }

    @GetMapping(value = "/editUser")
    public String showEditUserPage(@RequestParam("id") Long id, Model model) {
        Optional<User> user = userService.findUserById(id);
        user.ifPresent(notOptionalUser -> model.addAttribute("user", notOptionalUser));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "editUser";
    }

    @PostMapping(value = "/editUser")
    public String editUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return REDIRECT_USERS;
    }

    @GetMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return REDIRECT_USERS;
    }
}
