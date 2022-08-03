package com.david.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.portfolio.models.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
    
}