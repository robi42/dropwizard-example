package com.example.helloworld.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Saying {

    @JsonProperty
    private long id;

    @JsonProperty
    @Length(max = 3)
    private String content;
}
