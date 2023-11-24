package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final String REDIRECT_USERS = "redirect:users";
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("currentUser", userService.getUser(principal.getName()));
        return "user";
    }

    @GetMapping(value = "/users")
    public String showUsersListPage(Model model) {
        System.out.println("get /users");
        model.addAttribute("allUsers", userService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/addUser")
    public String showAddUserPage(Model model) {
        System.out.println("get /addUser");
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping(value = "/addUser")
    public String addUser(@ModelAttribute("user") User user) {
        System.out.println("post /addUser");
        if (userService.getUser(user.getUsername()) != null) {
            return REDIRECT_USERS;
        }
        userService.saveOrUpdate(user);
        return REDIRECT_USERS;
    }

    @GetMapping(value = "/editUser")
    public String showEditUserPage(@RequestParam("id") long id, Model model) {
        System.out.println("get /editUser");
        User userFromDb = userService.getUser(id);
        System.out.println("Edit user /get " + userFromDb);
        model.addAttribute("user", userFromDb);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping(value = "/editUser")
    public String editUser(@RequestParam("id") long id, @ModelAttribute("user") User user) {
        System.out.println("Edit user /post " + user);
        user.setId(id);
        userService.saveOrUpdate(user);
        return REDIRECT_USERS;
    }

    @GetMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        System.out.println("get /deleteUser");
        userService.delete(id);
        return REDIRECT_USERS;
    }
}
