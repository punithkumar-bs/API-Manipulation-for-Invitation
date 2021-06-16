package com.hubspot.rest.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Partner {

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private List<String> availableDates;

}
