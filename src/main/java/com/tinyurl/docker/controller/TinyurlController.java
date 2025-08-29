package com.tinyurl.docker.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyurl.docker.model.ShortURLEntity;
import com.tinyurl.docker.model.ShortURLModel;
import com.tinyurl.docker.service.TinyUrlService;


@RestController
@RequestMapping("${controller1.sub-path}")
public class TinyurlController {
	@Resource
	private TinyUrlService tinyUrlService;

	@GetMapping
	public RedirectView redirectToForm() {
		return new RedirectView("/tinyurl/TinyurlForm.html");
	}

	// For JSON API clients
	@PostMapping(value = "/createUrl", consumes = "application/json")
	public ResponseEntity<String> createUrlJson(@RequestBody ShortURLModel shortUrlModel)
			throws NoSuchAlgorithmException {
		System.out.println("JSON ---> " + shortUrlModel);
		try {
			String key = tinyUrlService.createShortUrl(shortUrlModel);
			return ResponseEntity.ok("http://localhost:9999/" + key);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create short URL: " + e.getMessage());
		}
	}

	// For HTML form submissions
	@PostMapping(value = "/createUrl", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> createUrlForm(@ModelAttribute ShortURLModel shortUrlModel)
			throws NoSuchAlgorithmException {
		System.out.println("Form ---> " + shortUrlModel);
		try {
			String key = tinyUrlService.createShortUrl(shortUrlModel);
			return ResponseEntity.ok("http://localhost:9999/" + key);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create short URL: " + e.getMessage());
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createURL(@RequestBody ShortURLModel shortUrlModel) throws NoSuchAlgorithmException {
		System.out.println("Controller --->" + shortUrlModel);
		try {
		String key = tinyUrlService.createShortUrl(shortUrlModel);
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
		String originalUrl = tinyUrlService.getOriginalUrl(shortKey);
		return ResponseEntity.ok(originalUrl);
//		return ResponseEntity.ok("https://www.geeksforgeeks.org/");
	}
	@GetMapping("/findAllEntity")
	public ResponseEntity<List<ShortURLEntity>> getAllUrls() {
        return ResponseEntity.ok(tinyUrlService.getAllUrls());
    } 
	
	@PostMapping("/createUrlDualType")
	public ResponseEntity<String> createURL(
	        HttpServletRequest request,
	       @ModelAttribute ShortURLModel formBody) throws NoSuchAlgorithmException, IOException {

		 ShortURLModel shortUrlModel = formBody;

		    // Manual JSON handling if content-type is application/json
		    if ("application/json".equals(request.getContentType())) {
		        String json = request.getReader().lines().reduce("", (acc, line) -> acc + line);
		        shortUrlModel = new ObjectMapper().readValue(json, ShortURLModel.class);
		    }

		    String key = tinyUrlService.createShortUrl(shortUrlModel);
		    return ResponseEntity.ok("http://localhost:9999/" + key);
	}
}
