package org.example.learning.qlearn;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.actionselection.AbstractActionSelectionStrategy;
import org.example.actionselection.ActionSelectionStrategy;
import org.example.actionselection.ActionSelectionStrategyFactory;
import org.example.actionselection.EpsilonGreedyActionSelectionStrategy;
import org.example.models.QModel;
import org.example.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;

/**
 * @author furkangunes
 */

public class QLearner implements Serializable, Cloneable {
    protected QModel model;

    private ActionSelectionStrategy actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();

    /**
     * @return Q ogrenme modelinin kopyasini olusturur.
     */
    public QLearner makeCopy() {
        QLearner clone = new QLearner();
        clone.copy(this);
        return clone;
    }

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public static QLearner fromJson(String json) {
        return JSON.parseObject(json, QLearner.class);
    }

    /**
     * @param rhs
     */
    public void copy(QLearner rhs) {
        model = rhs.model.makeCopy();
        actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof QLearner) {
            QLearner rhs = (QLearner) obj;
            if (!model.equals(rhs.model)) return false;
            return actionSelectionStrategy.equals(rhs.actionSelectionStrategy);
        }
        return false;
    }

    /**
     * @return
     */
    public QModel getModel() {
        return model;
    }

    /**
     * @param model
     */
    public void setModel(QModel model) {
        this.model = model;
    }

    /**
     * @return
     */
    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(actionSelectionStrategy);
    }

    public void setActionSelection(String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }

    public QLearner() {

    }

    /**
     * @param stateCount
     * @param actionCount
     */
    public QLearner(int stateCount, int actionCount) {
        this(stateCount, actionCount, 0.1, 0.7, 0.1);
    }

    public QLearner(QModel model, ActionSelectionStrategy actionSelectionStrategy) {
        this.model = model;
        this.actionSelectionStrategy = actionSelectionStrategy;
    }

    /**
     * @param stateCount
     * @param actionCount
     * @param alpha
     * @param gamma
     * @param initialQ
     */
    public QLearner(int stateCount, int actionCount, double alpha, double gamma, double initialQ) {
        model = new QModel(stateCount, actionCount, initialQ);
        model.setAlpha(alpha);
        model.setGamma(gamma);
        actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    /**
     * @param stateId
     * @param actionsAtState
     * @return
     */
    protected double maxQAtState(int stateId, Set<Integer> actionsAtState) {
        IndexValue iv = model.actionWithMaxQAtState(stateId, actionsAtState);
        double maxQ = iv.getValue();
        return maxQ;
    }

    /**
     * @param stateId
     * @param actionsAtState
     * @return
     */
    public IndexValue selectAction(int stateId, Set<Integer> actionsAtState) {
        return actionSelectionStrategy.selectAction(stateId, model, actionsAtState);
    }

    /**
     * @param stateId
     * @return
     */
    public IndexValue selectAction(int stateId) {
        return selectAction(stateId, null);
    }

    /**
     * @param stateId
     * @param actionId
     * @param nextStateId
     * @param immediateReward
     */
    public void update(int stateId, int actionId, int nextStateId, double immediateReward) {
        update(stateId, actionId, nextStateId, null, immediateReward);
    }

    /**
     * @param stateId
     * @param actionId
     * @param nextStateId
     * @param actionsAtNextStateId
     * @param immediateReward
     */
    public void update(int stateId, int actionId, int nextStateId, Set<Integer> actionsAtNextStateId, double immediateReward) {

        double oldQ = model.getQ(stateId, actionId);

        double alpha = model.getAlpha(stateId, actionId);

        double gamma = model.getGamma();

        double maxQ = maxQAtState(nextStateId, actionsAtNextStateId);

        double newQ = oldQ + alpha * (immediateReward + gamma * maxQ - oldQ);

        model.setQ(stateId, actionId, newQ);
    }


}
