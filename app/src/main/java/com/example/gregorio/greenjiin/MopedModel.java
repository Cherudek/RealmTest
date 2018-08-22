package com.example.gregorio.greenjiin;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import java.util.Date;
import java.util.UUID;

public class MopedModel extends RealmObject {

  @PrimaryKey
  @Required
  private String mopedId;
  @Required
  private String mopedName;
  @Required
  private String mopedUrl;
  @Required
  private Date timestamp;

  public MopedModel() {
    this.mopedId = UUID.randomUUID().toString();
    this.mopedName = "";
    this.mopedUrl = "";
    this.timestamp = new Date();
  }

  public String getMopedId() {
    return mopedId;
  }

  public void setMopedId(String mopedId) {
    this.mopedId = mopedId;
  }

  public String getMopedName() {
    return mopedName;
  }

  public void setMopedName(String mopedName) {
    this.mopedName = mopedName;
  }

  public String getMopedUrl() {
    return mopedUrl;
  }

  public void setMopedUrl(String mopedUrl) {
    this.mopedUrl = mopedUrl;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
