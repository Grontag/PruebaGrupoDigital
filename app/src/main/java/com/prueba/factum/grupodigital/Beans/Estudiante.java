package com.prueba.factum.grupodigital.Beans;

import java.util.Date;

/**
 * Created by David on 03/07/2017.
 */

public class Estudiante {
    private String name;
    private Integer id;
    private Date birthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}
