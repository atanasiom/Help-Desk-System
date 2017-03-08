package application;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RequestIO {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    public static ArrayList<ServiceRequest> parseList(InputStream input) throws ParseException {

        ArrayList<ServiceRequest> list = new ArrayList<ServiceRequest>();

        Scanner s = new Scanner(input);
        s.nextLine();
        while (s.hasNextLine()) {
            String[] data = s.nextLine().split(",");
            Date requested = RequestIO.DATE_FORMATTER.parse(data[0]);
            Date closed = data[1].equals(" ") ? null : RequestIO.DATE_FORMATTER.parse(data[1]);
            list.add(new ServiceRequest(requested, closed, data[2], data[3]));
        }
        s.close();
        return list;
    }

    public static boolean saveList(ArrayList<ServiceRequest> list) {
        return true;
    }

}