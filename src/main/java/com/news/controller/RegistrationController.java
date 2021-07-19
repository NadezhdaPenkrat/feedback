package com.news.controller;

import com.news.model.User;
import com.news.model.dto.CaptchaResponseDto;
import com.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {


    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;
    // получить secret
    @Value("${recaptcha.secret}")
    private String secret;
    // подключить
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(

            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponce,
            //устанавливаем действительность пользователя
            @Valid
                    User user,
            //получать проверку ошибок компиляции
            BindingResult bindingResult,
            // добавить список пользователей  ввиде модели
            Model model
    ) {
        // полученый шаблон подставляются аргументы
        String url = String.format(CAPTCHA_URL, secret, captchaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        // проверка на получение ответа
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }
        //возвращать будет значение с состаяниями пароля
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        //проверка наличия подтверждения пароля, что пароль не пустой
        if (isConfirmEmpty) {
            // и если ошибка , будет оповещение
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        //проверка соответсвия паролей, введенных в форму  при регистрации, пользователем
        //получить пароль неравный ноль или этот пароль не равен паролю2
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            //сообщить об этом пользователю
            model.addAttribute("passwordError", "Passwords are different!");
        }
        //при проверке ошибок компиляции, по их получению, их нужно  преобразовать
        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            //попадают в список errors
            model.mergeAttributes(errors);
            // возвращаем пользователя на страницу регистрации
            return "registration";
        }
        // если не смогли добавить пользователя, значит пользоватль существует
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            // и возращаем на страницу формы регистрации
            return "registration";
        }

        // при успешной авторизации перенаправление пользователя на страницу логина
        return "redirect:/login";
    }


    //после получения пользователем уведомления, нужно обработать подтверждение аккаунта
    //пользователь будет переходть по ссылке из письма, в котором будет код
    @GetMapping("/activate/{code}")
    //метод активации, присутствует модель с сообщением, такп же будет лежать код
    public String activate(Model model, @PathVariable String code) {
        //возвращать будет значение с состаяниями, пользователь активирован или нет
        boolean isActivated = userService.activateUser(code);
        // проверка активации
        if (isActivated) {
            model.addAttribute("messageType", "success");// прошло
            model.addAttribute("message", "User successfully activated");
        } else {
            //пользователь не найден с таким кодом активации
            model.addAttribute("messageType", "danger");//безуспешная попытка
            model.addAttribute("message", "Activation code is not found!");
        }
        // возвращать ссылку на login страницу
        return "login";
    }
}
