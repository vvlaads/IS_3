package lab.beans.util;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("navigationBean")
@SessionScoped
public class NavigationBean implements Serializable {
    private String currentPage;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public void markPage(String page) {
        this.currentPage = page;
    }
}
