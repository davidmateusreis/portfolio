package com.david.portfolio.models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, message = "O nome deve ter no mínimo 3 carateres")
    private String titulo;

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dataDeLancamento;

    @NotNull
    @Size(min = 3, message = "O desenvolvedor deve ter no mínimo 3 carateres")
    private String desenvolvedor;
    @NotNull
    @Size(min = 3, message = "O gênero deve ter no mínimo 3 carateres")
    private String genero;
    @NotNull
    @Size(min = 3, message = "A região deve ter no mínimo 3 carateres")
    private String regiao;

}