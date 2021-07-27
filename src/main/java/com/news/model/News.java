package com.news.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// This tells Hibernate to make a table out of this class
@Entity
public class News {
    //создаем поля Id
    @Id
    // автоматически сгенерировнное значение базой данных
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;


    // поле текста не должно быть пустым, заполнить
    @NotBlank(message = "Please fill the text")
    // установлена длина текста
    @Length(max = 2048, message = "Text too long (more than 2kB)")
    private String text;
    @Length(max = 255, message = "Text too long (more than 255)")
    private String title;

    /*
    указываем базе данных, что у нас в этой связи
    одному пользователю соответсвует множество
    сообщений
    добавим режим выборки который позволяет каждый раз когда
    хотим получить сообщение , также получать информацию об авторе
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public News() {
    }

    public News(String title, String text, User user) {

        this.title = title;
        this.text = text;
        this.author = user;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
