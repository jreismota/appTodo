/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.service;

import br.edu.unipam.entity.Usuario;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author josereis
 */
@Transactional
public class UsuarioService  implements Serializable {

    @PersistenceContext(name = "pu_todo")
    private EntityManager entityManager;

    //inserir
    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setDataCriacao(new Date());
        entityManager.persist(usuario);
        return usuario;
    }

    //Encontrar usuário por ID
    public Usuario localizarPorId(Long id) {
        Usuario userBd = entityManager.find(Usuario.class, id);
        return userBd;
    }

    //Remover
    public void remover(Long id) {
        Usuario user = localizarPorId(id);
        if (user != null) {
//            entityManager.remove(user);
            Query q = entityManager.createNativeQuery("Delete from usuario u where u.id = ?");
            q.setParameter(1, id);
            q.executeUpdate();
        }
    }

    //Editar
    public Usuario editar(Usuario usuario) {
        Usuario userBd = localizarPorId(usuario.getId());

        if (userBd != null) {
            usuario.setDataAlteracao(new Date());
            entityManager.merge(usuario);
            return usuario;
        }
        return null;
    }

    //Listar Todos os usuários
    public List<Usuario> listarTodos ()
    {
        //return entityManager.createQuery("select u from Usuario u order by u.nome", Usuario.class).getResultList();
        return entityManager.createNamedQuery(Usuario.GET_ALL_USERS, Usuario.class).getResultList();
    }
    
    //ToDo: Salvar usuario
}
