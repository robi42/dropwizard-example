package com.example.helloworld.domain;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

@Data
@Entity
@Table(name = "people")
public class Person {

    @Id
    @Type(type = "pg-uuid")
    private UUID id;

    @NotNull
    @Column(name = "time_zone_preference")
    private TimeZone timeZonePreference;

    @NotNull
    @Column(name = "created_at")
    private DateTime createdAt;

    @Column(name = "modified_at")
    private DateTime modifiedAt;

    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "job_title")
    private String jobTitle;

    @Email
    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @Column(name = "favorite_number")
    private Integer favoriteNumber;

    @NotNull
    @Temporal(DATE)
    private Date birthday;

    @Enumerated(STRING)
    private Gender gender;


    public static enum Gender {
        FEMALE, MALE, UNKNOWN
    }
}
