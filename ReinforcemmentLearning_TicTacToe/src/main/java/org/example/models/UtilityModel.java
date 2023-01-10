package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.utils.Vec;

import java.io.Serializable;


/**
 * @author furkangunes

 * (s) = durum (State)
 * U(s) = durumun fayda değeri (Utility Value of State)
 * Bir durumun fayda değeri U(s), ödül dizisi ve optimal politika verildiğinde, verilen durumun(s) beklenen uzun vadeli ödülüdür.
 * (s) durumundaki U(s) fayda değeri , Bellman denklemi ile elde edilebilir.
 * Belman denklemi => U(s) = R(s) + \gamma * max_a \sum_{s'} T(s,a,s')U(s')
 * a = eylem (Action)
 * s' = a eylemi ve s durumunda oluşacak geçiş durumu.
 * T(s,a,s') = geçiş olasılığı (Transition Probability).
 * Geçiş olasılığı, s durumuna a eylemi uygulandıktan sonra s' geçiş durumuna geçme olasılığı
 * sum_{s'} T(s,a,s')U(s') = a eyleminin s durumda uygulandığı göz önüne alındığında beklenen uzun vadeli ödüldür.
 * max_a \sum_{s'} T(s,a,s')U(s') = seçilen optimum eylem a'nın s durumunda uygulandığı göz önüne alındığında beklenen maksimum uzun vadeli ödül.

 */
@Getter
@Setter
public class UtilityModel implements Serializable {
    private Vec U;
    private int stateCount;
    private int actionCount;

    public void setU(Vec U) {
        this.U = U;
    }

    public Vec getU() {
        return U;
    }

    public double getU(int stateId) {
        return U.get(stateId);
    }

    public int getStateCount() {
        return stateCount;
    }

    public int getActionCount() {
        return actionCount;
    }

    public UtilityModel(int stateCount, int actionCount, double initialU) {
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        U = new Vec(stateCount);
        U.setAll(initialU);
    }

    public UtilityModel(int stateCount, int actionCount) {
        this(stateCount, actionCount, 0.1);
    }

    public UtilityModel() {

    }

    /**
     *
     * @param rhs
     * param objeyi içe kopyalar.
     */
    public void copy(UtilityModel rhs) {
        U = rhs.U == null ? null : rhs.U.makeCopy();
        actionCount = rhs.actionCount;
        stateCount = rhs.stateCount;
    }

    /**
     *
     * @return UtilityModel
     * Objenin clonu'nu yaratır.
     */
    public UtilityModel makeCopy() {
        UtilityModel clone = new UtilityModel();
        clone.copy(this);
        return clone;
    }

    /**
     *
     * @param rhs
     * @return boolean
     * param obje ile eşitlik kontrolü yapar.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs != null && rhs instanceof UtilityModel) {
            UtilityModel rhs2 = (UtilityModel) rhs;
            if (actionCount != rhs2.actionCount || stateCount != rhs2.stateCount) return false;

            if ((U == null && rhs2.U != null) && (U != null && rhs2.U == null)) return false;
            return !(U != null && !U.equals(rhs2.U));

        }
        return false;
    }

    /**
     *
     * @param initialU
     * Vectör resetlenir
     */
    public void reset(double initialU) {
        U.setAll(initialU);
    }
}
