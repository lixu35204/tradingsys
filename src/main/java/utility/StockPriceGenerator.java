package utility;

import datamodel.Position;
import util.ThreadDispatchCachePublisher;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class StockPriceGenerator extends TimerTask {
    public static Timer timer = new Timer();
    private ThreadDispatchCachePublisher handler;
    private Position stockPosition;

    public StockPriceGenerator(ThreadDispatchCachePublisher handler, Position stockPosition) {
        this.handler = handler;
        this.stockPosition = stockPosition;
    }

    @Override
    public void run() {
        double delay = (new Random().nextDouble() * (2 - 0.5) + 0.5) * 1000;
        timer.schedule(new StockPriceGenerator(handler, stockPosition), (long) delay);
        handler.publish(getPositionWithNewMarketPrice(stockPosition, delay / 1000));
    }

    private synchronized Position getPositionWithNewMarketPrice(Position position, double deltaTime) {
        double deltaPrice = position.getExpectedReturn() * deltaTime / 7257600 + position.getStandardTd() * Math.sqrt(deltaTime / 7257600) * new Random().nextGaussian();
        position.setPrice(deltaPrice * position.price() + position.price());
        return position;
    }

}
