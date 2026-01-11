package lab.beans.profiles;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Location;
import lab.database.DatabaseManager;

import java.io.Serializable;

@Named("locationProfileBean")
@SessionScoped
public class LocationProfileBean implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    private long lastKnownVersion = -1;
    @Inject
    private UpdateBean updateBean;

    private int id;
    private Location location;

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
        location = databaseManager.getObjectById(Location.class, id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        updateTable();
    }

    public Location getLocation() {
        return location;
    }
}
