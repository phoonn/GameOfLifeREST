package com.gameoflife.app.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;

@SpringBootTest
class GameOfLifeServiceTest {

    @Autowired
    GameOfLifeService gameService;

    /**
     * Test that one seed always generates the same board.
     */
    @Test
    void testGeneration() {

        final long seed = 23213L;

        final boolean[][] firstBoard = gameService.initializeBoard(10, 10, seed);

        final boolean[][] secondBoard = gameService.initializeBoard(10, 10, seed);

        final boolean[][] thirdBoard = gameService.initializeBoard(10, 10, seed);

        assertTrue(Arrays.deepEquals(firstBoard, secondBoard));
        assertTrue(Arrays.deepEquals(firstBoard, thirdBoard));
    }

    @Test
    void testBoardIteration() {
        //Test: alive -> dead, because of too little alive neighbours 
        final boolean[][] firstBoard = new boolean[][]{
                new boolean[]{false, true, false},
                new boolean[]{false, true, false},
                new boolean[]{false, false, false}
        };
        final boolean[][] firstBoardExpected = new boolean[][]{
                new boolean[]{false, false, false}, // i=0; j=1 has only 1 alive neighbour, so will die. 
                new boolean[]{false, false, false}, // i=1; j=1 has only 1 alive neighbour, so will die. 
                new boolean[]{false, false, false}
        };
        final boolean[][] firstBoardResult = gameService.iterateBoard(firstBoard, 1);

        assertTrue(Arrays.deepEquals(firstBoardResult, firstBoardExpected));

        //Test: dead -> alive, because of alive neighbours
        final boolean[][] secondBoard = new boolean[][]{
                new boolean[]{false, true, true},
                new boolean[]{false, true, false},
                new boolean[]{false, false, false}
        };
        final boolean[][] secondBoardExpected = new boolean[][]{
                new boolean[]{false, true, true}, // i=0; j=1 and i=0;j=2 have both two neighbours, so will both survive. 
                new boolean[]{false, true, true}, // i=1; j=1 has two neighbours, so will survive. i=1; j=2 has three alive neighbours, so it will become alive.
                new boolean[]{false, false, false} // everything else remains dead. 
        };
        final boolean[][] secondBoardResult = gameService.iterateBoard(secondBoard, 1);

        assertTrue(Arrays.deepEquals(secondBoardResult, secondBoardExpected));

        //Test: dead -> alive, because of too many alive neighbours. 
        final boolean[][] thirdBoard = new boolean[][]{
                new boolean[]{false, true, true},
                new boolean[]{false, true, true},
                new boolean[]{true, false, false}
        };
        final boolean[][] thirdBoardExpected = new boolean[][]{
                new boolean[]{false, true, true}, // i=0; j=1 and i=0;j=2 have both three neighbours, so will both survive. 
                new boolean[]{true, false, true}, // i=1; j=0 has three alive neighbours, so will become alive. i=1; j=1 has four neighbours, so will die. i=1; j=2 has three alive neighbours, so it will remain alive.
                new boolean[]{false, true, false} // i=2; j=0 has only one alive neighbour, so will die. i=2; j=1 has three alive neighbours, so will become alive. 
        };
        final boolean[][] thirdBoardResult = gameService.iterateBoard(thirdBoard, 1);

        assertTrue(Arrays.deepEquals(thirdBoardResult, thirdBoardExpected));

        //Test: dead board will not become alive.
        final boolean[][] fourthBoard = new boolean[][]{
                new boolean[]{false, false, false},
                new boolean[]{false, false, false},
                new boolean[]{false, false, false}
        };
        final boolean[][] fourthBoardExpected = new boolean[][]{
                new boolean[]{false, false, false},
                new boolean[]{false, false, false},
                new boolean[]{false, false, false}
        };
        final boolean[][] fourthBoardResult = gameService.iterateBoard(fourthBoard, 1);

        assertTrue(Arrays.deepEquals(fourthBoardResult, fourthBoardExpected));
    }

}
