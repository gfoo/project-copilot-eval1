package org.gfoo.entity;

import java.util.Date;
import org.bson.codecs.pojo.annotations.BsonId;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "news")
public class NewsDocument extends PanacheMongoEntityBase {
    @BsonId
    public String id;
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
