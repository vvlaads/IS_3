package lab.beans.conflicts;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Coordinates;
import lab.data.Movie;
import lab.database.DatabaseManager;

import java.io.Serializable;
import java.util.List;

@Named("coordinatesConflicts")
@SessionScoped
public class CoordinatesConflicts implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private int coordinatesId;
    private List<Movie> conflictMovieList;
    private long lastKnownVersion;
    private boolean allowed = false;

    @PostConstruct
    public void init() {
        lastKnownVersion = updateBean.getVersion();
        updateTable();
    }

    public void findMoviesWithConflicts(int coordinatesId) {
        this.coordinatesId = coordinatesId;
        updateTable();
    }

    public void deleteCoordinates() {
        if (allowed) {
            databaseManager.deleteObject(Coordinates.class, coordinatesId);
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
        conflictMovieList = databaseManager.findMoviesByCoordinatesId(coordinatesId);
        allowed = conflictMovieList.isEmpty();
    }

    public void setCoordinatesId(int coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public int getCoordinatesId() {
        return coordinatesId;
    }

    public List<Movie> getConflictMovieList() {
        return conflictMovieList;
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
