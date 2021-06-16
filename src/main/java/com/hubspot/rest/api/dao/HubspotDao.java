package com.hubspot.rest.api.dao;

import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HubspotDao {

    List<Partner> getPartnersAvailability();

    String sendPartnerInvitations(List<Invitation> invitationList);
}
