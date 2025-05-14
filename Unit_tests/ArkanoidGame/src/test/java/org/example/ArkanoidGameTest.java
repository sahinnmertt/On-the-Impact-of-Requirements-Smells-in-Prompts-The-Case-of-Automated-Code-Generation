package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class ArkanoidGameTest {
    private Game game;
    private Ball ball;
    private Paddle paddle;
    private Brick brick;
    private Board board;

    @Before
    public void setUp() {
        game = new Game();
    }


    // Req 1: The goal of the game is to complete 33 levels successfully.
    @Test
    public void testGameHas33Levels1() { // Req 1
        assertEquals(33, game.maxLevel);
    }

    //Req 2: The board is 200 pixels wide horizontally and 300 pixels wide vertically.
    @Test
    public void testBoardSize2() { // Req 2
        assertEquals(200, game.board.width);
        assertEquals(300, game.board.height);
    }

    // Req 3: The bricks and the paddle are rectangular blocks with a width of 20 pixels and height of 10 pixels. The ball has a diameter of 10 pixels.
    @Test
    public void testBricksAndPaddleSize3() { // Req 3

        assertEquals(20, game.bricks.get(0).width);
        assertEquals(10, game.bricks.get(0).height);

        assertEquals(10, game.ball.diameter);

        assertEquals(20, game.bricks.get(0).width);
        assertEquals(10, game.bricks.get(0).height);
    }

    // Req 4.At the beginning of each level, the ball starts from the top left corner of the board and moves downwards with a straight trajectory at a 45° angle and a speed of 5 pixels per second.
    @Test
    public void testBallInitialPositionAndSpeed4() { // Req 4
        //maybe give some range due to ball diameter?
        assertEquals(0, game.ball.x);
        assertEquals(0, game.ball.y);
        assertEquals(5, game.ball.xSpeed);
        assertEquals(5, game.ball.ySpeed);
    }

    /*
        Req 5:At each level, the user moves a paddle in order to hit a ball, making the ball change its direction upward.
        Smelly: At each level, the user moves a paddle in order to hit a ball, making it change its direction upward.
     */
    @Test
    public void testPaddleMovementWithinBounds5() { // Req 5
        //paddle.moveLeft();
        System.out.println(game.ball.ySpeed);
        game.ball.x = 0;
        game.ball.y =0;

        game.paddle.x= 0;
        game.paddle.x =0;
        game.checkCollisions();

        System.out.println("after"+ game.ball.ySpeed);
        //paddle.moveRight();

    }

    /*
        Req 6: To successfully complete a level, the user needs to break all bricks on the board by hitting them with the ball.
        Smelly: To successfully complete a level, the user needs to break all bricks.

      */
    @Test
    public void testBreakAllBricksToCompleteLevel6() { // Req 6
        game = new Game();
       System.out.println(game.level);
        game.bricks.clear();

        game.updateGame();
        System.out.println(game.level);
        Assert.assertEquals(2,game.level);
    }

    /*    Req 7: Each brick must be either blue, red, or gold and is sized single or double.
        Smelly: Bricks can have a color (blue, red, gold) and can be sized single or double.

        */
    // Ask this part ---> is sized single or double. how to test that?
    @Test
    public void testBrickColors7() { // Req 7
        List<String> validColors = Arrays.asList("BLUE", "RED", "GOLD");

        for (Brick brick : game.bricks) {
            assertTrue("Invalid brick color: " + brick.color, validColors.contains(brick.color.toUpperCase()));
        }

    }
    //Req 8: A brick is sized single (20 x 10 pixels) or double (40 x 10 pixels).
    // Smelly : -- - -- A brick is sized single (20 x 10 pixels) or double (60 x 10 pixels).
    @Test
    public void testBrickSizes8() { // Req 8
        for (Brick brick : game.bricks) {
            assertTrue("Invalid brick size: " + brick.width + "x" + brick.height,
                    (brick.width == 20 && brick.height == 10) || (brick.width == 40 && brick.height == 10));
        }
    }
/*
    //Req 9: Blue bricks are destroyed with one hit, red bricks require two hits, gold bricks require three hits.
        Smelly: Blue bricks are destroyed with one hit, red bricks require two hits, yellow bricks require three hits.*/
    @Test
    public void testBricksRequireCorrectHitsToBreak9() { // Req 9
        for (Brick brick : game.bricks) {
            int requiredHits = brick.color.equalsIgnoreCase("Blue") ? 1 :
                    brick.color.equalsIgnoreCase("Red") ? 2 :
                            brick.color.equalsIgnoreCase("Gold") ? 3 : -1;

            assertTrue("Invalid brick color for hit test: " + brick.color, requiredHits != -1);

            for (int i = 0; i < requiredHits - 1; i++) {
                brick.hit();
                assertFalse("Brick should not be destroyed yet: " + brick.color, brick.destroyed);
            }

            brick.hit();
            assertTrue("Brick should be destroyed: " + brick.color, brick.destroyed);
        }
    }

    /*
        Req 10: The first level gets initialised with 50 blue, single bricks randomly placed on the upper third of the board.
        ??Smelly: The first level gets initialised with 50 blue, single bricks that are placed in the brick section of the board. ambiguities	semantic

     */
    @Test
    public void testInitialBrickPlacement10() { // Req 10
        game.generateBricks();
        int count = 0;
        int boardWidth = game.board.width;
        int boardHeight = game.board.height;
        int upperThirdLimit = boardHeight / 3;

        for (Brick brick : game.bricks) {
            if (brick.color.equalsIgnoreCase("Blue") && brick.width == 20 && brick.height == 10) {
                count++;
                assertTrue("Brick placed outside upper third: " + brick.y, brick.y < upperThirdLimit);
                assertTrue("Brick placed outside board width: " + brick.x, brick.x >= 0 && brick.x + brick.width <= boardWidth);
            }
        }

        assertEquals("Incorrect number of blue bricks", 50, count);
    }

    /*
    Req 11: The paddle only moves horizontally on the bottom of the board, controlled by the user by left and right arrow keys. The paddle always stays inside the board. When each level starts the paddle resets to the center of the bottom border.

    Smelly: The paddle only moves horizontally on the bottom of the board, controlled by the user by left and right arrow keys. The paddle cannot go outside the board. When each level starts the paddle resets to the center of the bottom border.

    */
    @Test
    public void testPaddleBehavior11() { // Req 11
        int boardWidth = game.board.width;
        int boardHeight = game.board.height;
        int paddleWidth = game.paddle.width;

        // Test paddle resets to the center
        game.nextLevel();
        int expectedCenterX = (boardWidth - paddleWidth) / 2;
        assertTrue("Paddle should be positioned at the center range",
                game.paddle.x >= expectedCenterX - 2 && game.paddle.x <= expectedCenterX + 2);
        System.out.println(game.paddle.y);
        assertTrue("Paddle should be at bottom", (0 <= game.paddle.y && game.paddle.y <= game.paddle.height) || (game.board.height >= game.paddle.y && game.paddle.y >= game.board.height - game.paddle.height))  ;

        // Test paddle moves left to 0 position
        game.paddle.x = 0;
        game.paddle.moveLeft();
        assertEquals("Paddle should not move left beyond 0", 0, game.paddle.x);

        // Test paddle moves only inside board boundaries
        game.paddle.x =boardWidth;
        game.paddle.moveRight(boardWidth);
        assertTrue("Paddle should stay inside the board", game.paddle.x == boardWidth);
    }

    /*

        Req 12.The ball direction changes every time the ball hits the paddle or the border of the board (except for the bottom border) with the same angle of reflection as the angle of incidence.
        Smelly: The ball direction changes every time the ball hits the paddle or the border of the board (except for the bottom border) with the correct angle of reflection.

    */
    @Test
    public void testBallBouncesCorrectly12() { // Req 12
        int initialXSpeed = game.ball.xSpeed;
        int initialYSpeed = game.ball.ySpeed;

        // Simulate hitting the left or right wall
        game.ball.x = 0; // Ball at left wall
        game.checkCollisions();
        assertEquals("Ball should reflect off the left wall", -initialXSpeed, game.ball.xSpeed);

        game.ball.x = 200; // Ball at right wall
        game.checkCollisions();
        assertEquals("Ball should reflect off the right wall", initialXSpeed, game.ball.xSpeed);

        // Simulate hitting the top wall
        game.ball.y = 0; // Ball at the top
        game.checkCollisions();
        assertEquals("Ball should reflect off the top wall", -initialYSpeed, game.ball.ySpeed);

        // Simulate hitting the paddle
        game.ball.y = game.paddle.y - 1;
        game.checkCollisions();
        assertEquals("Ball should reflect off the paddle", -initialYSpeed, game.ball.ySpeed);
    }

    /*

        Req 13: Every time the next level starts, the speed of the ball increases by 1 pixel per second.
        Smelly: Every time the next level starts, the speed of the ball increases by a sufficient amount.

    */
    @Test
    public void testBallSpeedIncreasesEachLevel13() { // Req 13
        int initialSpeed = game.ball.xSpeed;
        game.nextLevel();
        assertTrue(game.ball.xSpeed > initialSpeed);
    }

    //Req 14:  When the user finishes a level, the next level with a different brick arrangement and increased difficulty begins. Each level adds two red bricks and one gold brick.
  // ??? Smelly: When the user finishes a level, the next level with a different brick arrangement and increased difficulty begins. Each level removes two red bricks and one gold brick.
    @Test
    public void testBricksIncreaseEachLevel14() { // Req 14

        game.bricks.clear();
        game.nextLevel();
        int redCount = 0, goldCount = 0;
        for (Brick b : game.bricks) {

                if (b.color.equalsIgnoreCase("Red")) redCount++;
                if (b.color.equalsIgnoreCase("Gold")) goldCount++;

        }
        assertEquals(2, redCount);
        assertEquals(1, goldCount);
    }

   /*  Req 15: The program shall generate the brick pattern and arrangement randomly.
    Smelly: The brick pattern and arrangement is randomly generated.	*/
   @Test
   public void testBrickPatternGeneratedRandomly15() { // Req 15
       game.generateBricks();
       List<Brick> firstPattern = new ArrayList<>(game.bricks);
       game.generateBricks();
       List<Brick> secondPattern = new ArrayList<>(game.bricks);

       assertNotEquals("Brick patterns should be different on each generation", firstPattern, secondPattern);
   }

 /*  Req 16: The user has 3 lives.
    Smelly: 3 lives are given to a user*/
    @Test
    public void testPlayerStartsWithThreeLives16() { // Req 16
        assertEquals(3, game.lives);
    }

/*    Req 17: The user loses a life every time the ball falls off the bottom of the board.
        Smelly: The user loses a life every time the ball falls of the side of the board.*/
    @Test
    public void testLifeLostWhenBallFalls17() { // Req 17
        int initialLives = game.lives;
        game.loseLife();
        assertEquals(initialLives - 1, game.lives);
    }
/*
        Req 18: When the user loses a life but still has lives remaining, the level restarts. Once the user loses all lives, the game is over.
        Smelly : When the user loses a life but still has lives remaining, the level should restart if possible. Once the user loses all lives, the game is over.*/
    @Test
    public void testLevelRestartsWhenLifeIsLost18() { // Req 18
        game.loseLife();
        game.updateGame();
        assertEquals(2, game.lives);
        assertEquals(false, game.gameOver);
    }

/*
    Req 19: When a game is over, the user shall decide if they want to play again. If they want to play again, the new game starts from level 1. Otherwise, the game ends.
    Smelly: When a game is over, it can be started again from level 1 or ended.
*/

    @Test
    public void testGameResetsWhenPlayerChoosesToRestart19() { // Req 19

        game.level = 2;
        game.lives = 0;
        game.updateGame();
        assertEquals(0, game.lives);
        game.resetGame();
        assertEquals(1, game.level);
        assertEquals(3, game.lives);
    }
}
