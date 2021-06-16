package com.hubspot.rest.api.helper;

import com.hubspot.rest.api.model.Invitation;
import com.hubspot.rest.api.model.Partner;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HubspotHelper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Invitation> checkAvailableDatesAndGetInvitations(List<Partner> partnersList) {

        Map<String, String> countryToDateMap = new HashMap<>();

        Map<String, List<Integer>> countriesToIndexMap = new HashMap<>();
        for (int i = 0; i < partnersList.size(); i++) {
            String country = partnersList.get(i).getCountry();
            List<Integer> indexes = countriesToIndexMap.getOrDefault(country, new ArrayList<>());
            indexes.add(i);
            countriesToIndexMap.put(country, indexes);
        }

        for (String country : countriesToIndexMap.keySet()) {
            List<String> datesList = new ArrayList<>();

            // loop through mapped index position for each country to get dates list.
            for (int index : countriesToIndexMap.get(country)) {
                List<String> dates = partnersList.get(index).getAvailableDates();

                for (int j = 0; j < dates.size() - 1; j++) {
                    if (getDiffInDates(dates.get(j), dates.get(j + 1)) == 1) {
                        datesList.add(dates.get(j));
                    }
                }
            }

            // check the duplicates strings in the dates list and add it's count to a map.
            HashMap<String, Integer> datesCountMap = new HashMap<>();
            for (String dateStr : datesList) {
                datesCountMap.put(dateStr, datesCountMap.getOrDefault(dateStr, 0) + 1);
            }

            // get maximum dates count from map.
            int maxDateCount = Collections.max(datesCountMap.values());

            // get list of dates which has maximum count.
            List<String> similarCountDatesList = new ArrayList<>();
            for (String mapDateStr : datesCountMap.keySet()) {
                if (datesCountMap.get(mapDateStr) == maxDateCount) {
                    similarCountDatesList.add(mapDateStr);
                }
            }

            // sort similar dates and get the lowest date available from max count.
            Collections.sort(similarCountDatesList);

            if (similarCountDatesList.size() > 0) {
                countryToDateMap.put(country, similarCountDatesList.get(0));
            } else {
                countryToDateMap.put(country, null); //null if not found any
            }

        }

        // countries and dates are mapped to countryToDateMap.
        return generateInvitationRequestObj(countryToDateMap, partnersList, countriesToIndexMap);

    }

    /**
     * Generate invitation request post object.
     * @param countryToDateMap
     * @param partnersList
     * @param countriesToIndexMap
     * @return
     * @throws Exception
     */
    public static List<Invitation> generateInvitationRequestObj(Map<String, String> countryToDateMap,
                                                                List<Partner> partnersList, Map<String, List<Integer>>
                                                                        countriesToIndexMap) {

        List<Invitation> invitationsList = new ArrayList<>();

        for (String country : countriesToIndexMap.keySet()) {
            int attendeeCount = 0;
            Set<String> attendeesList = new HashSet<>();

            for (int i : countriesToIndexMap.get(country)) {

                List<String> datesList = partnersList.get(i).getAvailableDates();
                if (datesList.contains(countryToDateMap.get(country))) {
                    Date currDate = null;

                    try {
                        currDate = dateFormat.parse(countryToDateMap.get(country));
                    } catch (ParseException e) {
                        log.error("Unable to parse date in countryToDateMap: " + e.getMessage());
                    }

                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(currDate);
                    cal1.add(Calendar.DATE, 1);

                    Date nextDay = cal1.getTime();
                    String nextDayStr = dateFormat.format(nextDay);

                    if (datesList.contains(nextDayStr)) {
                        attendeesList.add(partnersList.get(i).getEmail());
                        attendeeCount++;
                    }
                }
            }

            Invitation invitation = new Invitation();

            invitation.setStartDate(countryToDateMap.get(country));
            invitation.setName(country);
            invitation.setAttendeeCount(attendeeCount);
            List<String> attendeesListArray = new ArrayList<>(attendeesList);
            invitation.setAttendees(attendeesListArray);

            invitationsList.add(invitation);
        }
        return invitationsList;
    }


    /**
     * Get diff in days between two dates.
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    private static long getDiffInDates(String startDateStr, String endDateStr) {
        long diff = 0;
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            Period difference = Period.between(startDate, endDate);
            diff = difference.getDays();
        } catch (Exception ex) {
            log.error("Exception when comparing dates: " + ex.getMessage());
        }
        return diff;
    }
}
