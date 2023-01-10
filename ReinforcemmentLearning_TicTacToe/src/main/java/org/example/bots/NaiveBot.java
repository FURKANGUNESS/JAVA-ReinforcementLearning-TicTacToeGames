package org.example.bots;


import org.example.Board;
import org.example.Move;
import org.example.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author furkangunes
 */
public class NaiveBot {
    protected final int color;
    protected final Board board;

    public NaiveBot(int color, Board board) {
        this.color = color;
        this.board = board;
    }

    /**
     *
     */
    public void act() {
        int state = getState();

        Set<Integer> possibleActions = getPossibleActions();
        List<Integer> possibleActionList = new ArrayList<>(possibleActions);

        int action = -1;
        if (!possibleActions.isEmpty()) {
            action = possibleActionList.get(random.nextInt(possibleActionList.size()));
            Position pos = Position.fromInteger(board, action);
            board.move(pos, color);
        }

        if (action != -1) {
            int newState = getState();
            moves.add(new Move(state, action, newState, 0));
        }
    }

    /**
     * Bot stratejileri guncellenir
     */
    public void updateStrategy() {

    }


    protected List<Move> moves = new ArrayList<>();
    protected static Random random = new Random(42);

    protected double[] REWARD = new double[3];

    protected int getStrategyColor(int boardColor) {
        if (boardColor == color) return 1;
        else if (boardColor == 0) return 0;
        else return 2;
    }

    public int getState() {
        int state = 0;
        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.size(); ++j) {
                int strategyColor = getStrategyColor(board.getCell(i, j));
                state = state * 3 + strategyColor;
            }
        }
        return state;
    }

    public Set<Integer> getPossibleActions() {
        List<Position> actions = board.getPossibleActions();
        return actions.stream().map(pos -> pos.toInteger(board)).collect(Collectors.toSet());
    }

    /**
     * Board ve ogrenme resetlenir.
     */
    public void clearHistory() {
        moves.clear();
    }


}
