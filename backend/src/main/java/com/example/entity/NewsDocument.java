package com.example.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.util.Date;

@MongoEntity(collection = "news")
public class NewsDocument extends PanacheMongoEntity {
    public String title;
    public String catchLine;
    public Boolean isEvent;
    public String language;
    public String status;
    public String userCreated;
    public String userLastModified;
    public Date dateCreated;
    public Date dateLastModified;
}
