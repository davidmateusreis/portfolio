package com.david.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.portfolio.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Usuario findByUsername(String username);
}