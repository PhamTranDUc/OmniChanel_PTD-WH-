package com.example.omnichannelfinal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;

    private String fullName;

//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Message> listMessage;
//
//    private String idFacebook;

//    private String image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> listRole= new ArrayList<>();
        for(Role role: this.roles){
            listRole.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }

        return listRole;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
        return true;
    }

    public String getListRole(){

        StringBuilder rolesStringBuilder = new StringBuilder();
        for (Role role : this.roles) {
            rolesStringBuilder.append(role.getName()).append(",");
        }
        // Loại bỏ dấu phẩy cuối cùng
        if (rolesStringBuilder.length() > 0) {
            rolesStringBuilder.setLength(rolesStringBuilder.length() - 1);
        }
        return rolesStringBuilder.toString();
    }

}
