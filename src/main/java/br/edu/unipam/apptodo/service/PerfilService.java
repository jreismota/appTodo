/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo.service;

import br.edu.unipam.apptodo.entity.Perfil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author josereis
 */
@Transactional
public class PerfilService  implements Serializable {

    @PersistenceContext(name = "pu_todo")
    private EntityManager entityManager;

    //inserir
    public Perfil salvarPerfil(Perfil perfil) {
        entityManager.persist(perfil);
        return perfil;
    }

    //Encontrar usuário por ID
    public Perfil localizarPorId(int id) {
        Perfil perfilBd = entityManager.find(Perfil.class, id);
        return perfilBd;
    }

    //Remover
    public void remover(int id) {
        Perfil perfil = localizarPorId(id);
        if (perfil != null) {
//            entityManager.remove(perfil);
            Query q = entityManager.createNativeQuery("Delete from perfil u where u.id = ?");
            q.setParameter(1, id);
            q.executeUpdate();
        }
    }

    //Editar
    public Perfil editar(Perfil perfil) {
        Perfil perfilBd = localizarPorId(perfil.getId());

        if (perfilBd != null) {
            entityManager.merge(perfil);
            return perfil;
        }
        return null;
    }

    //Listar Todos os usuários
    public List<Perfil> listarTodos ()
    {
        return entityManager.createQuery("select u from Perfil u order by u.nome", Perfil.class).getResultList();
    }
    
}
