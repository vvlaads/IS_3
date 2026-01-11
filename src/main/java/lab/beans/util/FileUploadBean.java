package lab.beans.util;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.data.util.Operation;
import lab.database.DatabaseManager;
import org.primefaces.model.file.UploadedFile;

@Named("fileUploadBean")
@RequestScoped
public class FileUploadBean {
    @EJB
    private DatabaseManager databaseManager;

    @Inject
    private UpdateBean updateBean;
    private UploadedFile file;

    public void upload() {
        try {
            if (file == null) {
                showError("Не выбран файл");
                return;
            }

            byte[] fileContent = file.getContent();
            String fileName = file.getFileName();

            if (fileName == null || !fileName.toLowerCase().endsWith(".json")) {
                showError("Неверный тип файла");
                return;
            }
            String sessionId = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionId(true);
            Operation operation = new Operation();
            operation.setUsername(sessionId);

            int result = databaseManager.importObjects(fileContent);
            if (result > 0) {
                showMessage("Успешно выполнено", "Добавлено " + result + " объектов");
                operation.setCompleted(true);
                operation.setCount(result);
            } else {
                showError("Ошибка при добавлении объектов");
                operation.setCompleted(false);
                operation.setCount(0);
            }
            databaseManager.addObject(operation);
            updateBean.increaseVersion();
        } catch (Exception e) {
            showError("Внутренняя ошибка: " + e.getMessage());
        }
    }

    private void showMessage(String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void showError(String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", detail));
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
