package com.kkk.sbgtest.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDBRepository extends MongoRepository<MongoModel, String> {
    public MongoModel findByName(String name);
}


