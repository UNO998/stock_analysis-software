package profitsrus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockCSVReader {
	public static String CVS_SPLIT = ",";

	public static List<StockData> ComputeStockDataList(ArrayList<String> data) {
		List<StockData> stockDataList = new ArrayList<>();
		for (String line : data) {
			String[] splitLine = line.split(CVS_SPLIT);
			StockData stockData = StockData.CreateStockDataFromArray(splitLine);
			stockDataList.add(stockData);
		}
		return stockDataList;
	}

	public static List<StockData> ComputeStockDataList(String filename, int limit) {
		List<StockData> stockDataList = new ArrayList<>();

		BufferedReader br = null;
		String line = "";
		int lineCount = 0;

		try {
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				if (lineCount != 0) {
					String[] splitLine = line.split(CVS_SPLIT);
					StockData stockData = StockData.CreateStockDataFromArray(splitLine);
					stockDataList.add(stockData);
				}
				lineCount++;
				if (lineCount > limit && limit != 0) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return stockDataList;
	}
}
