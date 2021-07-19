package com.news.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usr") //Храниться будет в таблице usr
public class User implements UserDetails {
    //создаем поля Id
    @Id
    // автоматически сгенерировнное значение базой данных
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // поле имя не должно быть пустым, заполнить
    @NotBlank(message = "Username cannot be empty")
    // у пользователя будет имя
    private String username;

    // поле пароль не должно быть пустым, заполнить
    @NotBlank(message = "Password cannot be empty")
    // у пользователя имеется пароль
    private String password;
    //признак активности
    private boolean active;


    // поле почта не должно быть пустым или несоответствующим, заполнить
    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    //для оповещения пользователя добавляются поля
    // email и activationCode, чтобы подтвердить,
    //действительно ли ими владеет
    private String email;
    private String activationCode;


    //ролевая система USER,ADMIN, JOURNALIST, SUBSCRIBER, UNREGISTERED;
    // fetch -определяет как параметры данные значений будут подгружаться
    // относительно основной сущности из таблицы ролей
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    // обратная связь author
    // при загрузке текста подгружаться автоматически не должны
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<News> tidings;

    // для обеспечения связи между пользователем и пользователем
    @ManyToMany
    //таблица подписки пользователя, хранения подписок на себя
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    //список подписчиков
    private Set<User> subscribers = new HashSet<>();

    @ManyToMany
    //таблица подписчики пользователя, хранения подписчиков
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "channel_id")}
    )
    //список подписок
    private Set<User> subscriptions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Set<News> getTidings() {
        return tidings;
    }

    public void setTidings(Set<News> tidings) {
        this.tidings = tidings;
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<User> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
