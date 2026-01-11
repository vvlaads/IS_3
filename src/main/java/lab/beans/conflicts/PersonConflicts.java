package lab.beans.conflicts;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.Updatable;
import lab.beans.util.UpdateBean;
import lab.data.Movie;
import lab.data.Person;
import lab.database.DatabaseManager;

import java.io.Serializable;
import java.util.List;

@Named("personConflicts")
@SessionScoped
public class PersonConflicts implements Updatable, Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private int personId;
    private List<Movie> conflictMovieList;
    private long lastKnownVersion;
    private boolean allowed = false;

    @PostConstruct
    public void init() {
        lastKnownVersion = updateBean.getVersion();
        updateTable();
    }

    public void findMoviesWithConflicts(int personId) {
        this.personId = personId;
        updateTable();
    }

    public void deletePerson() {
        if (allowed) {
            databaseManager.deleteObject(Person.class, personId);
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
        conflictMovieList = databaseManager.findMoviesByPersonId(personId);
        allowed = conflictMovieList.isEmpty();
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
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
