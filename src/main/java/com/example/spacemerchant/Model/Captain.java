package com.example.spacemerchant.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class Captain implements Cloneable, Serializable {

    private int credit;
    private Ship ship;

    public Captain(){
        this.setCredit(100);
        this.setShip(null);
    }


    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }
    public Ship getShip() {
        return ship;
    }
    public void setShip(Ship ship) {
        this.ship = ship;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Captain captain = (Captain) o;
        return credit == captain.credit && Objects.equals(ship, captain.ship);
    }

    //Actions done by the captain -> Inputs must be string, they are cast to the respective type inside
    public void buy(String itemName, Integer quantity) throws IOException {

        Item item = this.getShip().getCurrentPort().getItemByStringName(itemName);
        if (!this.getShip().getCurrentPort().getStock().containsKey(item)){
            throw new IOException("Sorry couldn't find the item you entered");
        } else if (item.getName().equals(null)){
            throw new IOException("Sorry couldn't find the item you entered");
        } else if (quantity<0){
            throw new IOException("Negative inputs not allowed");
        }

        int totalPurchase = item.getPriceIn(this.ship.getCurrentPort()) * quantity;

            if (!item.isTradedIn(this.getShip().getCurrentPort())){
                System.out.println("Woohooo you can't do that here buddy!");
                throw new IOException("Illegal item!");
            } else if ( totalPurchase > this.getCredit() ) {
                System.out.println("You don't have enough credits to do that!");
                throw new IOException("You're too poor!");
            } else {

            this.getShip().getCurrentPort().removeItemFromStock(item,quantity);
            this.getShip().addItemToInventory(item,quantity);

            int oldCredits = this.getCredit();
            this.setCredit( oldCredits- totalPurchase );

            }


    }
    public void sell(String itemName,Integer quantity) throws IOException {


        Item item = this.getShip().getItemByStringName(itemName);
        //TODO -> Changed this from getCurrentPort().getStock into getShipInventory! - could be buggy
        if (!this.getShip().getShipInventory().containsKey(item)){
            throw new IOException("Sorry couldn't find the item you entered");
        } else if (item.getName().equals(null)){
            throw new IOException("Sorry couldn't find the item you entered");
        } else if (quantity<0){
            throw new IOException("Negative inputs not allowed");
        }

        int totalGained = item.getPriceIn(this.ship.getCurrentPort()) * quantity;
        if (!item.isTradedIn(this.getShip().getCurrentPort())){
            throw new IOException("Illegal item! You can't sell this in this harbor!");
        } else {
            this.getShip().getCurrentPort().addItemToStock(item,quantity);
            this.getShip().removeItemFromInventory(item,quantity);

            int oldCredits = this.getCredit();
            this.setCredit( oldCredits + totalGained );
        }
    }
    public void sail(String destinationPortName) throws IOException {


        Harbor destinationHarbor = this.ship.getCurrentPort().getPortByPortName(destinationPortName);

        if (!this.getShip().getCurrentPort().getDistanceToHashMap().containsKey(destinationHarbor)){
            throw new IOException("Sorry couldn't find the port you entered");
        }

        int fuelToDestination = this.ship.getCurrentPort().getDistanceTo(destinationHarbor);

        if (this.ship.getFuel() - fuelToDestination < 0){
            throw new IOException("You don't have enough fuel, required fuel to this port is: " + fuelToDestination);
        } else {
            this.ship.removeFuel(fuelToDestination);
            this.ship.setCurrentPort(destinationHarbor);
            this.ship.incrementTripCounter();
        }
    }


}
