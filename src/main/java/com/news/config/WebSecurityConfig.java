package com.news.config;

import com.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
класс , который при старте приложения
конфигурирует Web Security System.
Система заходит, передает на вход объект и
  в нем включаем авторизацию.
указываем, что для этого пути,
 по которому проходит пользователь,
  разрешаем полный доступ.
для всех остальных запросов - требуем авторизацию.
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //включаем авторизацию
                .authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                //форма
                    .formLogin()
              // login страница находится по маппинге /login
                    .loginPage("/login")
                //разрешаем пользоваться всем
                    .permitAll()
                .and()
                //сессия востановит значения
                    .rememberMe()
                .and()
                //включаем  logout страница
                    .logout()
                //разрешаем пользоваться всем
                    .permitAll();
    }




    // метод создает менеджер ,   который обслуживает учётные записи
          @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
