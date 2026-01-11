package lab.beans.util;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.conflicts.CoordinatesConflicts;
import lab.beans.conflicts.LocationConflicts;
import lab.beans.conflicts.PersonConflicts;
import lab.beans.data.CoordinatesBean;
import lab.beans.data.LocationBean;
import lab.beans.data.MovieBean;
import lab.beans.data.PersonBean;
import lab.beans.data.util.OperationBean;
import lab.beans.profiles.CoordinatesProfileBean;
import lab.beans.profiles.LocationProfileBean;
import lab.beans.profiles.MovieProfileBean;
import lab.beans.profiles.PersonProfileBean;

@Named("updateController")
@RequestScoped
public class UpdateController {
    @Inject
    private MovieBean movieBean;
    @Inject
    private PersonBean personBean;
    @Inject
    private LocationBean locationBean;
    @Inject
    private CoordinatesBean coordinatesBean;
    @Inject
    private CoordinatesConflicts coordinatesConflicts;
    @Inject
    private LocationConflicts locationConflicts;
    @Inject
    private PersonConflicts personConflicts;
    @Inject
    private CoordinatesProfileBean coordinatesProfileBean;
    @Inject
    private LocationProfileBean locationProfileBean;
    @Inject
    private PersonProfileBean personProfileBean;
    @Inject
    private MovieProfileBean movieProfileBean;
    @Inject
    private OperationBean operationBean;

    public void checkAllUpdates() {
        movieBean.checkForUpdates();
        personBean.checkForUpdates();
        locationBean.checkForUpdates();
        coordinatesBean.checkForUpdates();

        coordinatesConflicts.checkForUpdates();
        locationConflicts.checkForUpdates();
        personConflicts.checkForUpdates();
    }

    public void checkCoordinatesProfileUpdates() {
        coordinatesProfileBean.checkForUpdates();
    }

    public void checkLocationProfileUpdates() {
        locationProfileBean.checkForUpdates();
    }

    public void checkPersonProfileUpdates() {
        personProfileBean.checkForUpdates();
    }

    public void checkMovieProfileUpdates() {
        movieProfileBean.checkForUpdates();
    }

    public void checkHistoryUpdates() {
        operationBean.checkForUpdates();
    }
}
