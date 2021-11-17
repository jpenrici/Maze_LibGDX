package com.mazegdx.game;

public class MazeTest {

    public static void main(String[] args) {

        MazeMatrix mazeMatrix;
        mazeMatrix = new MazeMatrix(10);
        view(mazeMatrix);
        mazeMatrix.reverse();
        view(mazeMatrix);

        mazeMatrix = new MazeMatrix(10, "spiral");
        view(mazeMatrix);
        mazeMatrix.reverse();
        view(mazeMatrix);
        mazeMatrix.flip();
        view(mazeMatrix);
        mazeMatrix.flip(true);
        view(mazeMatrix);

        mazeMatrix.basic();
        view(mazeMatrix);
    }

    private static void view(MazeMatrix mazeMatrix) {

        int[][] matrix = mazeMatrix.getMatrix();
        System.out.println();
        for (int y = 0; y < mazeMatrix.getSize(); ++y) {
            for (int x = 0; x < mazeMatrix.getSize(); ++x) {
                switch (matrix[y][x]) {
                    case MazeMatrix.WALL:
                        System.out.print("#");
                        break;
                    case MazeMatrix.FREE:
                        System.out.print(".");
                        break;
                    case MazeMatrix.BEGIN:
                        System.out.print("B");
                        break;
                    case MazeMatrix.END:
                        System.out.print("E");
                        break;
                }
            }
            System.out.println();
        }
    }

}
