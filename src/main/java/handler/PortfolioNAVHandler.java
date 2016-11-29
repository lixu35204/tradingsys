package handler;

import datamodel.Position;
import util.Publisher;
import utility.DerivativePriceUtil;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class PortfolioNAVHandler implements Publisher<Position>{
    protected ConcurrentMap<String, Position> positions = new ConcurrentHashMap<>();

    public PortfolioNAVHandler(ConcurrentMap<String, Position> positions)
    {
        this.positions = positions;
    }

    public synchronized void publish(Position position) {
        double totalNav = 0;
        boolean updateDerivativePosition = false;
        positions.put(position.symbol(),position);
        if ("AAPL".equals(position.symbol()))
        {
            updateDerivativePosition = true;
        }
        for (String productName:positions.keySet())
        {
            Position current = positions.get(productName);
            if (updateDerivativePosition&&!"AAPL".equals(current.symbol()))
            {
                current = updateDerivativePrice(position.price(),current);
            }
            totalNav = totalNav+current.quantity()*current.price();
        }
        System.out.println(new Timestamp(System.currentTimeMillis()).toString() + "\tPortfolioNAVHandler NAV: " + totalNav);
    }

    private Position updateDerivativePrice(double stockPrice, Position position) {
        if ("AAPLCallOption".equals(position.symbol())) {
            position.setPrice(DerivativePriceUtil.callOptionPrice(stockPrice, position, DerivativePriceUtil.getD1(stockPrice, position), DerivativePriceUtil.getD2(stockPrice, position)));
            positions.put(position.symbol(),position);
        }
        else if ("AAPLPutOption".equals(position.symbol())) {
            position.setPrice(DerivativePriceUtil.putOptionPrice(stockPrice, position, DerivativePriceUtil.getD1(stockPrice, position), DerivativePriceUtil.getD2(stockPrice, position)));
            positions.put(position.symbol(),position);
        }
        return position;
    }
}
