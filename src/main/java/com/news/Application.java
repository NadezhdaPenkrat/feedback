package com.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
файл запуск. будет запускать приложение
с помощью аннотации @SpringBootApplication
(поднимет, запустит , найдет контролеры,
также найдет файл или подключение к базе данных
 и прочее)
*/
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
