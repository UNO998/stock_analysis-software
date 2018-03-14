package profitsrus;

public class DateData {
	private int year;
	private int month;
	private int day;

	public static DateData CreateDateDataFromString(String data) {
		DateData dateData = new DateData();
		String[] splitData = data.split("-");
		dateData.year = Integer.parseInt(splitData[0]);
		dateData.month = Integer.parseInt(splitData[1]);
		dateData.day = Integer.parseInt(splitData[2]);
		return dateData;
	}

	public static DateData CreateDateData(int day, int month, int year) {
		DateData dateData = new DateData();
		dateData.year = year;
		dateData.month = month;
		dateData.day = day;
		return dateData;
	}

	public int GetYear() {
		return year;
	}

	public int GetMonth() {
		return month;
	}

	public int GetDay() {
		return day;
	}
}
