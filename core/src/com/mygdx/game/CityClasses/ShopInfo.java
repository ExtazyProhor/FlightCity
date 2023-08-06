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
            8000, /* bank */
            20000  /* hotel */
    };
    public static final int maxLevel = 4;
    public static final int[][] upgradeCost = {
            {100, 900, 5000, 30000}, /* house */
            {700, 3000, 15000, 75000}, /* shop */
            {2000, 6000, 50000, 120000}, /* restaurant */
            {10000, 25000, 70000, 250000}, /* bank */
            {35000, 150000, 400000, 950000} /* hotel */
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
            1500000
    };
}
