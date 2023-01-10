package org.example;

import lombok.Getter;
import lombok.Setter;

/**
 * @author furkangunes
 */
@Getter
@Setter
public class Position {
    private int x;
    private int y;
    public int BOUND;

    public Position() {

    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param board
     * @return Pozisyonlari numaralandirir.
     */
    public int toInteger(Board board) {
        return x * board.size() + y;
    }

    /**
     * @param board
     * @param intValue
     * @return Pozisyon numaralarini x ve y koordinatlarina dondurur.
     */
    public static Position fromInteger(Board board, int intValue) {
        int x = (int) Math.floor((double) intValue / board.size());
        int y = intValue - x * board.size();
        return new Position(x, y);
    }
}
