package com.example.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String socialName;
    private String password;
    private String phone;
    private String address;
    @Setter
    private String email;
    private LocalDateTime registerDate;

    @ElementCollection
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "memberId", referencedColumnName = "id"))
    private List<String> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
