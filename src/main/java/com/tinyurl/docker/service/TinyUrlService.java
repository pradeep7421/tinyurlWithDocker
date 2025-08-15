package com.tinyurl.docker.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinyurl.docker.model.ShortURLEntity;
import com.tinyurl.docker.model.ShortURLModel;
import com.tinyurl.docker.repository.TinyUrlRepository;

@Service
public class TinyUrlService {
	@Autowired
	private TinyUrlRepository tinyUrlRepository;

	public String createShortUrl(ShortURLModel shortUrlModel) throws NoSuchAlgorithmException {

		String customAlias = shortUrlModel.getCustomAlias();
		boolean isCustomAliasValid = customAlias != null && !customAlias.isEmpty();

		byte[] hash = MessageDigest.getInstance("SHA-256")
				.digest(shortUrlModel.getOriginalUrl().getBytes(StandardCharsets.UTF_8));
		String key = Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 6);
		// Boolean isKeyExist = tinyUrlRepository.existsById(key);
		Boolean isKeyExist = tinyUrlRepository.existsByShortKey(key);
//		if (isCustomAliasValid) {
			if (isKeyExist) {
				throw new RuntimeException("Custom alias or key already taken.");
//			}

		} else {

			ShortURLEntity shortUrlEntity = new ShortURLEntity();

			shortUrlEntity.setOriginalUrl(shortUrlModel.getOriginalUrl());
			shortUrlEntity.setApiDevKey(shortUrlModel.getApiDevKey());
			shortUrlEntity.setCustomAlias(shortUrlModel.getCustomAlias());
			shortUrlEntity.setUserName(shortUrlModel.getUserName());
			shortUrlEntity.setExpireDate(new Date());
			shortUrlEntity.setShortKey(key);

			tinyUrlRepository.save(shortUrlEntity);
		}

		return key;
	}

	// public ShortURLEntity getShortURL(ShortURLModel shortUrlModel) {
	// ShortURLEntity shortURLEntity =
	// tinyUrlRepository.findByOriginalUrl(shortUrlModel.getOriginalUrl());
	// return shortURLEntity;
	// }

	public String getOriginalUrl(String shortKey) {
		// ShortURLEntity entity = tinyUrlRepository.findById(shortKey)
		ShortURLEntity entity = tinyUrlRepository.findByShortKey(shortKey)
				.orElseThrow(() -> new RuntimeException("Short URL not found."));

		return entity.getOriginalUrl();
		// return "faceebook.com";
	}
	
	  public List<ShortURLEntity> getAllUrls() {
	        return tinyUrlRepository.findAll();
	    }

}
