package com.machine.monitor.api.domain.useracess;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class UserAcessId implements Serializable {

    private static final long serialVersionUID = 6061622068149314581L;

    @Column(name = "user_id")
    private String userLogin;

    @Column(name = "machine_id")
    private Long machineId;

    public UserAcessId(String userLogin, Long machineId) {

        this.userLogin = userLogin;
        this.machineId = machineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserAcessId that = (UserAcessId) o;
        return Objects.equals(userLogin, that.userLogin) &&
                Objects.equals(machineId, that.machineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin, machineId);
    }
}
