package com.mathesis.serviceone.controller.matrix;

import lombok.Data;

@Data
public class Matrix {
    private int nrows;
    private int ncols;
    private double[][] data;

    public Matrix(double[][] dat) {
        this.data = dat;
        this.nrows = dat.length;
        this.ncols = dat[0].length;
    }

    public Matrix(int nrow, int ncol) {
        this.nrows = nrow;
        this.ncols = ncol;
        data = new double[nrow][ncol];
    }
    public double getValueAt(int i, int j) {
        return data[i][j];
    }

    public void setValueAt(int i, int j, double valueAt) {
        this.data[i][j] = valueAt;
    }
}
