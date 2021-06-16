package com.hubspot.rest.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Invitation {

    private int attendeeCount;
    private List<String> attendees;
    private String name;
    private String startDate;

}
