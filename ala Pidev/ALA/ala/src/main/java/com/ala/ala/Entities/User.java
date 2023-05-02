package com.ala.ala.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "userp")
public class User implements Serializable {

    private static final long serialVersionUID = 1618L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String nomUser;

    private String emailUser;
    private Integer numTel;
    @Enumerated(EnumType.STRING)
    private Role role;
}