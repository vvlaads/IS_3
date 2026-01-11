package lab.beans.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("updateBean")
@ApplicationScoped
public class UpdateBean implements Serializable {
    private long version = 0;

    public void increaseVersion() {
        this.version += 1;
    }

    public long getVersion() {
        return version;
    }
}
