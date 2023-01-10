package org.example.learning.rlearn;


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
 * R ogrenme modelinin algoritmasidir.
 */
public class RLearner implements Serializable, Cloneable {

    private QModel model;
    private ActionSelectionStrategy actionSelectionStrategy;
    private double rho;
    private double beta;

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public static RLearner fromJson(String json) {
        return JSON.parseObject(json, RLearner.class);
    }

    public RLearner makeCopy() {
        RLearner clone = new RLearner();
        clone.copy(this);
        return clone;
    }

    /**
     * @param rhs
     */
    public void copy(RLearner rhs) {
        model = rhs.model.makeCopy();
        actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
        rho = rhs.rho;
        beta = rhs.beta;
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof RLearner) {
            RLearner rhs = (RLearner) obj;
            if (!model.equals(rhs.model)) return false;
            if (!actionSelectionStrategy.equals(rhs.actionSelectionStrategy)) return false;
            if (rho != rhs.rho) return false;
            return beta == rhs.beta;
        }
        return false;
    }

    public RLearner() {

    }

    /**
     * @return
     */
    public double getRho() {
        return rho;
    }

    /**
     * @param rho
     */
    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
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

    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(actionSelectionStrategy);
    }

    public void setActionSelection(String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }

    /**
     * @param stateCount
     * @param actionCount
     */
    public RLearner(int stateCount, int actionCount) {
        this(stateCount, actionCount, 0.1, 0.1, 0.7, 0.1);
    }

    /**
     * @param state_count
     * @param action_count
     * @param alpha
     * @param beta
     * @param rho
     * @param initial_Q
     */
    public RLearner(int state_count, int action_count, double alpha, double beta, double rho, double initial_Q) {
        model = new QModel(state_count, action_count, initial_Q);
        model.setAlpha(alpha);

        this.rho = rho;
        this.beta = beta;

        actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    /**
     * @param stateId
     * @param actionsAtState
     * @return
     */
    private double maxQAtState(int stateId, Set<Integer> actionsAtState) {
        IndexValue iv = model.actionWithMaxQAtState(stateId, actionsAtState);
        double maxQ = iv.getValue();
        return maxQ;
    }

    /**
     * @param currentState
     * @param actionTaken
     * @param newState
     * @param actionsAtNextStateId
     * @param immediate_reward
     */
    public void update(int currentState, int actionTaken, int newState, Set<Integer> actionsAtNextStateId, double immediate_reward) {
        double oldQ = model.getQ(currentState, actionTaken);

        double alpha = model.getAlpha(currentState, actionTaken); // learning rate;

        double maxQ = maxQAtState(newState, actionsAtNextStateId);

        double newQ = oldQ + alpha * (immediate_reward - rho + maxQ - oldQ);

        double maxQAtCurrentState = maxQAtState(currentState, null);
        if (newQ == maxQAtCurrentState) {
            rho = rho + beta * (immediate_reward - rho + maxQ - maxQAtCurrentState);
        }

        model.setQ(currentState, actionTaken, newQ);
    }

    /**
     * @param stateId
     * @param actionsAtState
     * @return
     */
    public IndexValue selectAction(int stateId, Set<Integer> actionsAtState) {
        return actionSelectionStrategy.selectAction(stateId, model, actionsAtState);
    }
}
