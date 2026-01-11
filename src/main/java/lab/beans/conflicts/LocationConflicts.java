package lab.beans.conflicts;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Location;
import lab.data.Person;
import lab.database.DatabaseManager;

import java.io.Serializable;
import java.util.List;

@Named("locationConflicts")
@SessionScoped
public class LocationConflicts implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private int locationId;
    private List<Person> conflictPersonList;
    private long lastKnownVersion;
    private boolean allowed = false;

    @PostConstruct
    public void init() {
        lastKnownVersion = updateBean.getVersion();
        updateTable();
    }

    public void findPersonsWithConflicts(int locationId) {
        this.locationId = locationId;
        updateTable();
    }

    public void deleteLocation() {
        if (allowed) {
            databaseManager.deleteObject(Location.class, locationId);
            updateBean.increaseVersion();
        }
    }

    @Override
    public void checkForUpdates() {
        if (lastKnownVersion != updateBean.getVersion()) {
            lastKnownVersion = updateBean.getVersion();
            updateTable();
        }
    }

    @Override
    public void updateTable() {
        conflictPersonList = databaseManager.findPersonsByLocationId(locationId);
        allowed = conflictPersonList.isEmpty();
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public List<Person> getConflictPersonList() {
        return conflictPersonList;
    }

    public long getLastKnownVersion() {
        return lastKnownVersion;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
