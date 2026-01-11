package lab.beans.data;

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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named("coordinatesBean")
@SessionScoped
public class CoordinatesBean implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private List<Coordinates> coordinatesList;
    private long lastKnownVersion = -1;


    @PostConstruct
    public void init() {
        lastKnownVersion = updateBean.getVersion();
        updateTable();
    }

    public void checkForUpdates() {
        long currentVersion = updateBean.getVersion();
        if (currentVersion != lastKnownVersion) {
            lastKnownVersion = currentVersion;
            updateTable();
        }
    }

    public void updateTable() {
        coordinatesList = databaseManager.getObjectList(Coordinates.class);
        coordinatesList = coordinatesList.stream()
                .sorted(Comparator.comparing(Coordinates::getId))
                .collect(Collectors.toList());
    }

    public List<Coordinates> getCoordinatesList() {
        if (coordinatesList == null) {
            coordinatesList = databaseManager.getObjectList(Coordinates.class);
        }
        return coordinatesList;
    }
}
