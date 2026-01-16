package lab.beans.util;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named("formatBean")
@RequestScoped
public class FormatBean {
    public String formatMoney(long amount) {
        if (amount >= 1_000_000) {
            return String.format("%.1f млн", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("%.0f тыс", amount / 1_000.0);
        } else {
            return String.valueOf(amount);
        }
    }

    public String formatDate(String date) {
        return date;
    }
}
