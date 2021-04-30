/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo;

import br.edu.unipam.apptodo.util.JsfUtil;
import br.edu.unipam.entity.Usuario;
import br.edu.unipam.service.UsuarioService;
import java.io.Serializable;
import java.util.Enumeration;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named("autenticacao")

@SessionScoped
public class AutenticacaoController implements Serializable {

    @Inject
    private UsuarioService usuarioService;
    private String email, senha;
    private Usuario usuario;

    public AutenticacaoController() {
    }

    
    public String efetuarLogin() {
        System.out.println("efetuando login " + email + " " + senha);

        try {
        usuario = usuarioService.verificarLogin(email, senha);
        }catch (Exception e)
        {
            JsfUtil.addErrorMessage("Usuário não encontrado");
            return "login.xhtml";
        }
        

        if (usuario == null) {
            JsfUtil.addErrorMessage("Login ou senha não correspondem");
            return "login.xhtml";
        } else {
            // USE MD5!!!
            if (senha.equals(usuario.getSenha())) {

                HttpSession session;

                FacesContext ctx = FacesContext.getCurrentInstance();
                session = (HttpSession) ctx.getExternalContext().getSession(false);
                session.setAttribute("usuarioAutenticado", usuario);

                return "index.xhtml";
            } else {
                JsfUtil.addErrorMessage("Login ou senha não correspondem");
                return "login.xhtml";
            }
        }

    }

    public String logout() {
        HttpSession session;
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        session = (HttpSession) ctx.getExternalContext().getSession(false);
        session.setAttribute("usuarioAutenticado", null);

        Enumeration<String> vals = session.getAttributeNames();

        while (vals.hasMoreElements()) {
            session.removeAttribute(vals.nextElement());
        }
        JsfUtil.addSuccessMessage("Usuário deslogado");
        //return "/faces/login?faces-redirect=true";
        return "login";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
