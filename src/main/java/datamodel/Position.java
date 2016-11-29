package datamodel;

public class Position {
    private String symbol;
    private double price;
    private double quantity;
    private int yearToMaturity;
    private double strikePrice;
    private double standardTd;
    private double expectedReturn;

    public Position(String symbol, double price, double quantity, double standardTd){
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.standardTd = standardTd;
    }

    public String symbol (){
        return this.symbol;
    }

    public double price (){
        return this.price;
    }

    public double quantity (){
        return this.quantity;
    }

    public void setSymbol (String symbol){
         this.symbol = symbol;
    }

    public void setPrice (double price){
        this.price = price;
    }

    public void setQuantity (double quantity){
        this.quantity = quantity;
    }

    public int getYearToMaturity(){
        return this.yearToMaturity;
    }

    public double getStrikePrice (){
        return this.strikePrice;
    }

    public void setStrikePrice (double strikePrice){
        this.strikePrice = strikePrice;
    }

    public void setYearToMaturity (int yearToMaturity){
        this.yearToMaturity = yearToMaturity;
    }

    public double getStandardTd (){
        return this.standardTd;
    }

    public void setStandardTd (double standardTd){
        this.standardTd = standardTd;
    }

    public double getExpectedReturn (){
        return this.expectedReturn;
    }

    public void setExpectedReturn (double expectedReturn){
        this.expectedReturn = expectedReturn;
    }
 }
 