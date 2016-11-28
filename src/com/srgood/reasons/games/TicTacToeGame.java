package com.srgood.reasons.games;

/**
 * Created by William Herron on 11/27/2016.
 */
public class TicTacToeGame {
    public final int[][] magicSquare = {
        {4, 9, 2},
        {3, 5, 7},
        {8, 1, 6}
    };
    public String[][] board = {
        {" ", " ", " "},
        {" ", " ", " "},
        {" ", " ", " "}
    };
    public int[] x = {};
    public int[] o = {};
    public String[] boardFormat = {" ", " | ", " | ", "-----------"};
    public TicTacToeGame() {

    }
}
