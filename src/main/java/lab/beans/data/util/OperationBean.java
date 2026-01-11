package lab.beans.data.util;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.util.Operation;
import lab.database.DatabaseManager;

import java.io.Serializable;
import java.util.List;

@Named("operationBean")
@SessionScoped
public class OperationBean implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private List<Operation> operationList;
    private long lastKnownVersion = -1;


    @PostConstruct
    public void init() {
        lastKnownVersion = updateBean.getVersion();
        updateTable();
    }

    @Override
    public void checkForUpdates() {
        long currentVersion = updateBean.getVersion();
        if (currentVersion != lastKnownVersion) {
            lastKnownVersion = currentVersion;
            updateTable();
        }
    }

    @Override
    public void updateTable() {
        operationList = databaseManager.getObjectList(Operation.class);
        System.out.println("New Operation list: " + operationList.size() + "\n\n\n");
    }

    public List<Operation> getOperationList() {
        if (operationList == null) {
            operationList = databaseManager.getObjectList(Operation.class);
        }
        return operationList;
    }
}
