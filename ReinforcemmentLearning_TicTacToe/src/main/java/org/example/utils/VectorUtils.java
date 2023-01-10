package org.example.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author furkangunes
 */
public class VectorUtils {
    /**
     * @param vlist
     * @return 0 vektorleri listeden atar.
     */
    public static List<Vec> removeZeroVectors(Iterable<Vec> vlist) {
        List<Vec> vstarlist = new ArrayList<Vec>();
        for (Vec v : vlist) {
            if (!v.isZero()) {
                vstarlist.add(v);
            }
        }

        return vstarlist;
    }

    /**
     * @param vlist
     * @return Ikili vektor listesi yaratir.
     */
    public static TupleTwo<List<Vec>, List<Double>> normalize(Iterable<Vec> vlist) {
        List<Double> norms = new ArrayList<Double>();
        List<Vec> vstarlist = new ArrayList<Vec>();
        for (Vec v : vlist) {
            norms.add(v.norm(2));
            vstarlist.add(v.normalize());
        }

        return TupleTwo.create(vstarlist, norms);
    }


}
