package sgh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
        
        File stock_file = new File(filePath);
        boolean exists = stock_file.exists();

        if (exists == false) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }

        Scanner sc = new Scanner(stock_file);
        String line = sc.nextLine();

        FileWriter new_stock_file = new FileWriter("data_out/" + stock + ".csv");
        new_stock_file.write(line + ",Change" + "\n");

        while (sc.hasNextLine()) {
            
            line = sc.nextLine();
            String[] position = line.split(",");
            
            float value_from_opening = Float.valueOf(position[1]);
            float value_from_closing = Float.valueOf(position[4]);
            
            float percentage_change = (value_from_closing - value_from_opening) / value_from_opening;
            new_stock_file.write(line + "," + percentage_change * 100 + "\n");
        }
        
        new_stock_file.close();
    
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[] { "IBM", "MSFT", "GOOG" };
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
