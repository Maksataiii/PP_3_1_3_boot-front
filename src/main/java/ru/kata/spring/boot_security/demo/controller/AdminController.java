package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    /***
     * Сохранить в базу
     */
    @PostMapping
    public String createUser(@RequestParam("listOfRoles") Collection<Role> roles, @ModelAttribute("newUser")@Valid User user, BindingResult bindingResult) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        if(bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.createOrUpdate(user);
        return "redirect:/admin";
    }
    /***
     * Получить всех пользователей
     */
    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("users", userService.getUsersList());
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("newUser", new User());
        model.addAttribute("listOfRoles", roleService.getRoles());
        return "user_list";
    }

    /***
     * Получить одного пользователя
     */
    @GetMapping("/{id}")
    public String getUserById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("user", userService.getUser(id));
        return "user";
    }
    /***
     * Сохранить изменённого пользователя
     */
    @PatchMapping("/{id}")
    public String editUser(@RequestParam("listOfRoles") Collection<Role> roles, @ModelAttribute("editUser") User user, @PathVariable("id") int id) {
        user.setUsername(user.getUsername());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setAge(user.getAge());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setRoles(roles);
        userService.createOrUpdate(user);
        return "redirect:/admin";
    }
    /***
     * Удалить пользователя (подготовки объекта User не требуется)
     */
    @DeleteMapping("/{id}")
    public String deleteUserById(@ModelAttribute("user") User user,
                                 @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
