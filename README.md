Hubspot API Coding Challenge

Contains the logic to send invitations to a special two-day event in each country for clients partners in those countries. We need to find the dates that’ll work best based on survey results that partners have sent in and determine how many people can attend.
You’re provided with an API that gives you a list of partners, their countries, and which dates they’re available in ISO 8601 format.
Another team will send out the invitations, but you need to tell them when we should host the event and who should attend by POSTing to an API.
The date you send in for the country should be the starting date of the two day period where the most partners can make it for both days in a row. In case of multiple dates with the same number of partners, pick the earlier date. If there are no two days in a row when any partners can make it, return null.


Spring Boot REST API project

Export project to Intellij and start the service using Spring Boot.

Tomcat application will start on port : 8080

Hit url : http://localhost:8080/hubspot-coding-challenge/partners from browser or Postman URL.

If partners are retrieved and successfully transformed then the response of "Successfully invited, Status code: 200 OK" will be returned.

Otherwise "Unsuccessful in inviting because : 400" will be returned.