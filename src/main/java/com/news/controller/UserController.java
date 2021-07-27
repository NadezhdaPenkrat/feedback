package com.news.controller;

import com.news.model.Role;
import com.news.model.User;
import com.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
программный модуль контролер, по пути /user
получает запросы от пользователя и
возвращает данные текущей ситуации
в файл шаблон.
 @PreAuthorize("hasAuthority('')") для каждого из методов
  в данном контроллере будет проверять наличие у пользователя прав

 */

@Controller
@RequestMapping("/user") //при запросе к методу будет сожержать путь /user
public class UserController {
    @Autowired
    private UserService userService;


    //для каждого из методов в данном классе  будут доступны не только админу ,
    //  но и любому авторизованному пользователю
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) { // добавить список пользователей  ввиде модели
        model.addAttribute("users", userService.findAll());
        // возращаем список пользвателей
        return "userList";
    }

    //для каждого из методов в данном классе  будут доступны не только админу ,
    //  но и любому авторизованному пользователю
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}") //добавляем путь для редактора пользователя, делаем GetMapping и записываем user
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        //добавляем атрибут роль и вызываем enum все значения
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    //для каждого из методов в данном классе  будут доступны не только админу ,
    //  но и любому авторизованному пользователю
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping  // добавляем метод сохранение PostMapping
    public String userSave(
            // чтобы сохранить данные пользователя нужно получить данные с сервера
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        //пользователь сохраняется в хранилище
        userService.saveUser(user, username, form);
        // перенаправление на список пользователей
        return "redirect:/user";
    }

    // возвраращает форму с профилем для просмотра данных
    @GetMapping("profile")
    // на вход принимает модель (помещаются данные) и ожидать пользователя с контекстом,
    // чтобы не получать из базы данных
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        // в модели ожидаем  имя пользователя и почту
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        // возращаем пользователю форму profile
        return "profile";
    }

    // возвраращает форму с профилем для сохранения данных
    @PostMapping("profile")
    //метод возвращает строку, с запрашиваемыми параметрами пользователь, пароль и почта
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {
        // обрабатывать замену всех параметров, где передается пользователь, пароль и почта
        userService.updateProfile(user, password, email);
        // после обновления данных, пользователь направляется на страницу /user/profile
        return "redirect:/user/profile";
    }

    // подписки
    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.subscribe(currentUser, user);

        //возврат на страницу user-tidingsnewss с Id пользователя, на которого подписываемся
        return "redirect:/user-tidingsnews/" + user.getId();
    }

    //отписки
    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.unsubscribe(currentUser, user);
        //возврат на страницу user-tidingsnews
        return "redirect:/user-tidingsnews/" + user.getId();
    }


    //метод сожержит тип (отмечается кто, подписчики или подписки) и чьи, в список
    @GetMapping("{type}/{user}/list")
    public String userList(
            //выводится в модель
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ) {
        //отражаются атрибуты
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        //выводить список в зависимости от типа
        if ("subscriptions".equals(type)) {
            // в модель выводятся подписки
            model.addAttribute("users", user.getSubscriptions());
        } else {
            // в модель выводятся подписчики
            model.addAttribute("users", user.getSubscribers());
        }
        //возврат на страницу подписок
        return "subscriptions";
    }
}
