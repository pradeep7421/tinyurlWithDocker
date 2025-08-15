package com.tinyurl.docker.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tinyurl.docker.model.ShortURLEntity;



@Repository
public interface TinyUrlRepository extends MongoRepository<ShortURLEntity,String>{

//	@Query("SELECT s FROM ShortURLEntity s WHERE s.originalUrl = :originalUrl")
//	ShortURLEntity findByOriginalUrl(@Param("originalUrl")String originalUrl);
//
//	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShortURLEntity s WHERE s.shortKey = :shortKey")
//	boolean existsById(@Param("shortKey")String key);
//
//	@Query("SELECT s FROM ShortURLEntity s WHERE s.shortKey = :shortKey")
//	Optional<ShortURLEntity> findById(@Param("shortKey")String shortKey);

	Optional<ShortURLEntity> findByShortKey(String shortKey);

    boolean existsByShortKey(String shortKey);

    Optional<ShortURLEntity> findByOriginalUrl(String originalUrl);

    boolean existsByCustomAlias(String customAlias);
}

