package org.example.learning.qlearn;

import org.example.models.EligibilityTraceUpdateMode;
import org.example.utils.Matrix;

import java.util.Set;

/**
 * @author furkangunes
 * Q-öğrenme yöntemine dayanan bir öğrenme algoritması olan Lambda Q-öğrenme yöntemini gerçekleştirir.
 */
public class QLambdaLearner extends QLearner {
    private double lambda = 0.9;
    private Matrix e;
    private EligibilityTraceUpdateMode traceUpdateMode = EligibilityTraceUpdateMode.ReplaceTrace;

    /**
     * @return
     */
    public EligibilityTraceUpdateMode getTraceUpdateMode() {
        return traceUpdateMode;
    }

    /**
     * @param traceUpdateMode
     */
    public void setTraceUpdateMode(EligibilityTraceUpdateMode traceUpdateMode) {
        this.traceUpdateMode = traceUpdateMode;
    }

    /**
     * @return
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * @param lambda
     */
    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    /**
     * @return
     */
    public QLambdaLearner makeCopy() {
        QLambdaLearner clone = new QLambdaLearner();
        clone.copy(this);
        return clone;
    }

    /**
     * @param rhs
     */
    @Override
    public void copy(QLearner rhs) {
        super.copy(rhs);

        QLambdaLearner rhs2 = (QLambdaLearner) rhs;
        lambda = rhs2.lambda;
        e = rhs2.e.makeCopy();
        traceUpdateMode = rhs2.traceUpdateMode;
    }

    /**
     * @param learner
     */
    public QLambdaLearner(QLearner learner) {
        copy(learner);
        e = new Matrix(model.getStateCount(), model.getActionCount());
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (obj instanceof QLambdaLearner) {
            QLambdaLearner rhs = (QLambdaLearner) obj;
            return rhs.lambda == lambda && e.equals(rhs.e) && traceUpdateMode == rhs.traceUpdateMode;
        }

        return false;
    }

    public QLambdaLearner() {
        super();
    }

    /**
     * @param stateCount
     * @param actionCount
     */
    public QLambdaLearner(int stateCount, int actionCount) {
        super(stateCount, actionCount);
        e = new Matrix(stateCount, actionCount);
    }

    /**
     * @param stateCount
     * @param actionCount
     * @param alpha
     * @param gamma
     * @param initialQ
     */
    public QLambdaLearner(int stateCount, int actionCount, double alpha, double gamma, double initialQ) {
        super(stateCount, actionCount, alpha, gamma, initialQ);
        e = new Matrix(stateCount, actionCount);
    }

    /**
     * @return
     */
    public Matrix getEligibility() {
        return e;
    }

    /**
     * @param e
     */
    public void setEligibility(Matrix e) {
        this.e = e;
    }

    /**
     * @param currentStateId
     * @param currentActionId
     * @param nextStateId
     * @param actionsAtNextStateId
     * @param immediateReward
     */
    @Override
    public void update(int currentStateId, int currentActionId, int nextStateId, Set<Integer> actionsAtNextStateId, double immediateReward) {
        double oldQ = model.getQ(currentStateId, currentActionId);

        double alpha = model.getAlpha(currentStateId, currentActionId);

        double gamma = model.getGamma();

        double maxQ = maxQAtState(nextStateId, actionsAtNextStateId);

        double td_error = immediateReward + gamma * maxQ - oldQ;

        int stateCount = model.getStateCount();
        int actionCount = model.getActionCount();

        e.set(currentStateId, currentActionId, e.get(currentStateId, currentActionId) + 1);


        for (int stateId = 0; stateId < stateCount; ++stateId) {
            for (int actionId = 0; actionId < actionCount; ++actionId) {
                oldQ = model.getQ(stateId, actionId);
                double newQ = oldQ + alpha * td_error * e.get(stateId, actionId);

                // new_value is $Q_{t+1}(s_t, a_t)$
                model.setQ(currentStateId, currentActionId, newQ);

                if (actionId != currentActionId) {
                    e.set(currentStateId, actionId, 0);
                } else {
                    e.set(stateId, actionId, e.get(stateId, actionId) * gamma * lambda);
                }
            }
        }


    }

}
