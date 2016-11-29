import datamodel.Position;
import handler.PortfolioNAVHandler;
import handler.PortfolioReportHandler;
import util.ThreadDispatchCachePublisher;
import utility.StockPriceGenerator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        ConcurrentMap<String, Position> positionMap = initializedData();
        ThreadDispatchCachePublisher publisher = new ThreadDispatchCachePublisher();
        publisher.subscribe(new PortfolioNAVHandler(positionMap));
        publisher.subscribe(new PortfolioReportHandler(positionMap));
        new StockPriceGenerator(publisher, positionMap.get("AAPL")).run();
    }

    private static ConcurrentMap<String, Position> initializedData(){
        ConcurrentMap<String, Position> positionMap = new ConcurrentHashMap<>();
        Position stockPosition = new Position("AAPL",100,10,0.05);
        stockPosition.setExpectedReturn(0.2);
        Position callOption = new Position("AAPLCallOption",20,-1,0.1);
        callOption.setStrikePrice(120);
        callOption.setYearToMaturity(1);
        Position putOption = new Position("AAPLPutOption",15,1,0.1);
        putOption.setStrikePrice(80);
        putOption.setYearToMaturity(1);
        positionMap.put("AAPL",stockPosition);
        positionMap.put("AAPLCallOption",callOption);
        positionMap.put("AAPLPutOption",putOption);
        return positionMap;
    }
}
