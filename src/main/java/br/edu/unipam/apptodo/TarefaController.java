package br.edu.unipam.apptodo;

import br.edu.unipam.entity.Tarefa;
import br.edu.unipam.apptodo.util.JsfUtil;
import br.edu.unipam.apptodo.util.JsfUtil.PersistAction;
import br.edu.unipam.service.TarefaService;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;


@Named("tarefaController")
@SessionScoped
public class TarefaController implements Serializable {


//    @EJB private br.edu.unipam.apptodo.TarefaFacade ejbFacade;
    @Inject
    private TarefaService tarefaService;
    private List<Tarefa> items = null;
    private Tarefa selected;

    public TarefaController() {
    }

    public Tarefa getSelected() {
        return selected;
    }

    public void setSelected(Tarefa selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

//    private TarefaFacade getFacade() {
//        return ejbFacade;
//    }

    public Tarefa prepareCreate() {
        selected = new Tarefa();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TarefaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TarefaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TarefaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Tarefa> getItems() {
        if (items == null) {
            items = tarefaService.listar();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == persistAction.CREATE)
                    selected = tarefaService.salvar(selected, selected.getUsuario().getId());
                else if (persistAction != PersistAction.DELETE) {
                    selected = tarefaService.editar(selected, selected.getUsuario().getId());
                } else {
                    tarefaService.remover(selected.getId());
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Tarefa getTarefa(java.lang.Long id) {
        return tarefaService.localizarPorId(id);
    }

    public List<Tarefa> getItemsAvailableSelectMany() {
        return tarefaService.listar();
    }

    public List<Tarefa> getItemsAvailableSelectOne() {
        return tarefaService.listar();
    }

    @FacesConverter(forClass=Tarefa.class)
    public static class TarefaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TarefaController controller = (TarefaController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tarefaController");
            return controller.getTarefa(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Tarefa) {
                Tarefa o = (Tarefa) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Tarefa.class.getName()});
                return null;
            }
        }

    }

}
