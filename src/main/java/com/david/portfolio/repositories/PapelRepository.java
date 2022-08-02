package com.david.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.portfolio.models.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {
    
    Papel findByPapel(String papel);
}