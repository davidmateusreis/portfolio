package com.david.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.portfolio.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}