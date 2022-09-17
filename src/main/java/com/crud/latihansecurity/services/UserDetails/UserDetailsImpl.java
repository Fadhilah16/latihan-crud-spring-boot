package com.crud.latihansecurity.services.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.crud.latihansecurity.models.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private Long id;
	private String username;
    

    @JsonIgnore
	private String password;
    private Collection<? extends GrantedAuthority> authorities;
    public UserDetailsImpl(Long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    public static UserDetailsImpl build(UserEntity user) {
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getPassword(), 
				authorities);
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        return authorities;
    }

    public Long getId() {
        return id;
    }
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean equals(Object arg0) {
        // TODO Auto-generated method stub
        if (this == arg0){
            return true;
        }
        if(arg0 == null || getClass() != arg0.getClass()){
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) arg0;
        return Objects.equals(id, user.id);
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
}
