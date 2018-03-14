package profitsrus;

import java.awt.Dimension;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ProfitsRUs extends Application {
	private static String TITLE = "ProfitsRUs - Market Analysis Tool";
	//private static String STOCK_DATA_FILE = "data/sample.csv";
	private static Dimension GRAPH_DIMENSIONS = new Dimension(650, 550);
	private Stage stage;
	private BorderPane rootLayout;


	@FXML
	private CheckBox moving20;

	@FXML
	private CheckBox moving50;

	@FXML
	private CheckBox moving100;

	@FXML
	private CheckBox moving200;

	@FXML
	private ChoiceBox<String> stock;

	@FXML
	private TextField fromday;

	@FXML
	private TextField frommonth;

	@FXML
	private TextField fromyear;

	@FXML
	private TextField today;

	@FXML
	private TextField tomonth;

	@FXML
	private TextField toyear;

	@FXML
	private Button enter;


	@FXML
	protected void handleStockList(MouseEvent event){
		try{
		stock.setItems(FXCollections.observableArrayList(
			    "IBM","stock1123123412312","stock2")
			);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();

		}

	}

	@FXML
	protected void handleButtonAction(ActionEvent event) {
		enter.setText("Enter");

		try {
			// TODO: Use this later
			/*moving20.isSelected();
			moving50.isSelected();
			moving100.isSelected();
			moving200.isSelected();*/

			DateData fromDate = DateData.CreateDateData(Integer.parseInt(fromday.getText()), Integer.parseInt(frommonth.getText()), Integer.parseInt(fromyear.getText()));
			DateData toDate = DateData.CreateDateData(Integer.parseInt(today.getText()), Integer.parseInt(tomonth.getText()), Integer.parseInt(toyear.getText()));
			String stockname = (String) stock.getValue();
			updateStockDiagram(stockname, fromDate, toDate);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			enter.setText("Invalid input!");
		}
	}

	@Override
	public void start(Stage stage) {

		this.stage = stage;
		stage.setTitle(TITLE);
		initRootLayout();
		showStockview();
		createStockDiagram();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the Stock overview inside the root layout.
	 */
	public void showStockview() {
		try {
			// Load Stock overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("Stockview.fxml"));
			AnchorPane stockView = (AnchorPane) loader.load();

			// Set Stock overview into the center of root layout.
			rootLayout.setLeft(stockView);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the Stock Diagram
	 */
	public void createStockDiagram() {
		try {
			// Load Stock Diagram
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("StockDiagram.fxml"));
			FlowPane root = (FlowPane) loader.load();

			NumberAxis xAxis = new NumberAxis(0, 0, 1);
			NumberAxis yAxis = new NumberAxis(0, 0, 1);
			chart = new LineChart<Number, Number>(xAxis, yAxis);
			chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);
			root.getChildren().add(chart);

			rootLayout.setCenter(root);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static LineChart<Number, Number> chart;

	private void updateStockDiagram(String stock, DateData fromDate, DateData toDate) {
		chart.getData().clear();

		if (fromDate.GetYear() == toDate.GetYear() && fromDate.GetMonth() == toDate.GetMonth()) {
			createDayLineChart(stock, fromDate.GetYear(), fromDate.GetMonth());
		} else if (fromDate.GetYear() == toDate.GetYear()) {
			createMonthLineChart(stock, fromDate.GetYear());
		} else {
			createYearLineChart(stock, fromDate.GetYear(), toDate.GetYear());
		}
	}

	private LineChart<Number, Number> createYearLineChart(String stock, int minimumYear, int maximumYear) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(minimumYear, 1, 1);
		GregorianCalendar endDate = new GregorianCalendar(maximumYear, 12, 31);

		chart.setTitle("Stock " + stock + " between " + minimumYear + " and " + maximumYear);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Open' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		for (StockData stockData : stockDatas) {
			int stockDataYear = stockData.GetDate().GetYear();
			if (stockDataYear >= minimumYear && stockDataYear <= maximumYear) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetOpen().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetOpen());
				maximumStock = (int) Math.max(maximumStock, stockData.GetOpen());
				series.getData().add(new XYChart.Data<Number, Number>(stockDataYear, stockData.GetOpen()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		((NumberAxis)chart.getXAxis()).setLabel("Year");
		((NumberAxis)chart.getXAxis()).setLowerBound(minimumYear);
		((NumberAxis)chart.getXAxis()).setUpperBound(maximumYear);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting years chart, years " + minimumYear + " to " + maximumYear + ": " + series.getData().size() + " results");

		return chart;
	}

	private LineChart<Number, Number> createMonthLineChart(String stock, int year) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(year, 1, 1);
		GregorianCalendar endDate = new GregorianCalendar(year, 12, 31);

		chart.setTitle("Stock " + stock + " in " + year);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Open' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		for (StockData stockData : stockDatas) {
			if (stockData.GetDate().GetYear() == year) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetOpen().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetOpen());
				maximumStock = (int) Math.max(maximumStock, stockData.GetOpen());
				series.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetMonth(), stockData.GetOpen()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		((NumberAxis)chart.getXAxis()).setLabel("Month");
		((NumberAxis)chart.getXAxis()).setLowerBound(1);
		((NumberAxis)chart.getXAxis()).setUpperBound(12);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting months chart, year " + year + ": " + series.getData().size() + " results");

		return chart;
	}

	private LineChart<Number, Number> createDayLineChart(String stock, int year, int month) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(year, month, 1);
		GregorianCalendar endDate = new GregorianCalendar(year, month, 31);

		chart.setTitle("Stock " + stock + " in " + month + " of " + year);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Open' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		for (StockData stockData : stockDatas) {
			if (stockData.GetDate().GetYear() == year && stockData.GetDate().GetMonth() == month) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetOpen().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetOpen());
				maximumStock = (int) Math.max(maximumStock, stockData.GetOpen());
				series.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetDay(), stockData.GetOpen()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		((NumberAxis)chart.getXAxis()).setLabel("Day");
		((NumberAxis)chart.getXAxis()).setLowerBound(1);
		((NumberAxis)chart.getXAxis()).setUpperBound(31);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting days chart, year " + year + ", month " + month + ": " + series.getData().size() + " results");

		return chart;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
