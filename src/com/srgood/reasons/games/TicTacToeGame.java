package com.srgood.reasons.games;

import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Created by HiItsMe on 11/27/2016.
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
    public boolean ready = false;
    public String[] boardFormat = {" ", " | ", " | ", "\n-----------\n"};
    public MessageChannel channel;
    public String output = "";
    public TicTacToeGame(MessageChannel ch) {
        channel = ch;
        output = "";
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                output += boardFormat[j];
                output += board[i][j];
            }
            output += boardFormat[3];
        }
        channel.sendMessage(output).queue(m -> {
            ready = true;
        });
    }
}
