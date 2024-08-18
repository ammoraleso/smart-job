package co.com.andres.domain;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    private Date lastLogin;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        lastLogin = createdAt;
        active = true;
    }
}