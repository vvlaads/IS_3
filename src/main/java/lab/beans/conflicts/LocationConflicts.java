package lab.beans.conflicts;

import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Location;
import lab.data.Person;
import lab.database.DatabaseManager;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named("locationConflicts")
@SessionScoped
public class LocationConflicts implements Updatable, Serializable {
    @Inject
    private DatabaseManager databaseManager;
    private int locationId;
    private List<Person> conflictPersonList;
    private UpdateBean updateBean;
    private long lastKnownVersion;
    private boolean allowed = false;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        updateBean = context.getApplication()
                .evaluateExpressionGet(context, "#{updateBean}", UpdateBean.class);
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
