package com.mazegdx.game;

public class MazeMatrix {

    private static final int MINIMUM_LIMIT = 8;
    protected static final int ERROR = -1;
    protected static final int FREE = 0;
    protected static final int WALL = 1;
    protected static final int BEGIN = 2;
    protected static final int END = 3;
    private int[][] matrix;  // matrix[y][x]
    private int size;

    public MazeMatrix(int size) {
        setMaze(size, "basic");
    }

    public MazeMatrix(int size, String model) {
        setMaze(size, model);
    }

    private void setMaze(int size, String model) {
        size = Math.max(size, MINIMUM_LIMIT);
        this.size = size;
        if ("spiral".equals(model)) {
            spiral();
        } else {
            basic();
        }
    }

    public int getSize() {
        return size;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getValue(int y, int x) {
        if (x < 0 || x > size -1 || y < 0 || y > size - 1) {
            return ERROR;
        }
        return matrix[y][x];
    }

    private void initialize() {
        matrix = new int[size][size];
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                matrix[y][x] = FREE;
            }
        }
    }

    public void reverse() {
        int[][] temp = new int[size][size];
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                temp[y][x] = matrix[x][y];
            }
        }
        matrix = temp;
    }

    public void flip(Boolean vertical) {
        int[][] temp = new int[size][size];
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                if (vertical) {
                    temp[y][x] = matrix[size - 1 - y][x];
                } else {
                    temp[y][x] = matrix[y][size - 1 - x];
                }
            }
        }
        matrix = temp;
    }

    public void flip() {
        flip(false);
    }

    public void basic() {
        if (size % 2 == 0) {
            size = size + 1;
        }
        initialize();
        int left = 2;
        int right = size - 3;
        for (int y = 0; y < size; ++y) {
            matrix[y][0] = WALL;
            matrix[y][size - 1] = WALL;
            if (y % 2 == 0) {
                int p = left + (int) (Math.random() * ((right - left) + 1));
                for (int x = 0; x < size; ++x) {
                    if (x == p) {
                        if (y == 0) {
                            matrix[y][x] = BEGIN;
                        }
                        if (y == size - 1) {
                            matrix[y][x] = END;
                        }
                    } else {
                        matrix[y][x] = WALL;
                    }
                }
            }
        }
    }

    public void spiral() {
        initialize();
        int step = 0;
        int x = 0;
        int y = 0;
        int left = 0;
        int right = size - 1;
        int up = 0;
        int down = size - 1;
        do {
            matrix[y][x] = WALL;
            if (step % 4 == 0) {
                if (x == right) {
                    right -= 2;
                    step += 1;
                } else {
                    x += 1;
                }
            }
            if (step % 4 == 1) {
                if (y == down) {
                    down -= 2;
                    step += 1;
                } else {
                    y += 1;
                }
            }
            if (step % 4 == 2) {
                if (x == left) {
                    left += 2;
                    step += 1;
                } else {
                    x -= 1;
                }
            }
            if (step % 4 == 3) {
                if (y == up) {
                    up += 2;
                    step += 1;
                } else {
                    y -= 1;
                }
            }
        } while (up < down);
        matrix[y][x] = BEGIN;
        matrix[0][size - 2] = END;
    }
}
