package com.tinyurl.docker.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinyurl.docker.model.ShortURLEntity2;
import com.tinyurl.docker.model.ShortURLModel;
import com.tinyurl.docker.service.TinyUrlService2;


@RestController
@RequestMapping("${controller2.sub-path}")
public class TinyurlController2 {
	@Resource
	private TinyUrlService2 tinyUrlService2;

	@PostMapping("/create")
	public ResponseEntity<String> createURL(@RequestBody ShortURLModel shortUrlModel) throws NoSuchAlgorithmException {
		System.out.println("Controller --->" + shortUrlModel);
		try {
		String key = tinyUrlService2.createShortUrl(shortUrlModel);
//			String key= "uq1wr";
		return ResponseEntity.ok("http://localhost:9999/" + key);
		}catch(RuntimeException e) {
			String errorMessage = "Failed to create short URL  as key or custom alis already exists: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}
	
	@GetMapping("/getOriginalUrl/{shortKey}")
	public ResponseEntity<String> getOriginalUrlByShortKey(@PathVariable("shortKey") String shortKey) {
		System.out.println("Controller --->" + shortKey);
		String originalUrl = tinyUrlService2.getOriginalUrl(shortKey);
		return ResponseEntity.ok(originalUrl);
//		return ResponseEntity.ok("https://www.geeksforgeeks.org/");
	}
	@GetMapping("/findAllEntity")
	public ResponseEntity<List<ShortURLEntity2>> getAllUrls() {
        return ResponseEntity.ok(tinyUrlService2.getAllUrls());
    } 
}
