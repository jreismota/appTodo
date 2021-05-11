package br.edu.unipam.apptodo;

import br.edu.unipam.apptodo.entity.Perfil;
import br.edu.unipam.apptodo.service.PerfilService;
import br.edu.unipam.apptodo.util.JsfUtil;
import br.edu.unipam.apptodo.util.JsfUtil.PersistAction;

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

@Named("perfilController")
@SessionScoped
public class PerfilController implements Serializable {

    @Inject
    private PerfilService perfilService;
    private List<Perfil> items = null;
    private Perfil selected;

    public PerfilController() {
    }

    public Perfil getSelected() {
        return selected;
    }

    public void setSelected(Perfil selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }


    public Perfil prepareCreate() {
        selected = new Perfil();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PerfilCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PerfilUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PerfilDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Perfil> getItems() {
        if (items == null) {
            items = perfilService.listarTodos();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == persistAction.CREATE){
                    selected = perfilService.salvarPerfil(selected);
                }
                else if (persistAction != PersistAction.DELETE) {
                    selected = perfilService.editar(selected);
                } else {
                    perfilService.remover(selected.getId());
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

    public Perfil getPerfil(java.lang.Integer id) {
        return perfilService.localizarPorId(id);
    }

    public List<Perfil> getItemsAvailableSelectMany() {
                return perfilService.listarTodos();
    }

    public List<Perfil> getItemsAvailableSelectOne() {
                return perfilService.listarTodos();
    }

    @FacesConverter(forClass = Perfil.class)
    public static class PerfilControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PerfilController controller = (PerfilController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "perfilController");
            return controller.getPerfil(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Perfil) {
                Perfil o = (Perfil) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Perfil.class.getName()});
                return null;
            }
        }

    }

}
