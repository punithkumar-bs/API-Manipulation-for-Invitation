package com.hubspot.rest.api.service;

import com.hubspot.rest.api.dao.HubspotDao;
import com.hubspot.rest.api.helper.HubspotHelper;
import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubbspotServiceImpl implements HubspotService{

    @Autowired
    private HubspotDao hubspotDao;

    @Override
    public List<Partner> getPartnersAvailablitiy() {
        List<Partner> partnersList = hubspotDao.getPartnersAvailability();
        return partnersList;
    }

    @Override
    public List<Invitation> getInvitationsList(List<Partner> partnersList) {
        return HubspotHelper.checkAvailableDatesAndGetInvitations(partnersList);
    }

    @Override
    public String sendPartnerInvitations(List<Invitation> invitations) {
        return hubspotDao.sendPartnerInvitations(invitations);
    }
}
