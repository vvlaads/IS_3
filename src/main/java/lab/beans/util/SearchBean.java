package lab.beans.util;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Arrays;
import java.util.List;

@Named("searchBean")
@RequestScoped
public class SearchBean {
    @Inject
    private RedirectBean redirectBean;

    private String className;
    private final List<String> classNameList = Arrays.asList("Movie", "Person", "Location", "Coordinates");
    private int id;

    public void findByID() {
        switch (className) {
            case "Movie":
                redirectBean.toMovie(id);
                break;
            case "Person":
                redirectBean.toPerson(id);
                break;
            case "Location":
                redirectBean.toLocation(id);
                break;
            case "Coordinates":
                redirectBean.toCoordinates(id);
                break;
            default:
                redirectBean.redirectTo("index");
                break;
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getClassNameList() {
        return classNameList;
    }

    private RedirectBean getRedirectBean() {
        return redirectBean;
    }
}
