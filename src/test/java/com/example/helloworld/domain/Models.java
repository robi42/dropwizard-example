package com.example.helloworld.domain;

import org.joda.time.DateTime;

import static java.util.TimeZone.getTimeZone;
import static org.joda.time.DateTimeZone.UTC;

public class Models {

    private Models() {
    }


    public static Person createTestPerson() {
        final Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setCreatedAt(new DateTime(1371208810893L).withZone(UTC));
        person.setBirthday(DateTime.parse("1980-1-1").withZone(UTC).toDate());
        person.setTimeZonePreference(getTimeZone(UTC.getID()));

        return person;
    }
}
