package lab.beans.dialogs;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.UpdateBean;
import lab.data.Location;
import lab.database.DatabaseManager;

import java.io.Serializable;

@Named("locationDialogBean")
@SessionScoped
public class LocationDialogBean implements Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private Location location = new Location();

    private boolean editing = false;

    public void openAddDialog() {
        location = new Location();
        editing = false;
    }

    public void openEditDialog(int id) {
        location = databaseManager.getObjectById(Location.class, id);
        editing = true;
    }

    public void addLocation() {
        databaseManager.addObject(location);
        updateBean.increaseVersion();
    }

    public void updateLocation() {
        databaseManager.updateObject(location);
        updateBean.increaseVersion();
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
