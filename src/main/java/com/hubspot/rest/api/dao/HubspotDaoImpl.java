package com.hubspot.rest.api.dao;

import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;
import com.hubspot.rest.api.model.PartnerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class HubspotDaoImpl implements HubspotDao {

    @Value("${hubspot.get.partners.url}")
    private String getPartnersUrl;

    @Value("${hubspot.post.invitation.url}")
    private String postInvitationUrl;


    @Override
    public List<Partner> getPartnersAvailability() {
        RestTemplate restTemplate = new RestTemplate();
        PartnerList result = restTemplate.getForObject(getPartnersUrl, PartnerList.class);
        return Objects.requireNonNull(result).getPartners();
    }

    @Override
    public String sendPartnerInvitations(List<Invitation> invitationList) {
        String responseMessage;
        try {
            Map<String, List<Invitation>> list = new HashMap<>();
            list.put("countries", invitationList);

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<Map<String, List<Invitation>>> invitationListRequest = new HttpEntity<>(list);

            ResponseEntity<Invitation> result = restTemplate.postForEntity(
                    postInvitationUrl,
                    invitationListRequest,
                    Invitation.class);

            responseMessage = "Successfully invited, Status code: " + result.getStatusCode().toString();

        } catch (HttpClientErrorException e) {
            log.error("An Exception occured with while calling {}, returned a status code of {} because of message {}",
                    postInvitationUrl,
                    e.getStatusCode(),
                    e.getMessage());

            responseMessage = "Unsuccessful in inviting because : " + e.getMessage();
        }
        return responseMessage;
    }
}
