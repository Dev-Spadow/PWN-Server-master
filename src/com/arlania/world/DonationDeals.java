package com.arlania.world;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;

public class DonationDeals {
    public enum Deals {
        NONE, BOGO,BTGO, $50_OR_MORE, $75_OR_MORE
    }

    private Item item = new Item(-1, -1);

    public Deals currentDeal = Deals.NONE;

    private int amountClaimed = 0;

    private int totalAllowableClaim = -1;

    public void setCurrentDeal(Deals deal) {
        currentDeal = deal;
    }

    public Deals getCurrentDeal() {
        return currentDeal;
    }

    public void setItemToGive(Item newItem) {
        item = newItem;
    }

    public Item getItemToGive() {
        return item;
    }

    public void setAmountClaimed(int amount) {

    }

    public String displayDeal() {
        switch (currentDeal) {
            case BOGO:
                return "Buy one get one is currently activated on all items in the donation store";
            case BTGO:
                return "Buy two get one is currently activated on all items in the donation store";
            case $50_OR_MORE:
                return "Spend $50 or more and get a bonus of: " + item.getAmount() + "x " + ItemDefinition.forId(item.getId()) + (totalAllowableClaim != -1 ? " " + amountClaimed + "/" + totalAllowableClaim : " ");
            case $75_OR_MORE:
                return "Spend $75 or more and get a bonus of: " + item.getAmount() + "x " + ItemDefinition.forId(item.getId()) + (totalAllowableClaim != -1 ? " " + amountClaimed + "/" + totalAllowableClaim : " ");
            default:
                return "There is currently no donation deal running";
        }
    }

    public void appendBonuses(int amountClaimed) {

    }
}
