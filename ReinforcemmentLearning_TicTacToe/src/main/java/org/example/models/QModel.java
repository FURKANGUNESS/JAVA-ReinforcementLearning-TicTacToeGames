package org.example.models;


import org.example.utils.IndexValue;
import org.example.utils.Matrix;
import org.example.utils.Vec;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


/**
 * @author furkangunes
 * Q, durum-eylem kombinasyonunun kalitesi olarak bilinir, bunun bir durumun faydasından farklı olduğuna dikkat edin.
 */
@Getter
@Setter
public class QModel {
    /**
     * (state_id, action_id) çifti için Q değeri
     * Q, durum-eylem kombinasyonunun kalitesi olarak bilinir, bunun bir durumun faydasından farklı olduğuna dikkat edin.
     */
    private Matrix Q;
    /**
     * Öğrenme oranı için \alpha[s, a] değeri: alpha(state_id, action_id)
     */
    private Matrix alphaMatrix;

    /**
     * indirim faktörü
     */
    private double gamma = 0.7;

    private int stateCount;
    private int actionCount;

    public QModel(int stateCount, int actionCount, double initialQ) {
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        Q = new Matrix(stateCount, actionCount);
        alphaMatrix = new Matrix(stateCount, actionCount);
        Q.setAll(initialQ);
        alphaMatrix.setAll(0.1);
    }

    public QModel(int stateCount, int actionCount) {
        this(stateCount, actionCount, 0.1);
    }

    public QModel() {

    }

    /**
     *
     * @param rhs
     * @return boolean
     * Gelen objenin eşitlik kontrolü
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs != null && rhs instanceof QModel) {
            QModel rhs2 = (QModel) rhs;


            if (gamma != rhs2.gamma) return false;


            if (stateCount != rhs2.stateCount || actionCount != rhs2.actionCount) return false;

            if ((Q != null && rhs2.Q == null) || (Q == null && rhs2.Q != null)) return false;
            if ((alphaMatrix != null && rhs2.alphaMatrix == null) || (alphaMatrix == null && rhs2.alphaMatrix != null))
                return false;

            return !((Q != null && !Q.equals(rhs2.Q)) || (alphaMatrix != null && !alphaMatrix.equals(rhs2.alphaMatrix)));

        }
        return false;
    }

    /**
     *
     * @return QModel
     * Objenin kendisini klonlar
     */
    public QModel makeCopy() {
        QModel clone = new QModel();
        clone.copy(this);
        return clone;
    }

    /**
     *
     * @param rhs
     * Gelen objeyi içe kopyalar
     */
    public void copy(QModel rhs) {
        gamma = rhs.gamma;
        stateCount = rhs.stateCount;
        actionCount = rhs.actionCount;
        Q = rhs.Q == null ? null : rhs.Q.makeCopy();
        alphaMatrix = rhs.alphaMatrix == null ? null : rhs.alphaMatrix.makeCopy();
    }


    public double getQ(int stateId, int actionId) {
        return Q.get(stateId, actionId);
    }


    public void setQ(int stateId, int actionId, double Qij) {
        Q.set(stateId, actionId, Qij);
    }


    public double getAlpha(int stateId, int actionId) {
        return alphaMatrix.get(stateId, actionId);
    }


    public void setAlpha(double defaultAlpha) {
        this.alphaMatrix.setAll(defaultAlpha);
    }


    public IndexValue actionWithMaxQAtState(int stateId, Set<Integer> actionsAtState) {
        Vec rowVector = Q.rowAt(stateId);
        return rowVector.indexWithMaxValue(actionsAtState);
    }

    private void reset(double initialQ) {
        Q.setAll(initialQ);
    }


    public IndexValue actionWithSoftMaxQAtState(int stateId, Set<Integer> actionsAtState, Random random) {
        Vec rowVector = Q.rowAt(stateId);
        double sum = 0;

        if (actionsAtState == null) {
            actionsAtState = new HashSet<>();
            for (int i = 0; i < actionCount; ++i) {
                actionsAtState.add(i);
            }
        }

        List<Integer> actions = new ArrayList<>();
        for (Integer actionId : actionsAtState) {
            actions.add(actionId);
        }

        double[] acc = new double[actions.size()];
        for (int i = 0; i < actions.size(); ++i) {
            sum += rowVector.get(actions.get(i));
            acc[i] = sum;
        }


        double r = random.nextDouble() * sum;

        IndexValue result = new IndexValue();
        for (int i = 0; i < actions.size(); ++i) {
            if (acc[i] >= r) {
                int actionId = actions.get(i);
                result.setIndex(actionId);
                result.setValue(rowVector.get(actionId));
                break;
            }
        }

        return result;
    }
}
