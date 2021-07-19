package com.news.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    JOURNALIST,
    SUBSCRIBER,
    UNREGISTERED;


    @Override
    public String getAuthority() {
        return name();
    }
}
