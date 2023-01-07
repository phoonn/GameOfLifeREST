package com.gameoflife.app.api;

import com.gameoflife.app.service.GameOfLifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/game")
public class GameOfLifeController {

    @Autowired
    GameOfLifeService gameService;

    /**
     * Play game of life in a board with dimensions, seed and number of iterations given by the user.
     * @return String state of the board after all iterations have been completed. 
     * */
    @GetMapping("/play")
    public ResponseEntity<String> playGame(@RequestParam("height") int height, @RequestParam("width") int width, @RequestParam("seed") int seed, @RequestParam("iterations") int iterations) {
        if (height < GameOfLifeService.MIN_GRID) {
            return new ResponseEntity<>("Height of board can't be less than " + GameOfLifeService.MIN_GRID, HttpStatus.BAD_REQUEST);
        }
        if (width < GameOfLifeService.MIN_GRID) {
            return new ResponseEntity<>("Width of board can't be less than " + GameOfLifeService.MIN_GRID, HttpStatus.BAD_REQUEST);
        }
        
        final boolean[][] startingBoard = gameService.initializeBoard(height, width, seed);
        final boolean[][] boardAfterIterations = gameService.iterateBoard(startingBoard, iterations);
        final String printedBoard = gameService.printBoard(boardAfterIterations);
        
        return new ResponseEntity<>(printedBoard, HttpStatus.OK);
    }
}
