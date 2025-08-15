package com.tinyurl.docker.model;

public class ShortURLModel {

private String apiDevKey;
private String originalUrl;
private String customAlias;
private String userName;

public String getApiDevKey() {
    return apiDevKey;
}

public void setApiDevKey(String apiDevKey) {
    this.apiDevKey = apiDevKey;
}

public String getOriginalUrl() {
    return originalUrl;
}

public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
}

public String getCustomAlias() {
    return customAlias;
}

public void setCustomAlias(String customAlias) {
    this.customAlias = customAlias;
}

public String getUserName() {
    return userName;
}

public void setUserName(String userName) {
    this.userName = userName;
}

}
