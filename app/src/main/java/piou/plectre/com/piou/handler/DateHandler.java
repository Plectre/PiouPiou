package piou.plectre.com.piou.handler;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by El Di@blo on 10/05/2017.
 */

public class DateHandler {


    private String lastMaj = "out";
    private String piouDay;
    private String piouDayFinal;


    public DateHandler(String date) {

        this.piouDay = date;
    }

    public boolean compareYear() throws ParseException {

        Date currentDate = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
        getPiouDay();
        String strCurrentDate = sfd.format(currentDate);

        Date date1 = sfd.parse(strCurrentDate);
        Date date2 = sfd.parse(piouDayFinal);

        int retourDate = date1.compareTo(date2);

        if (retourDate != 0) {
            return false;
        }

        Log.i("APP", strCurrentDate);
        Log.i("APP", piouDayFinal);
        Log.i("APP", (String.valueOf(retourDate)));
        return true;
    }

    public String getPiouDay() {

        String month = piouDay.substring(5, 7);
        String day = piouDay.substring(8, 10);
        String year = piouDay.substring(0, 4);

        piouDayFinal = day + "/" + month + "/" + year;
        return piouDayFinal;
    }

    public String getPiouHeure() {

        lastMaj = piouDay.substring(11, 19);
        return lastMaj;
    }


}
