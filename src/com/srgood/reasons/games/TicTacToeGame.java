package com.srgood.reasons.games;

import net.dv8tion.jda.core.entities.MessageChannel;
import java.util.ArrayList;

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
    public boolean turn = false;
    public boolean ready = false;
    public String[] boardFormat = {" ", " | ", " | ", "\n-----------\n"};
    public MessageChannel channel;
    public String output = "";
    public boolean dead = false;
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
    public play(int X, int Y) {
        x = Arrays.copyOf(x, x.length+1);
        x[x.length-1] = magicSquare[X][Y];
        private int valid = true
        for(int i = 0; i < o.length; i++) {
            if(x[x.length-1] == o[i])
                valid = false;
        }
        for(int i = 0; i < x.length - 1; i++) {
            if(x[x.length-1] == x[i])
                valid = false;
        }
        if(valid) {
            turn = true;
        } else {
            x = Arrays.copyOf(x, x.length-1);
            //Output a reprimand to discord
        }
    }
    public AIplay() {
        o = Arrays.copyOf(o, o.length+1);
        private int out;
        private int finout;
        if(x.length > 1) {
            //check if there's a winning move, if so play- see below
            for(int i = 0; i < x.length; i++) {
                for(int j = 0; j < x.length) {
                    //check if there's a blocking move, if so play- code written, too lazy to transfer rn
                }
            }
        } else {
            private boolean loop = true;
            while(loop) {
                o[o.length] = Math.floor(Math.random()*9)+1;
                loop = false;
                for(int i = 0; i < x.length; i++) {
                    if(o[o.length-1] == x[i])
                        loop = true;
                }
                for(int i = 0; i < o.length - 1; i++) {
                    if(o[o.length-1] == o[i])
                        loop = true;
                }
            }
        }
        turn = false;
    }
    public drawBoard() {
        //compile board variable and output to Discord
    }
    public checkWin() {
        //Check if either player has won.  if so, output respective message to Discord and kill instance
    }
}
