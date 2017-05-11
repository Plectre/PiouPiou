package piou.plectre.com.piou.handler;


/**
 * Created by El Di@blo on 10/05/2017.
 */

public class DateHandler {

    private String date;


    private String currentDay;
    private String currentTime;

    public DateHandler(String date) {

        this.date = date;

    }

    public String getCurrentDay() {

        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        String year = date.substring(0, 4);

        String currentDay = day + "/" + month + "/" + year;
        return currentDay;
    }

    public String getHeure() {

        currentTime = date.substring(11, 19);
        return currentTime;
    }


}
