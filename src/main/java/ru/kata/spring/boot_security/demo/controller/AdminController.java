package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.EnumRole;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @PostConstruct
    void init() {
        Arrays.stream(EnumRole.values())
                .map(x -> x.toString())
                .forEach(x -> roleService.addRole(new Role(x)));


        User startUser = new User("admin", "admin", "admin","admin");
        startUser.addRoleToUser(roleService.findByName("ROLE_ADMIN"));
        userService.addUser(startUser);

    }


    @GetMapping()
    public String showAllUsers(Model model, Authentication authentication) {
        model.addAttribute("autUser", userService.findByEmail(authentication.getName()));
        model.addAttribute("listUsers", userService.getAllUsers());
        List<Role> roles = (List<Role>) roleService.getAllRoles();
        model.addAttribute("allRoles", roles);
        model.addAttribute("newUser", new User());
        return "adminPanel";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping()
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PutMapping
    public String update(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }


}
