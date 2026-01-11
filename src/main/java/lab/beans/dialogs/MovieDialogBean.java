package lab.beans.dialogs;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lab.beans.util.UpdateBean;
import lab.data.Coordinates;
import lab.data.Movie;
import lab.data.Person;
import lab.data.enums.MovieGenre;
import lab.data.enums.MpaaRating;
import lab.database.DatabaseManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("movieDialogBean")
@SessionScoped
public class MovieDialogBean implements Serializable {
    @EJB
    private DatabaseManager databaseManager;
    @Inject
    private UpdateBean updateBean;
    private Movie movie = new Movie();

    private Integer selectedDirectorId;
    private Integer selectedScreenwriterId;
    private Integer selectedOperatorId;
    private Integer selectedCoordinatesId;

    private boolean editing = false;

    public void openAddDialog() {
        movie = new Movie();
        editing = false;
    }

    public void openEditDialog(int id) {
        movie = databaseManager.getObjectById(Movie.class, id);
        selectedCoordinatesId = movie.getCoordinates() != null ? movie.getCoordinates().getId() : null;
        selectedDirectorId = movie.getDirector() != null ? movie.getDirector().getId() : null;
        selectedScreenwriterId = movie.getScreenwriter() != null ? movie.getScreenwriter().getId() : null;
        selectedOperatorId = movie.getOperator() != null ? movie.getOperator().getId() : null;
        editing = true;
    }

    public void addMovie() {
        movie.setDirector(databaseManager.getObjectById(Person.class, selectedDirectorId));
        movie.setScreenwriter(databaseManager.getObjectById(Person.class, selectedScreenwriterId));
        movie.setOperator(databaseManager.getObjectById(Person.class, selectedOperatorId));
        movie.setCoordinates(databaseManager.getObjectById(Coordinates.class, selectedCoordinatesId));
        databaseManager.addObject(movie);
        updateBean.increaseVersion();
    }

    public void updateMovie() {
        movie.setDirector(databaseManager.getObjectById(Person.class, selectedDirectorId));
        movie.setScreenwriter(databaseManager.getObjectById(Person.class, selectedScreenwriterId));
        movie.setOperator(databaseManager.getObjectById(Person.class, selectedOperatorId));
        movie.setCoordinates(databaseManager.getObjectById(Coordinates.class, selectedCoordinatesId));
        databaseManager.updateObject(movie);
        updateBean.increaseVersion();
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public List<MpaaRating> getMpaaRatingList() {
        return Arrays.asList(MpaaRating.values());
    }

    public List<MovieGenre> getMovieGenreList() {
        return Arrays.asList(MovieGenre.values());
    }

    public Integer getSelectedDirectorId() {
        return selectedDirectorId;
    }

    public void setSelectedDirectorId(Integer selectedDirectorId) {
        this.selectedDirectorId = selectedDirectorId;
    }

    public Integer getSelectedScreenwriterId() {
        return selectedScreenwriterId;
    }

    public void setSelectedScreenwriterId(Integer selectedScreenwriterId) {
        this.selectedScreenwriterId = selectedScreenwriterId;
    }

    public Integer getSelectedOperatorId() {
        return selectedOperatorId;
    }

    public void setSelectedOperatorId(Integer selectedOperatorId) {
        this.selectedOperatorId = selectedOperatorId;
    }

    public Integer getSelectedCoordinatesId() {
        return selectedCoordinatesId;
    }

    public void setSelectedCoordinatesId(Integer selectedCoordinatesId) {
        this.selectedCoordinatesId = selectedCoordinatesId;
    }
}
