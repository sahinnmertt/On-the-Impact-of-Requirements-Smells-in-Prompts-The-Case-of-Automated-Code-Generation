package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


import java.util.List;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeGameTest {

    /**
     * Requirement 1: The goal of the game is to grow the length of the snake by making it eat food until it fills the entire board.
     */

    // Maybe remove snake.growing just use body size?
    @Test
    public void testSnakeGrowth1() {


        Game game = new Game(20, "easy");

        int initialGrowSize = game.snake.body.size();
        System.out.println("-" + game.snake.body.size());
        //Eats food.
        game.food.position = new Coordinates(6, 5);
        game.snake.body.get(0).x=5;
        game.snake.body.get(0).y=5;
        game.snake.direction="RIGHT";

        game.updateGame();
        System.out.println(initialGrowSize);
        System.out.println(game.snake.growing);
        System.out.println( game.snake.body.size());

        assertTrue(initialGrowSize< game.snake.body.size());
    }

    /**
     * Requirement 2: The snake eats food when it touches the food (namely every time the snake passes over food on the board).
     */
    @Test
    public void testSnakeEatsFood2() {
        Game game = new Game(20, "easy");
        game.food.position = new Coordinates(0, 1);
        game.snake.body.get(0).x=0;
        game.snake.body.get(0).y=1;
        game.updateGame();
        assertNotEquals(game.food.position, new Coordinates(0, 1));
    }

    /**
     * Requirement 3: Every time the snake eats a piece of food, its body grows by one block which gets attached to the end of the body.
     */
    @Test
    public void testSnakeGrowsWhenEating3() {
        Game game = new Game(20, "easy");
        int initialSize = game.snake.body.size();

        //Eats food.
        game.food.position = new Coordinates(6, 5);
        game.snake.body.get(0).x=5;
        game.snake.body.get(0).y=5;
        game.snake.direction="RIGHT";

        game.updateGame();

        assertEquals(initialSize + 1, game.snake.body.size());
    }

    /**
     * Requirement 4: If the snake touches a board side, the game is over.
     * Smelly: The snake must not touch a board side.?
     */
    @Test
    public void testSnakeHitsWall4() {
        Game game = new Game(20, "easy");
        game.snake.body.set(0, new Coordinates(-1, 0));
        game.checkCollisions();

        assertTrue("Game should over after touches to the board", game.gameOver); // Resets game
    }

    /**
     * Requirement 5: If the snake touches its own body, the game is over.
     * Smelly : The game is over as soon as the snake's body is touched.
     */
    @Test
    public void testSnakeHitsItself5() {
        Game game = new Game(20, "easy");
        game.snake.body.clear();
        game.snake.body.add(0, new Coordinates(5, 5));
        game.snake.body.add(1, new Coordinates(5, 5));

        System.out.println(game.gameOver);
        System.out.println(game.snake.body.get(0));
        System.out.println(game.snake.body.get(1));
        System.out.println(game.snake.body.size());
        //game.snake.body.add(new Coordinates(0, 1));
       // game.snake.body.add(new Coordinates(0, 0)); // Moves head back on itself
        game.checkCollisions();
        assertTrue("Game should over after the snake touches to itself", game.gameOver); // Resets game
    }

    /**
     * Requirement 6: The game board consists of k x k blocks, where k shall be given as input by the user. Every block, including the food and body blocks of the snake, shall have a size of 5 x 5 pixels.
     * The game board consists of k x k blocks, where k shall be given as input by the user. Every block, including the food and body blocks of the snake, shall have a size such that there are enough blocks to play the game correctly.
     */
    @Test
    public void testBoardInitialization6() {
        Board board = new Board(20);
        assertEquals(20, board.size);
        assertTrue(board.size> board.blockSize);
    }

    /**
     * Requirement 7: When the game starts, the board is initialised with a snake and a piece of food. The snake is represented as a single green block, positioned at the bottom left square of the board.
     * The initial direction shall be up. The piece of food is represented as a single red block placed at a random position on the board.
     * Smelly: When the game starts, the board is initialised with a snake and a piece of food. It shall be represented as a single green block, positioned at the bottom left square of the board. The initial direction shall be up. The other is represented as a single red block placed at a random position on the board.

     */
    @Test
    public void testGameStartsWithSnakeAndFood7() {
        Game game = new Game(20, "easy");

        //Check first snake and food exist
        assertNotNull(game.snake);
        assertNotNull(game.food);

        //Check they correctly initialized to the related colors.
        assertTrue(game.snake.color.equalsIgnoreCase("green"));
        assertTrue(game.food.color.equalsIgnoreCase("red"));


        //Check direction of the snake
        assertTrue(game.snake.direction.equalsIgnoreCase("up"));


        //check snake position?


        //Check positions.
        Coordinates initialFoodCoordinates = game.food.position;

        game.food.position = new Coordinates(0, 1);
        game.snake.body.add(0, new Coordinates(0, 1));
        game.updateGame();

        Coordinates currentFoodCoordinates = game.food.position;

        assertTrue(initialFoodCoordinates.x != currentFoodCoordinates.x || initialFoodCoordinates.y != currentFoodCoordinates.y);
    }

    /**
     * Requirement 8: When the user presses up, down, right, or left on the keyboard, the snake continues to move to the corresponding direction.
     *
     * ?? Need to check all combinations like opposites?
     *
     * Smelly: The snake could be moved by pressing up, down, right, or left on the keyboard.
     */
    @Test
    public void testSnakeMovement8() {
        Game game = new Game(20, "easy");
        game.handleKeyPress('d');
        game.snake.move();
        assertTrue(game.snake.direction.equalsIgnoreCase("right"));
    }

    /**
     * Requirement 9: Every time the snake eats a piece of food, a new piece of food shall be placed at a random position on the board.
     * Every time the snake eats a piece of food, a new one shall appear.
     *
     */
    @Test
    public void testFoodRegenerates9() {
        Game game = new Game(20, "easy");
        //Check positions.
        //Eats food.
        Coordinates initialFoodCoordinates = new Coordinates(6, 5);
        game.food.position = initialFoodCoordinates;
        game.snake.body.get(0).x=5;
        game.snake.body.get(0).y=5;
        game.snake.direction="RIGHT";
        game.updateGame();

        Coordinates currentFoodCoordinates = game.food.position;

        assertTrue(initialFoodCoordinates.x != currentFoodCoordinates.x || initialFoodCoordinates.y != currentFoodCoordinates.y);
    }

    /**
     * Requirement 10: Every time the snake eats a piece of food, its speed increases by 1%.
     * Hem increase spped hem update game de olabilir ya da sadece update increase kaldır?
     * Every time the snake eats a piece of food, its speed increases by 10 milliseconds.
     */
    @Test
    public void testSpeedIncreases10() {
        Game game = new Game(20, "easy");
        double oldSpeed = game.snake.speed;
        System.out.println(game.snake.speed);
        System.out.println(game.snake.body.size());

        //Eats food.
        game.food.position = new Coordinates(6, 5);
        game.snake.body.get(0).x=5;
        game.snake.body.get(0).y=5;
        game.snake.direction="RIGHT";

        game.updateGame();

        System.out.println(game.snake.speed);
        System.out.println(game.snake.body.size());


        assertTrue(game.snake.speed > oldSpeed);
    }

    /**
     * Requirement 11:The user can set the difficulty to easy or hard. If the user selects “easy”, the snake initially moves with a speed of 0.5 blocks per second. If the user selects “hard”, the snake initially moves with a speed of 2 blocks per second.
     * Smelly: The user can set the difficulty to easy or hard. If the user selects “easy”, the snake initially moves with a speed of 0.5 blocks per second.
    ???
     */
    @Test
    public void testDifficultySetting11() {
        Game gameEasy = new Game(20, "easy");
        Game gameHard = new Game(20, "hard");
        assertEquals(0.5, gameEasy.snake.speed, 0.1);
        assertTrue(gameHard.snake.speed> gameEasy.snake.speed);
    }

    /**
     * Requirement 12: After the snake has eaten 10 pieces of food, a blue bonus piece of food should appear. When the snake eats that piece, three blocks shall be attached to the snake's body.
     * Smelly :After the snake has eaten many pieces of food, a blue bonus piece of food should appear. When the snake eats that piece, three blocks shall be attached to the snake's body.
     */
    @Test
    public void testBlueBonusFood12() {
        Game game = new Game(20, "easy");

        // Setting snake body to 10 blocks manually
        for (int i = 0; i < 10; i++) {
            game.snake.body.add(new Coordinates(i+4, 1));
        }

        game.snake.growing += 10;
        // The snake body should now have 11 blocks including initial head
        assertEquals(11, game.snake.body.size());

        // Blue bonus food should appear
        game.updateGame();

        //System.out.println(game.bonusFood.color);
        assertTrue(game.bonusFood.color.equalsIgnoreCase("blue"));

        // Simulating the snake eating the blue bonus food
        game.snake.body.add(0, game.bonusFood.position);


        // Snake should now have 14 blocks (11 + 3 from blue bonus food)
        assertEquals(14, game.snake.body.size());
    }

    /**
     * Requirement 13: Every 5 minutes, a golden bonus block shall appear additionally to the food block. If the snake eats the bonus block, the speed shall decrease by 5%.
        Smelly: Sometimes, a golden bonus block shall appear additionally to the food block. If the snake eats the bonus block, the speed shall decrease by 5%.
     */
    @Test
    public void testGoldenBonusFood13() {
        Game game = new Game(20, "easy");

        double oldSpeed = 5;
        game.snake.speed = 5;
        game.time = 300;
        System.out.println(oldSpeed);
        game.updateGame();

        // golden bonus should appear.
        assertTrue(game.bonusFood.color.equalsIgnoreCase("golden") || game.bonusFood.color.equalsIgnoreCase("gold"));
        System.out.println(game.snake.speed);

        game.bonusFood.position = new Coordinates(0, 1);
        game.snake.body.add(0, new Coordinates(0, 1));
        game.updateGame();

        assertTrue(game.snake.speed < oldSpeed);
    }

    /**
     * Requirement 14: If the user presses p on the keyboard, the game shall pause until the user presses p again.
        Smelly: If the user presses p on the keyboard, the game shall pause until the user presses q again.
     */
    @Test
    public void testGamePause14() {
        Game game = new Game(20, "easy");
        game.handleKeyPress('p');
        assertTrue(game.paused);
        game.handleKeyPress('p');
        assertFalse(game.paused);
    }
}
