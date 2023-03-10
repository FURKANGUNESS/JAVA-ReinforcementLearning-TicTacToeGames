package org.example.bots;

import org.example.Board;
import org.example.Move;
import org.example.Position;
import org.example.learning.sarsa.SarsaLearner;
import org.example.utils.IndexValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author furkangunes
 * Sarsa Bot modelidir.
 */
public class SarsaBot extends Bot {
    private final SarsaLearner agent;

    public SarsaBot(int color, Board board, SarsaLearner learner) {
        super(color, board);
        this.agent = learner;
    }

    @Override
    public void act() {
        int state = getState();

        Set<Integer> possibleActions = getPossibleActions();
        List<Integer> possibleActionList = new ArrayList<>(possibleActions);

        int action = -1;
        if (!possibleActions.isEmpty()) {
            IndexValue iv = agent.selectAction(state, possibleActions);
            action = iv.getIndex();
            double value = iv.getValue();

            if (value <= 0) {
                action = possibleActionList.get(random.nextInt(possibleActionList.size()));
            }

            Position pos = Position.fromInteger(board, action);
            board.move(pos, color);
        }

        if (action != -1) {
            int newState = getState();
            moves.add(new Move(state, action, newState, 0));
        }
    }

    @Override
    public void updateStrategy() {


        int winner = board.getWinner();
        int strategyColor = getStrategyColor(winner);

        double reward = REWARD[strategyColor];

        for (int i = moves.size() - 1; i >= 0; --i) {
            Move next_move = moves.get(i);
            if (i != moves.size() - 1) {
                next_move = moves.get(i + 1);
            }
            Move current_move = moves.get(i);
            if (i >= moves.size() - 2) {
                current_move.reward = reward;
            }
            agent.update(current_move.oldState, current_move.action, current_move.newState, next_move.action, current_move.reward);
        }

    }
}
