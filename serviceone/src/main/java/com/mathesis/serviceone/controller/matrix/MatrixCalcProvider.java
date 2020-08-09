package com.mathesis.serviceone.controller.matrix;

import org.springframework.stereotype.Service;

public class MatrixCalcProvider {
    private static Matrix transpose(Matrix matrix) {
        Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
        for (int i=0;i<matrix.getNrows();i++) {
            for (int j=0;j<matrix.getNcols();j++) {
                transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
            }
        }
        return transposedMatrix;
    }
    private static Matrix createRandomSizeMatrix() {
        int min = 0;
        int range = 4000;
        int rand = (int)(Math.random() * range) + min;
        return new Matrix(rand, rand);
    }
    public static Matrix transposeRandomMatrix() {
        Matrix matrix = createRandomSizeMatrix();
        return transpose(matrix);
    }
}
