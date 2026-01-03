package com.mxnuchim.auth.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mxnuchim.auth.domain.entities.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "roles")
public class Role extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<User> users;
}
