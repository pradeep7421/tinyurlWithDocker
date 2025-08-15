package com.tinyurl.docker.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//@Entity
//@Table(name="urlTable")
@Document(collection = "urlTable")
public class ShortURLEntity {

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String urlId;
//	@Column
	 @Field("apiDevKey")
	private String apiDevKey;
//	@Column
	 @Field("originalUrl")
	private String originalUrl;
//	@Column
	 @Field("customAlias")
	private String customAlias;
//	@Column
	 @Field("userName")
	private String userName;
//	@Column
	 @Field("expireDate")
	private Date expireDate;
//	@Column
	 @Field("shortKey")
	private String shortKey;
	
	
	public String getShortKey() {
		return shortKey;
	}
	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}
	public String getUrlId() {
		return urlId;
	}
	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}
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
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShortURLEntity [urlId=");
		builder.append(urlId);
		builder.append(", apiDevKey=");
		builder.append(apiDevKey);
		builder.append(", originalUrl=");
		builder.append(originalUrl);
		builder.append(", customAlias=");
		builder.append(customAlias);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", expireDate=");
		builder.append(expireDate);
		builder.append(", shortKey=");
		builder.append(shortKey);
		builder.append("]");
		return builder.toString();
	}


	
}
