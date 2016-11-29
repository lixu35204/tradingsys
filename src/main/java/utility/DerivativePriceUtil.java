package utility;

import datamodel.Position;
import org.apache.commons.math3.distribution.NormalDistribution;

public class DerivativePriceUtil {
    public static double getD1(double stockPrice, Position position) {
        return (Math.log1p(stockPrice/position.getStrikePrice())+(0.02+position.getStandardTd()*position.getStandardTd()/2)*position.getYearToMaturity())
                /(position.getStandardTd()*Math.sqrt(position.getYearToMaturity()));
    }

    public static double getD2(double stockPrice, Position position) {
        return getD1(stockPrice, position) - position.getStandardTd()*Math.sqrt(position.getYearToMaturity());
    }

    public static double callOptionPrice(double stockPrice, Position position, double d1, double d2) {
        return stockPrice * new NormalDistribution().cumulativeProbability(d1)
                - position.getStrikePrice() * Math.exp(-1 * 0.02 * position.getYearToMaturity()) * (new NormalDistribution().cumulativeProbability(d2));
    }

    public static double putOptionPrice(double stockPrice, Position position, double d1, double d2) {
        return position.getStrikePrice() * Math.exp(-1 * 0.02 * position.getYearToMaturity()) * (new NormalDistribution().cumulativeProbability(-1*d2))
                - stockPrice * new NormalDistribution().cumulativeProbability(-1*d1);
    }
}
