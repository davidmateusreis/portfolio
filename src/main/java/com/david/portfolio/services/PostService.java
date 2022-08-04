package com.david.portfolio.services;

import java.util.List;

import com.david.portfolio.models.Post;

public interface PostService {

    List<Post> findAll();
    Post findById(Long id);
    Post save(Post post);
}