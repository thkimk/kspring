package com.kkk.sbgtest.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Id;

@Document("event")
@Getter
@Setter
public class EventDoc {

//    @Id
    private String _id;

    private String title;

    private String image;

}