package org.example.utils;

import java.util.List;

/**
 * @author furkangunes
 */
public class MatrixUtils {
    /**
     * Vektor kolon listesini matrise cevirir.
     */
    public static Matrix matrixFromColumnVectors(List<Vec> R) {
        int n = R.size();
        int m = R.get(0).getDimension();

        Matrix T = new Matrix(m, n);
        for (int c = 0; c < n; ++c) {
            Vec Rcol = R.get(c);
            for (int r : Rcol.getData().keySet()) {
                T.set(r, c, Rcol.get(r));
            }
        }
        return T;
    }
}
