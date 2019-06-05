package com.zhaoguhong.baymax.mongo.entity;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mongodemo")
@Data
public class MongoDemo implements Serializable {

  @Id
  private String id;

  private String title;

  private String description;

}
