package com.kkk.sbgtest.controller;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class RestfulController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("restTemplate2")
    RestTemplate restTemplate2;

    public RestfulController() {
    }

    @GetMapping("/rest1")
    public void rest1() {
        System.out.println("## rest1() : begins.."+ restTemplate.toString());
        try {
            URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8091).setPath("/api/rest/test1").build();
            String lRes = restTemplate.getForObject(uri, String.class);
            System.out.println("## rest1() : " + lRes);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/rest2")
    public void rest2() {
        System.out.println("## rest2() : begins.."+ restTemplate2.toString());
        try {
            URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8091).setPath("/api/rest/test1").build();
            String lRes = restTemplate2.getForObject(uri, String.class);
            System.out.println("## rest2() : " + lRes);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
