package com.tinyurl.docker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinyurl.docker.model.ShortURLEntity2;



@Repository
public interface TinyUrlRepositoryMysql extends JpaRepository<ShortURLEntity2, Long>{

	@Query("SELECT s FROM ShortURLEntity2 s WHERE s.originalUrl = :originalUrl")
	ShortURLEntity2 findByOriginalUrl(@Param("originalUrl")String originalUrl);

	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShortURLEntity2 s WHERE s.shortKey = :shortKey")
	boolean existsById(@Param("shortKey")String key);

	@Query("SELECT s FROM ShortURLEntity2 s WHERE s.shortKey = :shortKey")
	Optional<ShortURLEntity2> findByShortKey(@Param("shortKey")String shortKey);

}

