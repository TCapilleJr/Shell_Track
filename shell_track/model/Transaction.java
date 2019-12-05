package shell_track.model;

import javafx.beans.property.*;

public class Transaction {
    private final IntegerProperty id;
    private final StringProperty client;
    private final StringProperty originalHarvestNumber;
    private final StringProperty typeOfShellfish;
    private final IntegerProperty numberOfUnits;
    private final IntegerProperty countPerUnit;
    private final StringProperty harvestDate;
    private final StringProperty harvestLocation;
    private final IntegerProperty temperature;
    private final StringProperty sold;
    private final StringProperty enteredBy;
    private final StringProperty timeEntered;
    private final StringProperty shippingDate;
    private final IntegerProperty productOnHand;


    public Transaction(int id, String client, String originalHarvestNumber, String typeOfShellfish, int numberOfUnits, int countPerUnit, String harvestDate, String harvestLocation, int temperature,
                       String soldOrReceived, String enteredBy, String timeEntered, String shippingDate, int productOnHand) {

        this.id = new SimpleIntegerProperty(id);
        this.client = new SimpleStringProperty(client);
        this.originalHarvestNumber = new SimpleStringProperty(originalHarvestNumber);
        this.typeOfShellfish = new SimpleStringProperty(typeOfShellfish);
        this.numberOfUnits = new SimpleIntegerProperty(numberOfUnits);
        this.countPerUnit = new SimpleIntegerProperty(countPerUnit);
        this.harvestDate = new SimpleStringProperty(harvestDate);

        this.harvestLocation = new SimpleStringProperty(harvestLocation);
        this.temperature = new SimpleIntegerProperty(temperature);
        this.sold = new SimpleStringProperty(soldOrReceived);
        this.enteredBy = new SimpleStringProperty(enteredBy);
        this.timeEntered = new SimpleStringProperty(timeEntered);
        this.shippingDate = new SimpleStringProperty(shippingDate);
        this.productOnHand = new SimpleIntegerProperty(productOnHand);
    }


    /**
     * Getters
     */


    public int getProductOnHand() {
        return productOnHand.get();
    }

    public IntegerProperty productOnHandProperty() {
        return productOnHand;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty shippingDateProperty() {
        return shippingDate;
    }

    public String getShippingDate() {
        return shippingDate.get();
    }

    public IntegerProperty countPerUnitProperty() {
        return countPerUnit;
    }

    public int getCountPerUnit() {
        return countPerUnit.get();
    }

    public StringProperty soldOrReceivedProperty() {
        return sold;
    }

    public String getSoldOrReceived() {
        return sold.get();
    }

    public IntegerProperty numberOfUnitsInBagProperty() {
        return numberOfUnits;
    }

    public int getNumberOfUnits() {
        return numberOfUnits.get();
    }

    public IntegerProperty temperatureProperty() {
        return temperature;
    }

    public int getTemperature() {
        return temperature.get();
    }

    public StringProperty clientProperty() {
        return client;
    }

    public String getClient() {
        return client.get();
    }

    public StringProperty enteredByProperty() {
        return enteredBy;
    }

    public String getEnteredBy() {
        return enteredBy.get();
    }

    public StringProperty harvestDateProperty() {
        return harvestDate;
    }

    public String getHarvestDate() {
        return harvestDate.get();
    }

    public StringProperty harvestLocationProperty() {
        return harvestLocation;
    }

    public String getHarvestLocation() {
        return harvestLocation.get();
    }

    public StringProperty originalHarvestNumberProperty() {
        return originalHarvestNumber;
    }

    public String getOriginalHarvestNumber() {
        return originalHarvestNumber.get();
    }

    public StringProperty timeEnteredProperty() {
        return timeEntered;
    }

    public String getTimeEntered() {
        return timeEntered.get();
    }

    public StringProperty typeOfShellfishProperty() {
        return typeOfShellfish;
    }

    public String getTypeOfShellfish() {
        return typeOfShellfish.get();
    }

    /**
     * Setters
     */
    public void setProductOnHand(int productOnHand) {
        this.productOnHand.set(productOnHand);
    }

    public void setClient(String client) {
        this.client.set(client);
    }

    public void setOriginalHarvestNumber(String originalHarvestNumber) {
        this.originalHarvestNumber.set(originalHarvestNumber);
    }

    public void setTypeOfShellfish(String typeOfShellfish) {
        this.typeOfShellfish.set(typeOfShellfish);
    }

    public void setNumberOfUnits(int numberOfUnitsInBag) {
        this.numberOfUnits.set(numberOfUnitsInBag);
    }

    public void setCountPerUnit(int countPerUnit) {
        this.countPerUnit.set(countPerUnit);
    }

    public void setHarvestDate(String harvestDate) {
        this.harvestDate.set(harvestDate);
    }

    public void setHarvestLocation(String harvestLocation) {
        this.harvestLocation.set(harvestLocation);
    }

    public void setTemperature(int temperature) {
        this.temperature.set(temperature);
    }

    public void setSold(String sold) {
        this.sold.set(sold);
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy.set(enteredBy);
    }

    public void setTimeEntered(String timeEntered) {
        this.timeEntered.set(timeEntered);
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate.set(shippingDate);
    }

}
