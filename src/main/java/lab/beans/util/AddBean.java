package lab.beans.util;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.util.Arrays;
import java.util.List;

@Named("addBean")
@RequestScoped
public class AddBean {
    private String className;

    private final List<String> classNameList = Arrays.asList("Movie", "Person", "Location", "Coordinates");

    public void showDialog() {
        if (className == null) return;

        String widgetVar = null;
        switch (className) {
            case "Movie":
                widgetVar = "movieDialog";
                break;
            case "Person":
                widgetVar = "personDialog";
                break;
            case "Location":
                widgetVar = "locationDialog";
                break;
            case "Coordinates":
                widgetVar = "coordinatesDialog";
                break;
        }

        if (widgetVar != null) {
            PrimeFaces.current().executeScript("PF('" + widgetVar + "').show()");
        }
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getClassNameList() {
        return classNameList;
    }
}
