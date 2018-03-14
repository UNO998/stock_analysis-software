package profitsrus;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class YahooFinanceAPI {
	public static ArrayList<String> FetchData(String symbol, GregorianCalendar start, GregorianCalendar end) {
		ArrayList<String> fetchedData = new ArrayList<>();

		//http://real-chart.finance.yahoo.com/table.csv?s=%5EGSPTSE&d=10&e=16&f=2016&g=d&a=5&b=29&c=1979&ignore=.csv

		String theUrl = "http://real-chart.finance.yahoo.com/table.csv?s="+symbol+    //This creates an URL with the dates
				"&d="+end.get(Calendar.MONTH)+                                        //the user will input to look for a certain stock price
				"&e=" + end.get(Calendar.DAY_OF_MONTH) +
				"&f=" + end.get(Calendar.YEAR) +
				"&g=d&a=" + start.get(Calendar.MONTH) +
				"&b=" + start.get(Calendar.DAY_OF_MONTH)+
				"&c=" + start.get(Calendar.YEAR) +
				"&ignore=.csv";

		try {
			URL yahooFin = new URL(theUrl);
			URLConnection data  = yahooFin.openConnection();
			@SuppressWarnings("resource")
			Scanner input = new Scanner(data.getInputStream());
			if (input.hasNext()) { //This skips a line, since its the header (E.g.: Date, Open, Low, etc...)
				input.nextLine();
			}

			//Start reading the data
			while(input.hasNextLine()) {
				fetchedData.add(input.nextLine());
			}
		} catch(Exception e) {
			System.err.println(e);
		}

		return fetchedData;
	}
}
