package com.nathan.usermanagementapi.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nathan.usermanagementapi.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Custom UserDetails implementation that represents the details of a user for authentication and authorization.
 */
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L; // The serial version UID for serialization

    @Getter
    private final Long id; // The user's ID

    @Getter
    private final String name; // The user's name

    @Getter
    private final String email; // The user's email address

    @JsonIgnore
    private final String password; // The user's password

    private final Collection<? extends GrantedAuthority> authorities; // The authorities granted to the user

    /**
     * Constructs a new UserDetailsImpl object with the provided user details and authorities.
     *
     * @param id          the user's ID
     * @param name        the user's name
     * @param email       the user's email address
     * @param password    the user's password
     * @param authorities the authorities granted to the user
     */
    public UserDetailsImpl(Long id, String name, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Build a UserDetailsImpl object based on the user entity.
     *
     * @param user the user entity from which to build the UserDetailsImpl
     * @return a UserDetailsImpl object representing the user details
     */
    public static UserDetailsImpl build(User user) {
        // For simplicity, all users have the same role
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));

        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

/**
 * Retrieves the authorities granted to the user.
 *
 * @return a collection of granted authorities
 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the username of the user.
     * <p>
     * In this application, the email address is used as the username.
     *
     * @return the username (email address) of the user
     */
    @Override
    public String getUsername() {
        // Email is used as the username
        return email;
    }

    /**
     * Indicates whether the user account has expired.
     * <p>
     * In this application, accounts never expire.
     *
     * @return true if the account has not expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is not locked.
     * <p>
     * In this application, accounts are never locked.
     *
     * @return true if the account is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user account password has not expired.
     * <p>
     * In this application, passwords never expire.
     *
     * @return true if the account password has not expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is enabled.
     * <p>
     * In this application, all accounts are enabled.
     *
     * @return true if the account is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Checks if the given object is equal to this user.
     *
     * <p>
     * Two users are considered equal if they have the same id.
     *
     * @param o the object to compare with
     * @return true if the object is equal to this user, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    /**
     * Computes the hash code for this user.
     *
     * <p>
     * The hash code is based on the user's id.
     *
     * @return the hash code of this user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}