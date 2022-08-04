package com.david.portfolio.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.portfolio.models.Post;
import com.david.portfolio.repositories.PostRepository;
import com.david.portfolio.services.PostService;

@Service
public class PostServiceImplement implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> findAll() {

        return postRepository.findAll();
    }

    @Override
    public Post findById(Long id) {

        return postRepository.findById(id).get();
    }

    @Override
    public Post save(Post post) {

        return postRepository.save(post);
    }

}