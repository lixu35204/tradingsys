package handler;


import datamodel.Position;
import util.Publisher;

import java.io.*;
import java.util.concurrent.ConcurrentMap;

public class PortfolioReportHandler extends PortfolioNAVHandler implements Publisher<Position> {
    public PortfolioReportHandler(ConcurrentMap<String, Position> positions) {
        super(positions);
    }

    private synchronized void buildReport() {
        double totalMV = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("**********Portfolio Report**********\n");
        stringBuffer.append("Product~Quantity~MarketValue\n");
        for (Position position:positions.values()) {
            totalMV = totalMV + position.quantity()*position.price();
            stringBuffer.append(position.symbol()).append("~").append(position.quantity()).append("~").append(position.quantity()*position.price()).append("\n");
        };
        stringBuffer.append("TotalNAV:\t").append(totalMV);
        File file = new File ("output/PortfolioReport.txt");
        try {
            FileWriter fileWriter = new FileWriter(file,false);
            fileWriter.write(stringBuffer.toString());
            fileWriter.close ();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(Position position) {
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String name = null;
            try {
                name = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ("PrintReport".equals(name)) {
                buildReport();
            }
        }
    }
}