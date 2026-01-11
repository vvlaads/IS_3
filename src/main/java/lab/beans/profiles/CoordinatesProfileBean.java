package lab.beans.profiles;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Coordinates;
import lab.database.DatabaseManager;

import java.io.Serializable;

@Named("coordinatesProfileBean")
@SessionScoped
public class CoordinatesProfileBean implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private long lastKnownVersion = -1;

    private int id;
    private Coordinates coordinates;

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
        coordinates = databaseManager.getObjectById(Coordinates.class, id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        updateTable();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
