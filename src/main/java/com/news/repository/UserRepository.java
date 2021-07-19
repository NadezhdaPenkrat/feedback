package com.news.repository;

import com.news.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// для нахождения пользователя
public interface UserRepository extends JpaRepository<User, Long> {

    // метод, который будет возвращать пользователя и принимать строку ввиде username
    User findByUsername(String username);

    User findByActivationCode(String code);
}
