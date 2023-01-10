package org.example.actionselection;


import org.example.models.QModel;
import org.example.utils.IndexValue;

import java.util.Random;
import java.util.Set;

/**
 * SoftMaxActionSelectionStrategy class
 * @author furkangunes 
 *
 */
public class SoftMaxActionSelectionStrategy extends AbstractActionSelectionStrategy {
    private Random random = new Random();

    /**
     *
     * @return Object
     * Nesne kopyalanır
     */
    @Override
    public Object clone() {
        SoftMaxActionSelectionStrategy clone = new SoftMaxActionSelectionStrategy(random);
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
        return obj != null && obj instanceof SoftMaxActionSelectionStrategy;
    }

    public SoftMaxActionSelectionStrategy() {

    }

    public SoftMaxActionSelectionStrategy(Random random) {
        this.random = random;
    }

    @Override
    public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
        return model.actionWithSoftMaxQAtState(stateId, actionsAtState, random);
    }
}
