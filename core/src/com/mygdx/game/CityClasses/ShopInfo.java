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
            1000, /* shop */
            10000, /* restaurant */
            80000, /* hotel */
            900000  /* bank */
    };
    public static final int maxLevel = 4;
    public static final int[][] upgradeCost = {
            {200, 300, 700, 1200}, /* house */
            {1500, 2500, 5000, 11000}, /* shop */
            {12000, 20000, 35000, 90000}, /* restaurant */
            {100000, 220000, 400000, 1000000}, /* hotel */
            {1100000, 2500000, 5000000, 9000000} /* bank */
    };
    public static final int[][] incomeFromHouses = {
            {5, 15, 35, 70, 120},
            {20, 60, 140, 300, 400},
            {80, 240, 560, 1100, 1600},
            {350, 1050, 2500, 5000, 7500},
            {1500, 4500, 10000, 20000, 30000}
    };
    public static final int[] houseTimer = {
            300,
            450,
            600,
            750,
            900
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
