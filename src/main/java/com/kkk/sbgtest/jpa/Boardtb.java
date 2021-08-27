package com.kkk.sbgtest.jpa;


import lombok.Getter;
import lombok.Setter;

//import javax.persistence.*;
import java.io.Serializable;

//@Entity
//@Table(name = "board")
//@Getter
//@Setter
public class Boardtb implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

//    @Column(length = 100)
    private String title;

//    @Column(length = 4000)
    private String contents;

}