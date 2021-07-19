package com.news.service;

import com.news.model.Role;
import com.news.model.User;
import com.news.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired // подключаем класс на отправку оповещений MailSender
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;
     // добавляется пользователь по имени
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        // проверка на наличие пользователя
        if (user == null) { //если пользователь пуст
            //бросается ошибка
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }


    //поиск пользователя после его сохранение
    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        // в случае,если  в базе данных найдено совпадение пользователя,
        //  нужно сообщить пользователю, который пытается зарегестрироваться
        if (userFromDb != null) {
            // пользователь найден в базе данных,
            // возвращаем false , что будет обозначать ,
            // что пользователь не добавлен
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        //почта подтверждена , когда пользователь перешел по сслыке с UUID
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        sendMessage(user);

        // в случае, что всё прошло успешно и пользователь добавлен,
        // из метода вовращается true, что означает - пользователь создан
        return true;
    }


    // реализуем отправку оповещения
    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            //приветсвие пользователя,с предложением перейти по ссылке для подтверждения почтового ящика
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Feedback. Please, visit next link: http://%s/activate/%s",
                    //заполнить переменные имя пользователя и  переменные активации кода, для их заполнения
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    // метод активации пользователя
    public boolean activateUser(String code) {
        //поиск пользователя по коду активации
        User user = userRepository.findByActivationCode(code);
        //проверяем,если пользователь не был найден
        if (user == null) {
            //возращаем значение того, что активация не удалась
            return false;
        }
        //выполняется  подтверждение пользователем своего почтового ящика
        user.setActivationCode(null);
        // передаем пользователя в хранилище пользователей, чтобы сохранить
        userRepository.save(user);

        //если же  пользователь  был найден, активация удалась
        return true;
    }


    // метод список пользователей, будет возвращать лист пользователей
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // сохранить пользователей
    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    // метод обновления профиля
    public void updateProfile(User user, String password, String email) {
        // берется у пользователя текущая почта
        String userEmail = user.getEmail();
        // проверка  значений  на изменение данных текущей почты
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || //не пусто или
                (userEmail != null && !userEmail.equals(email)); // не соответсвует почте

        //  изменения произошли
        if (isEmailChanged) {
            // нужно обновить у пользователя
            user.setEmail(email);
            // проверка пользователя на установку новой почты
            if (!StringUtils.isEmpty(email)) {
                //устанавливаем новый код активайии пользователю, который генерируется  по сслыке с UUID
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        // проверка установки пользователем нового пароля
        if (!StringUtils.isEmpty(password)) {
            //устанавливаем в учетную запись
            user.setPassword(password);
        }
        // сохранить пользователя в базе данных
        userRepository.save(user);

        //условие, что оповещение  отправляется в случае изменения почты
        if (isEmailChanged) {
            // отправить оповещение
            sendMessage(user);
        }
    }

    // метод подписки
    public void subscribe(User currentUser, User user) {
        //пользователю подписчиков добавляется текущий пользователь
        user.getSubscribers().add(currentUser);
        // сохраняется этот пользователь
        userRepository.save(user);
    }

    // метод отписки
    public void unsubscribe(User currentUser, User user) {
        //пользователю подписчиков удаляется текущий пользователь
        user.getSubscribers().remove(currentUser);
        // сохраняется удаление
        userRepository.save(user);
    }
}
