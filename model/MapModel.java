package model;



/**
 * This class is to record the map of one game. For example:
 */
public class MapModel {
    int[][] matrix;
    private int[][]originalMatrix;



    public MapModel(int[][] matrix) {

        this.matrix = matrix;
        this.originalMatrix=copyMatrix(matrix);
    }
    public void resetOriginalMatrix(){
        this.matrix=copyMatrix(originalMatrix);
    }

    public int[][] copyMatrix(int[][] matrix){
        int [][]copy=new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                copy[i][j]=matrix[i][j];
            }
        }return copy;
    }
    public void setMatrix(int[][]matrix){
        this.matrix=matrix;
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }
}
