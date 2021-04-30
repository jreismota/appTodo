package br.edu.unipam.apptodo.util;

import br.edu.unipam.entity.Usuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

public class AutenticacaoPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        String nomePagina = fc.getViewRoot().getViewId();

        System.out.println(nomePagina);

        if ("/login.xhtml".equals(nomePagina)) {
            return;
        }

        ExternalContext ec = fc.getExternalContext();

        if (!fc.getViewRoot().getViewId().contains("login.xhtml")) {
            HttpSession session = (HttpSession) ec.getSession(true);
            Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

            if (usuario == null) {
                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(AutenticacaoPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}


/**
 * A API do JSF define o as fases como etapas do ciclo de vida do framework.
Existem 6 fases no JSF, começando nesta sequência:
1.	Restore View
2.	Apply Request Values
3.	Process Validators
4.	Update Model
5.	Invoke Application
6.	Render Response
Os PhaseListener são objetos que serão notificados no início e término de cada etapa.
Normalmente são usados como “interceptadores”, isto é, você quer interceptar uma requisição para realizar algo específico, como validar o login do usuário no sistema, por exemplo.
Quando você implementa o PhaseListener, você obrigatoriamente precisa obedecer o contrato no qual garante os métodos:
[code]
afterPhase(javax.faces.event.PhaseEvent event)
// Manipula uma notificação que está procesando uma fase do ciclo de vida JSF quando este já completou.
void beforePhase(javax.faces.event.PhaseEvent event);
// Manipula uma notificação que está procesando uma fase do ciclo de vida JSF quando este está prestes a iniciar.
javax.faces.event.PhaseId getPhaseId();
// Retorna o Tipo de Fase no JSF que o objeto será notificado. Pode ser qualquer uma das 6 fases, ou todas as fases.[/code]

* Disponível em: https://www.guj.com.br/t/explicacao-sobre-phaselistener/152986/2. Acesso em 29 abr. 2021

 */