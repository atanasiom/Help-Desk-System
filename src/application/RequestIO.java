package application;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RequestIO {

	//Formatter for the dates
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	/**
	 * Parses the CSV file into an ArrayList of ServiceRequests
	 *
	 * @param file the CSV to use
	 *
	 * @return the ServiceRequest ArrayList
	 *
	 * @throws ParseException        error in parsing
	 * @throws FileNotFoundException file is not found
	 */
	public static ArrayList<ServiceRequest> parseList(File file) throws ParseException, FileNotFoundException {

		ArrayList<ServiceRequest> list = new ArrayList<ServiceRequest>();

		Scanner s = new Scanner(file);
		s.nextLine();
		//Loops through the file while there is still data and turns them into ServiceRequests
		while (s.hasNextLine()) {
			String[] data = s.nextLine().split(",");
			Date requested = RequestIO.DATE_FORMATTER.parse(data[0]);
			Date closed = data[1].equals(" ") ? null : RequestIO.DATE_FORMATTER.parse(data[1]);
			list.add(new ServiceRequest(requested, closed, data[2], data[4], data[3]));
		}
		s.close();
		return list;
	}

	/**
	 * Saves the master list of ServiceRequests back into the CSV file
	 *
	 * @param list the list to save
	 * @param file the file to save to
	 *
	 * @return {@code true} if the operation competes successfully, or {@code false} if it fails.
	 *
	 * @throws IOException if an I/O Exception occurs
	 */
	public static boolean saveList(ArrayList<ServiceRequest> list, File file) throws IOException {
		try {

			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			pw.write("dateRequested,dateCompleted,description,notes,technician\r\n");

			//For each Request, this writes it to the file
			for (ServiceRequest req : list) {
				pw.write(DATE_FORMATTER.format(req.getDateRequested()));
				pw.write(",");
				pw.write(req.getDateCompleted() == null ? " " : DATE_FORMATTER.format(req.getDateCompleted()));
				pw.write(",");
				pw.write(req.getDescription());
				pw.write(",");
				pw.write(req.getNotes());
				pw.write(",");
				pw.write(req.getTechnician());
				pw.write("\r\n");
			}
			pw.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}