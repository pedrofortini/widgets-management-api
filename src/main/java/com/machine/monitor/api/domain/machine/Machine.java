package com.machine.monitor.api.domain.machine;


import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.UnprocessableEntityException;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.domain.useracess.UserAcess;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Table(name = "machine")
@EnableAutoConfiguration
@Data
public class Machine implements Serializable {

    private static final long serialVersionUID = 2787308952488235560L;

    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="machine_is_up")
    @NotNull
    private boolean machineIsUp;

    @Column(name="ip_address")
    @NotNull
    private String ipAddress;

    @Column(name="last_downtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDownTime;

    @OneToMany(
            mappedBy = "machine",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<UserAcess> users = new HashSet<>();

    public User getMachineAdminUser(){

        List<UserAcess> adminUserAcess = this.users.stream()
                .filter(userAcess -> userAcess.isUserAdmin())
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(adminUserAcess)){

            return adminUserAcess.stream()
                    .map(userAcess -> userAcess.getUser())
                    .findFirst().get();
        }
        return null;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return machine.equals(machine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
