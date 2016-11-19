package com.srgood.reasons.games;


import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmanl on 9/11/2016.
 */
public class ChessGame {
    private Message board;
    private Map pieces = new HashMap();

    public ChessGame(Message b) {
        board = b;
    }


}
