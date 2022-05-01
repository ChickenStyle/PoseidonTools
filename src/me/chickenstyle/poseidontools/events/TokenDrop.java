package me.chickenstyle.poseidontools.events;


import java.util.concurrent.ThreadLocalRandom;

public class TokenDrop {

    private int chance;
    private int dropRange;

    public TokenDrop(int chance, int dropRange) {
        this.chance = chance;
        this.dropRange = dropRange;
    }

    public int generateDropAmount() {
        return generateDropAmount(0);
    }

    public int generateDropAmount(int rangeIncrease) {
        return ThreadLocalRandom.current().nextInt(1, dropRange + rangeIncrease + 1);
    }

    public int getChance() {
        return chance;
    }
}