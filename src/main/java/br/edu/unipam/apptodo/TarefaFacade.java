/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo;

import br.edu.unipam.entity.Tarefa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author josereis
 */
@Stateless
public class TarefaFacade extends AbstractFacade<Tarefa> {

    @PersistenceContext(unitName = "pu_todo")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TarefaFacade() {
        super(Tarefa.class);
    }
    
}
