/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author josereis
 */
@Entity
public class Tarefa extends AbstractEntity implements Serializable {

    @NotNull
    @Size(min = 5, max = 100)
    private String Descricao;
    
    @NotNull
    @JsonbDateFormat(locale = "yyyy-mm-dd")
    @Temporal(TemporalType.DATE)
    private Date DataPrevista;
    
    @JsonbDateFormat(locale = "yyyy-mm-dd")
    @Temporal(TemporalType.DATE)
    private Date DataTermino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IdUsuario")
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public Date getDataPrevista() {
        return DataPrevista;
    }

    public void setDataPrevista(Date DataPrevista) {
        this.DataPrevista = DataPrevista;
    }

    public Date getDataTermino() {
        return DataTermino;
    }

    public void setDataTermino(Date DataTermino) {
        this.DataTermino = DataTermino;
    }

  
}
