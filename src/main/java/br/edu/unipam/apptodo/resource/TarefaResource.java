/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo.resource;

import br.edu.unipam.apptodo.entity.Tarefa;
import br.edu.unipam.apptodo.service.TarefaService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author josereis
 */

@Path("tarefa") //domínio/contextPath/api/v1/tarefa - localhost:8081/appTodo/api/v1/tarefa
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TarefaResource {
    
    @Inject
    private TarefaService tarefaService;
    
    @Path("list") //localhost:8081/appTodo/api/v1/tarefa/list
    @GET
    public Response listar() {
        
        List<Tarefa> tarefas = tarefaService.listar();
        
        return Response.ok(tarefas).build();
    }
    
    @Path("listByUser/{id}") //localhost:8081/appTodo/api/v1/tarefa/list
    @GET
    public Response listarTarefaUsuario(@PathParam("id") long id)
    {
        List<Tarefa> tarefas = tarefaService.listarPorUsuario(id);
        return Response.ok(tarefas).build();
    }
    
    @POST
    @Path("new/{id}") //localhost:8081/appTodo/api/v1/tarefa/new/1
    public Response salvarTarefa(Tarefa tarefa, @PathParam("id") long idUser)
    {
        Tarefa tarefaSalva  = tarefaService.salvar(tarefa, idUser);
        return Response.ok(tarefaSalva).build();
    }
    
    @PUT
    @Path("update/{id}") //localhost:8081/appTodo/api/v1/tarefa/update/1
    public Response atualizarTarefa(Tarefa tarefa, @PathParam("id") long idUser)
    {
        Tarefa tarefaSalva  = tarefaService.editar(tarefa, idUser);
        return Response.ok(tarefaSalva).build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response excluirTarefa(@PathParam("id") long id)
    {
        String retorno = tarefaService.remover(id);
        if (retorno == null)
        {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Registro não encontrado").build();
    } 
    
    
}
