package com.mygdx.game.CityClasses;

public abstract class ShopInfo {
    public static final int quantityCoins = 6;
    public static final int[] coinsInShop = {
            100,
            500,
            1500,
            8000,
            75000,
            600000
    };
    public static final int[] sapphiresInShop = {
            10,
            40,
            100,
            400,
            2500,
            15000
    };

    public static final int quantityHouses = 5;
    public static final int[] cost = {
            100, /* house */
            500, /* shop */
            1000, /* restaurant */
            8000, /* hotel */
            20000  /* bank */
    };
    public static final int maxLevel = 4;
    public static final int[][] upgradeCost = {
            {100, 900, 5000, 30000}, /* house */
            {700, 3000, 15000, 75000}, /* shop */
            {2000, 6000, 50000, 120000}, /* restaurant */
            {10000, 25000, 70000, 250000}, /* hotel */
            {35000, 150000, 400000, 950000} /* bank */
    };
    public static final int[][] incomeFromHouses = {
            {5, 10, 40, 200, 1000},
            {15, 30, 100, 500, 2500},
            {40, 80, 300, 1500, 7500},
            {150, 300, 1200, 6000, 30000},
            {350, 600, 2400, 12000, 60000}
    };
    public static final int[] houseTimer = {
            300,
            240,
            180,
            150,
            120
    };
    public static final int territoryLevels = 5;
    public static final int[][] freeHousesPlace = {
            {3,2},
            {3,3},
            {4,3},
            {4,4},
            {5,4}
    };
    public static final int[] territoryLevelUpPrice = {
            15000,
            50000,
            200000,
            1500000,
            0
    };
}
