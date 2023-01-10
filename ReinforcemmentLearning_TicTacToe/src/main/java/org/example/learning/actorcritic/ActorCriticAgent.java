package org.example.learning.actorcritic;

import org.example.utils.Vec;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Function;

/**
 * @author furkangunes
 */
public class ActorCriticAgent implements Serializable {
    private ActorCriticLearner learner;
    private int currentState;
    private int prevState;
    private int prevAction;

    /**
     *
     * @param lambda
     * Öğrenme sırasında ödül çeşitlerinin hatırlanma oranını ayarlar.
     *
     */

    public void enableEligibilityTrace(double lambda){
        ActorCriticLambdaLearner acll = new ActorCriticLambdaLearner(learner);
        acll.setLambda(lambda);
        learner = acll;
    }

    /**
     *
     * @param stateId
     * Öğrenme sırasında kullanılacak başlangıç durumunu ayarlar.
     */

    public void start(int stateId){
        currentState = stateId;
        prevAction = -1;
        prevState = -1;
    }

    /**
     *
     * @return
     * Öğrenme sırasında kullanılacak öğrenici (öğretici) modeli döndürür.
     */

    public ActorCriticLearner getLearner(){
        return learner;
    }

    /**
     *
     * @param learner
     * Öğrenme sırasında kullanılacak öğrenici (öğretici) modelini ayarlar.
     */
    public void setLearner(ActorCriticLearner learner){
        this.learner = learner;
    }

    public ActorCriticAgent(int stateCount, int actionCount){
        learner = new ActorCriticLearner(stateCount, actionCount);
    }

    public ActorCriticAgent(){

    }

    public ActorCriticAgent(ActorCriticLearner learner){
        this.learner = learner;
    }

    /**
     *
     * @return
     * Bu actorCriticAgent nesnesinin bir kopyasını oluşturur.
     */
    public ActorCriticAgent makeCopy(){
        ActorCriticAgent clone = new ActorCriticAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(ActorCriticAgent rhs){
        learner = (ActorCriticLearner)rhs.learner.makeCopy();
        prevAction = rhs.prevAction;
        prevState = rhs.prevState;
        currentState = rhs.currentState;
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null && obj instanceof ActorCriticAgent){
            ActorCriticAgent rhs = (ActorCriticAgent)obj;
            return learner.equals(rhs.learner) && prevAction == rhs.prevAction && prevState == rhs.prevState && currentState == rhs.currentState;

        }
        return false;
    }

    public int selectAction(Set<Integer> actionsAtState){
        return learner.selectAction(currentState, actionsAtState);
    }

    public int selectAction(){
        return learner.selectAction(currentState);
    }

    public void update(int actionTaken, int newState, double immediateReward, final Vec V){
        update(actionTaken, newState, null, immediateReward, V);
    }

    public void update(int actionTaken, int newState, Set<Integer> actionsAtNewState, double immediateReward, final Vec V){

        learner.update(currentState, actionTaken, newState, actionsAtNewState, immediateReward, new Function<Integer, Double>() {
            public Double apply(Integer stateId) {
                return V.get(stateId);
            }
        });

        prevAction = actionTaken;
        prevState = currentState;

        currentState = newState;
    }

}
