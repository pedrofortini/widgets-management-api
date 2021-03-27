package com.machine.monitor.api.domain.useracess;

import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_acess")
@NoArgsConstructor
@Getter
@Setter
public class UserAcess {

    @EmbeddedId
    private UserAcessId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userLogin")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("machineId")
    private Machine machine;

    @Column(name = "is_user_admin")
    private boolean isUserAdmin;

    public UserAcess(User user, Machine machine, boolean isUserAdmin) {

        this.user = user;
        this.machine = machine;
        this.isUserAdmin = isUserAdmin;
        this.id = new UserAcessId(user.getLogin(), machine.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserAcess that = (UserAcess) o;
        return Objects.equals(machine, that.machine) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, user);
    }
}
