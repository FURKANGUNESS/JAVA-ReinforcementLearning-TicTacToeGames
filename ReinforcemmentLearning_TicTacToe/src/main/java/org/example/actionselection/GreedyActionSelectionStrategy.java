package org.example.actionselection;

import org.example.models.QModel;
import org.example.utils.IndexValue;

import java.util.Set;

/**
 * @author furkangunes
 */
public class GreedyActionSelectionStrategy extends AbstractActionSelectionStrategy {
    @Override
    public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
        return model.actionWithMaxQAtState(stateId, actionsAtState);
    }

    /**
     *
     * @return Object
     * Nesne kopyalanır
     */
    @Override
    public Object clone() {
        GreedyActionSelectionStrategy clone = new GreedyActionSelectionStrategy();
        return clone;
    }

    /**
     *
     * @param obj
     * @return boolean
     * Obje eşitlik kontrolü
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof GreedyActionSelectionStrategy;
    }
}
