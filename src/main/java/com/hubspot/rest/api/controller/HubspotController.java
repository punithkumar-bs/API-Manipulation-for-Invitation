package com.hubspot.rest.api.controller;

import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;
import com.hubspot.rest.api.service.HubspotService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hubspot-coding-challenge/")
@Slf4j
public class HubspotController {

    @Autowired
    private HubspotService hubspotService;

    private List<Partner> partnersList;

    private List<Invitation> invitationsList;


    @ApiOperation(value = "method to get list of partners with availability, transform the data and send post request for invitations.")
    @RequestMapping(value = "/" + "partners", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getPartnersAndSendInvitations() {
        partnersList = hubspotService.getPartnersAvailablitiy();

        if (CollectionUtils.isEmpty(partnersList)) {
            log.error("Unable to get partners list information");
            return "Unable to get partners list.";
        }

        invitationsList = hubspotService.getInvitationsList(partnersList);
        return hubspotService.sendPartnerInvitations(invitationsList);
    }
}
