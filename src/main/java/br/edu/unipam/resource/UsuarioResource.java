/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.resource;

import br.edu.unipam.entity.Usuario;
import br.edu.unipam.service.UsuarioService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author josereis
 */
@Path("usuario") //domínio/contextPath/api/v1/usuario
public class UsuarioResource {
    
    @Inject
    private UsuarioService usuarioService;
    
    @GET
    @Path("list") //domínio/contextPath/api/v1/usuario/list
    public List<Usuario> listarTodos()
    {
        List<Usuario> userList = usuarioService.listarTodos();
        return userList;
    }
    
}
