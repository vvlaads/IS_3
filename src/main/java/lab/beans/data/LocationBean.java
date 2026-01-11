package lab.beans.data;

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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named("locationBean")
@SessionScoped
public class LocationBean implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private List<Location> locationList;
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
        locationList = databaseManager.getObjectList(Location.class);
        locationList = locationList.stream()
                .sorted(Comparator.comparing(Location::getId))
                .collect(Collectors.toList());
    }

    public List<Location> getLocationList() {
        if (locationList == null) {
            locationList = databaseManager.getObjectList(Location.class);
        }
        return locationList;
    }
}
