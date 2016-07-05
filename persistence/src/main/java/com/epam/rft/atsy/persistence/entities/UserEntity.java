package com.epam.rft.atsy.persistence.entities;

import lombok.*;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "users", schema = "atsy", uniqueConstraints = @UniqueConstraint(columnNames = "user_name"))
public class UserEntity extends SuperEntity implements java.io.Serializable{


    @Column(name = "user_name", nullable = false, length = 255, table = "users")
    private String userName;

    @Column(name = "user_pwd", nullable = false, length = 255, table = "users")
    private String userPassword;


}
