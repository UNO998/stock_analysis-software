package profitsrus;

public class StockData {
	private DateData date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Integer volume;
	private Double adjclose;

	public static StockData CreateStockDataFromArray(String[] data) {
		StockData stockData = new StockData();
		stockData.date = DateData.CreateDateDataFromString(data[0]);
		stockData.open = Double.parseDouble(data[1]);
		stockData.high = Double.parseDouble(data[2]);
		stockData.low = Double.parseDouble(data[3]);
		stockData.close = Double.parseDouble(data[4]);
		stockData.volume = Integer.parseInt(data[5]);
		stockData.adjclose = Double.parseDouble(data[6]);
		return stockData;
	}

	public DateData GetDate() {
		return date;
	}

	public Double GetOpen() {
		return open;
	}

	public Double GetHigh() {
		return high;
	}

	public Double GetLow() {
		return low;
	}

	public Double GetClose() {
		return close;
	}

	public int GetVolume() {
		return volume;
	}

	public Double GetAdjClose() {
		return adjclose;
	}
}
