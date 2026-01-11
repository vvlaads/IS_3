package lab.beans.dialogs;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.UpdateBean;
import lab.data.Coordinates;
import lab.database.DatabaseManager;

import java.io.Serializable;

@Named("coordinatesDialogBean")
@SessionScoped
public class CoordinatesDialogBean implements Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private Coordinates coordinates = new Coordinates();

    private boolean editing = false;

    public void openAddDialog() {
        coordinates = new Coordinates();
        editing = false;
    }

    public void openEditDialog(int id) {
        coordinates = databaseManager.getObjectById(Coordinates.class, id);
        editing = true;
    }

    public void addCoordinates() {
        databaseManager.addObject(coordinates);
        updateBean.increaseVersion();
    }

    public void updateCoordinates() {
        databaseManager.updateObject(coordinates);
        updateBean.increaseVersion();
    }


    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
