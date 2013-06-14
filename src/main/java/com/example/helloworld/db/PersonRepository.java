package com.example.helloworld.db;

import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;

import java.util.List;
import java.util.UUID;

public interface PersonRepository {

    Person create(Person person);

    Optional<Person> findById(UUID id);

    List<Person> findAll();
}
