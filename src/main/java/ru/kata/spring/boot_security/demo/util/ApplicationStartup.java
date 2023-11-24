package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;

@Service
public class ApplicationStartup {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ApplicationStartup(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    @Transactional
    public void onApplicationStartup() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.save(roleAdmin);
        roleService.save(roleUser);

        User admin = new User("admin", "admin");
        admin.getRoles().add(roleAdmin);
        userService.save(admin);

        User user = new User("user", "user");
        user.getRoles().add(roleUser);
        userService.save(user);
    }
}
