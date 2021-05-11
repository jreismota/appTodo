/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo.resource;

import br.edu.unipam.apptodo.entity.Usuario;
import br.edu.unipam.apptodo.service.UsuarioService;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author josereis
 */

@Path("usuario") //domínio/contextPath/api/v1/usuario  - localhost:8081/appTodo/api/v1/usuario
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    @GET
    @Path("list") //domínio/contextPath/api/v1/usuario/list - localhost:8081/appTodo/api/v1/usuario/list
    public Response listarTodos() {
        List<Usuario> userList = usuarioService.listarTodos();
        return Response.ok(userList).build();
    }
    
    @POST
    @Path("new")    //localhost:8081/appTodo/api/v1/usuario/new
    public Response salvarUsuario(Usuario usuario)
    {
        Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
        return Response.ok(usuarioSalvo).build();
    }

//    public Response save(Usuario user) {
//        usuarioService.salvarUsuario(user);
//        return Response.ok(user).build();
//        Response response = Response.created(uriInfo.getAbsolutePath()).
//                status(Response.Status.ACCEPTED).
//                entity(user).
//                build();

        //return response;
        
    //}

}
