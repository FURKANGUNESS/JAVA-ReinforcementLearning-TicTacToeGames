package org.example.learning.actorcritic;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.actionselection.AbstractActionSelectionStrategy;
import org.example.actionselection.ActionSelectionStrategy;
import org.example.actionselection.ActionSelectionStrategyFactory;
import org.example.actionselection.GibbsSoftMaxActionSelectionStrategy;
import org.example.models.QModel;
import org.example.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Function;

/**
 * @author furkangunes
 * Actor-critic öğrenme algoritmasının bir uygulamasını temsil eden bir Java sınıfıdır.
 * Actor-critic öğrenme algoritmasını kullanarak, bir oyun gibi bir etkinlikte oyuncunun eylemlerini seçmek için kullanılabilir.
 */
public class ActorCriticLearner implements Serializable {
    protected QModel P;
    protected ActionSelectionStrategy actionSelectionStrategy;

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public static ActorCriticLearner fromJson(String json) {
        return JSON.parseObject(json, ActorCriticLearner.class);
    }

    /**
     * @return Kendisinin bir kopyasını oluşturarak ve başka bir actor-critic öğrenme algoritması nesnesinin özelliklerini kendi özelliklerine kopyalar.
     */
    public Object makeCopy() {
        ActorCriticLearner clone = new ActorCriticLearner();
        clone.copy(this);
        return clone;
    }

    public void copy(ActorCriticLearner rhs) {
        P = rhs.P.makeCopy();
        actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ActorCriticLearner) {
            ActorCriticLearner rhs = (ActorCriticLearner) obj;
            return P.equals(rhs.P) && getActionSelection().equals(rhs.getActionSelection());
        }
        return false;
    }

    /**
     * QModel sınıfını kullanarak davranış seçicisini temsil eder ve ActionSelectionStrategy sınıfını kullanarak değer öğrenicisini temsil eder.
     */
    public ActorCriticLearner() {

    }

    public ActorCriticLearner(int stateCount, int actionCount) {
        this(stateCount, actionCount, 1, 0.7, 0.01);
    }

    public int selectAction(int stateId, Set<Integer> actionsAtState) {
        IndexValue iv = actionSelectionStrategy.selectAction(stateId, P, actionsAtState);
        return iv.getIndex();
    }

    public int selectAction(int stateId) {
        return selectAction(stateId, null);
    }

    public ActorCriticLearner(int stateCount, int actionCount, double beta, double gamma, double initialP) {
        P = new QModel(stateCount, actionCount, initialP);
        P.setAlpha(beta);
        P.setGamma(gamma);

        actionSelectionStrategy = new GibbsSoftMaxActionSelectionStrategy();
    }

    public void update(int currentStateId, int currentActionId, int newStateId, double immediateReward, Function<Integer, Double> V) {
        update(currentStateId, currentActionId, newStateId, null, immediateReward, V);
    }

    public void update(int currentStateId, int currentActionId, int newStateId, Set<Integer> actionsAtNewState, double immediateReward, Function<Integer, Double> V) {
        double td_error = immediateReward + V.apply(newStateId) - V.apply(currentStateId);

        double oldP = P.getQ(currentStateId, currentActionId);
        double beta = P.getAlpha(currentStateId, currentActionId);
        double newP = oldP + beta * td_error;
        P.setQ(currentStateId, currentActionId, newP);
    }

    /**
     * @return Bir etkinlikte oyuncunun eylemlerini seçmek için, bir öğrenme algoritması olarak actor-critic öğrenme algoritmasını kullaniyoruz.
     */
    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(actionSelectionStrategy);
    }

    public void setActionSelection(String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }


    public QModel getP() {
        return P;
    }

    public void setP(QModel p) {
        P = p;
    }
}
