package com.gameoflife.app.service;

import org.springframework.stereotype.Service;
import java.util.Random;

/**
 * Game of life service, which can generate a new board according to given dimensions and seed,
 * Iterate the board,
 * Convert the state of the current board to a string. 
 * 
 * Rules of game of life:
 * Any live cell with two or three live neighbours survives.
 * Any dead cell with three live neighbours becomes a live cell.
 * All others live cells die in the next generation. Similarly, all other dead cells stay dead.
 * */
@Service
public class GameOfLifeService {

    private final static int MAX_ALIVE = 3;
    private final static int MIN_ALIVE = 2;
    
    public final static int MIN_GRID = 3;

    /**
     * Initialize board with given width and height using a random seed.
     *
     * @return boolean matrix that represents a pseudo-randomly generated board.
     */
    public boolean[][] initializeBoard(int height, int width, long seed) {
        final boolean[][] board = new boolean[height][width];
        final Random rand = new Random(seed);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final boolean randomVal = rand.nextBoolean();
                board[i][j] = randomVal;
            }
        }
        return board;
    }

    /**
     * Iterate the board using the game of life rules.
     * @param iterations number of times the game will be played. 
     * @return board after all iterations have been completed. 
     * */
    public boolean[][] iterateBoard(boolean[][] currentBoard, int iterations) {
        if (iterations < 1) {
            return currentBoard;
        }
        boolean[][] temporaryBoard = null;
        for (int i = 0; i < iterations; i++) {
            temporaryBoard = currentBoard.clone();
            currentBoard = iterateBoardOnce(temporaryBoard);
        }
        return currentBoard;
    }
    
    /**
     * Visualize the current state of the board.
     * False entries will be visualized with '[]'
     * True entries will be visualized with '[X]'
     * */
    public String printBoard(boolean[][] board){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j< board[0].length; j++){
                builder.append(board[i][j] ? "[X]" : "[ ]");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Play the game once.
     * Iterate the whole board and apply the following rules, thus creating a new board:
     * 
     * Any live cell with two or three live neighbours survives.
     * Any dead cell with three live neighbours becomes a live cell.
     * All others live cells die in the next generation. Similarly, all other dead cells stay dead.
     *
     * @return state of the board after one iteration
     */
    private boolean[][] iterateBoardOnce(boolean[][] currentBoard) {
        boolean[][] newBoard = new boolean[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                final int aliveNeighbours = calculateAliveNeighbours(currentBoard, i, j);
                final boolean currentPositionState = currentBoard[i][j];
                //If current position is alive
                if (currentPositionState) {
                    newBoard[i][j] = aliveNeighbours >= MIN_ALIVE && aliveNeighbours <= MAX_ALIVE;
                } else {
                    if (aliveNeighbours == MAX_ALIVE) {
                        newBoard[i][j] = true;
                    } else {
                        newBoard[i][j] = currentBoard[i][j];
                    }
                }
            }
        }
        return newBoard;
    }

    /**
     * Retrieve the state of a given position on the board, represented in an integer
     *
     * @return 1 - if position is true
     * 0 - if position is false
     * @throws IllegalArgumentException if illegal position was passed to the method.
     */
    private int getPositionStateInInt(boolean[][] board, int height, int width) {
        if (height > board.length || height < 0) {
            throw new IllegalArgumentException("Trying to access board member which does not exist");
        }
        if (width > board[0].length || width < 0) {
            throw new IllegalArgumentException("Trying to access board member which does not exist");
        }
        return board[height][width] ? 1 : 0;
    }

    /**
     * Method which calculates how many alive neighbours a cell has.
     */
    private int calculateAliveNeighbours(boolean[][] board, int i, int j) {
        int neighbors = 0;
        final int left = j - 1;
        final int up = i - 1;
        final int right = j + 1;
        final int down = i + 1;
        final int boardWidth = board[0].length;
        final int boardHeight = board.length;
        //Check the three left neighbours
        if (left >= 0) {
            neighbors += getPositionStateInInt(board, i, left);
            if (up >= 0) {
                neighbors += getPositionStateInInt(board, up, left);
            }
            if (down < boardHeight) {
                neighbors += getPositionStateInInt(board, down, left);
            }
        }
        //Check three right neighbours
        if (right < boardWidth) {
            neighbors += getPositionStateInInt(board, i, right);
            if (up >= 0) {
                neighbors += getPositionStateInInt(board, up, right);
            }
            if (down < boardHeight) {
                neighbors += getPositionStateInInt(board, down, right);
            }
        }
        //Check cell above
        if (up >= 0) {
            neighbors += getPositionStateInInt(board, up, j);
        }
        //Check cell below
        if (down < boardHeight) {
            neighbors += getPositionStateInInt(board, down, j);
        }

        return neighbors;//returns the amount of neighbors
    }

}
