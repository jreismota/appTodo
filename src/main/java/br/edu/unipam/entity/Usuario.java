/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author josereis
 */
@Entity
@NamedQuery(name =  Usuario.GET_ALL_USERS, query = "select u from Usuario u order by u.nome")
public class Usuario extends AbstractEntity implements Serializable {

    public static final String GET_ALL_USERS = "Usuario.getAllUsers";

    @NotEmpty
    @NotNull
    private String nome;
    
    @NotEmpty (message = "E-mail é obrigatório")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Formato de e-mail inválido")
    @Column(nullable = false)
    private String email;

    @OneToMany
    private Collection<Tarefa> tarefas = new ArrayList<>();

    public Collection<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(Collection<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

   public String toString() {
    return id + ", " + nome;
}
    
}
