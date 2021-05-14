package br.edu.unipam.apptodo;

import br.edu.unipam.apptodo.entity.Tarefa;
import br.edu.unipam.apptodo.report.TarefaReport;
import br.edu.unipam.apptodo.util.JsfUtil;
import br.edu.unipam.apptodo.util.JsfUtil.PersistAction;
import br.edu.unipam.apptodo.service.TarefaService;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;

@Named("tarefaController")
@SessionScoped
public class TarefaController implements Serializable {

//    @EJB private br.edu.unipam.apptodo.TarefaFacade ejbFacade;
    @Inject
    private TarefaService tarefaService;
    private List<Tarefa> items = null;
    private Tarefa selected;

    private ExcelOptions excelOpt;

    public ExcelOptions getExcelOpt() {
        return excelOpt;
    }

    public void setExcelOpt(ExcelOptions excelOpt) {
        this.excelOpt = excelOpt;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }

    private PDFOptions pdfOpt;

    @PostConstruct
    public void customizeOptions() {
        excelOpt = new ExcelOptions();
        excelOpt.setFacetBgColor("#F88017");
        excelOpt.setFacetFontSize("10");
        excelOpt.setFacetFontColor("#0000ff");
        excelOpt.setFacetFontStyle("BOLD");
        excelOpt.setCellFontColor("#00ff00");
        excelOpt.setCellFontSize("8");
        excelOpt.setFontName("Verdana");

        pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#F88017");
        pdfOpt.setFacetFontColor("#0000ff");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontSize("12");
        pdfOpt.setFontName("Courier");
        pdfOpt.setOrientation(PDFOrientationType.LANDSCAPE);
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException, Exception {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "unipam_logo_retangular.png";

        Image logoImg = Image.getInstance(logo);

        logoImg.scaleAbsolute(50, 50);

        pdf.add(logoImg);
    }

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
                if (persistAction == persistAction.CREATE) {
                    selected = tarefaService.salvar(selected, selected.getUsuario().getId());
                } else if (persistAction != PersistAction.DELETE) {
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

    @FacesConverter(forClass = Tarefa.class)
    public static class TarefaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TarefaController controller = (TarefaController) facesContext.getApplication().getELResolver().
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

    public void createCustomPdf() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        try {

            // step 1
            Document document = new Document();                   

            // step 2
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            // step 3
            document.open();
            // step 4
            TarefaReport.addMetaData(document);
            TarefaReport.addTitlePage(document);
            TarefaReport.addContent(document);
            
            //document.add(new Paragraph(text));
            // step 5
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                    "must-revalidate");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-disposition", "attachment; filename=TESTE.pdf");
            //response.setHeader("Content-disposition", "inline=filename=file.pdf");
            // setting the content type
            response.setContentType("application/pdf");

            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (DocumentException e) {
             System.out.println("br.edu.unipam.apptodo.TarefaController.createPDF()" + e);
        } catch (IOException e) {
            System.out.println("br.edu.unipam.apptodo.TarefaController.createPDF()" + e);
        } catch (Exception ex) {
            System.out.println("br.edu.unipam.apptodo.TarefaController.createPDF()" + ex.getMessage());
        }
        context.responseComplete();
            System.out.println("br.edu.unipam.apptodo.TarefaController.createPDF() context.responseComplete()");
    }

}
