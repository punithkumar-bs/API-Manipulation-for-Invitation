package com.hubspot.rest.api.service;

import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;

import java.util.List;

public interface HubspotService {

    List<Partner> getPartnersAvailablitiy();

    List<Invitation> getInvitationsList(List<Partner> partnersList);

    String sendPartnerInvitations(List<Invitation> invitations);
}
