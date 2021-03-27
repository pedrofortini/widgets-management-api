package com.machine.monitor.api.domain.user;

import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.useracess.UserAcess;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@EnableAutoConfiguration
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 3096302027142979826L;

    @Id
    @NotNull
    private String login;

    @Column(name="name")
    @NotNull
    private String name;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<UserAcess> machines = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
