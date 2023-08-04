package com.mygdx.game.CityClasses;

public abstract class ShopInfo {
    public static final int quantityCoins = 6;
    public static final int[] coins = {
            100,
            600,
            2000,
            10000,
            100000,
            750000
    };
    public static final int[] sapphires = {
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
            800, /* restaurant */
            1000, /* bank */
            1200  /* hotel */
    };
    public static final int maxLevel = 4;
    public static final int[][] upgradeCost = {
            {50, 200, 500, 1000}, /* house */
            {200, 700, 1500, 3500}, /* shop */
            {300, 1200, 2500, 5000}, /* restaurant */
            {600, 2000, 5000, 9000}, /* bank */
            {800, 2500, 7000, 12000} /* hotel */
    };
}
