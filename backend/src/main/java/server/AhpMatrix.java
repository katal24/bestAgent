package server;

import java.util.Arrays;

/**
 * Created by dawid on 26.11.16.
 */
public class AhpMatrix {
    String name;
    double[][] matrix;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        return "AhpMatrix{" +
                "name='" + name + '\'' +
                ", matrix=" + Arrays.deepToString(matrix) +
                '}';
    }
}
