package org.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScopaGameTest {

    @Mock
    Player mockPlayer;

    @Mock
    Game mockGame;

    @InjectMocks
    Card testCard;

    private ByteArrayOutputStream outContent;


    @BeforeEach
    public void setUp() {

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Requirement 1: A game for three players.
     */
    @Test
    public void testInitializeGamePlayers1() {
        Game game = new Game();
        game.initializeGame();
        //assertEquals("Alice", game.players.get(0).name);
        //assertEquals("Bob", game.players.get(1).name);
        //assertEquals("Charlie", game.players.get(2).name);
        assertEquals(3,game.players.size());
    }

    /**
     * Requirement 2: The program shall decide the players’ turn order randomly at the beginning of the game.
     */
    @Test
    public void testDeckShuffle2() {
        Game game = new Game();
        game.initializeGame();

        Game game2 = new Game();
        game2.initializeGame();

        Game game3 = new Game();
        game3.initializeGame();
        System.out.println(game.turnOrder);
        System.out.println(game2.turnOrder);
        System.out.println(game3.turnOrder);

        assertTrue((game.turnOrder != game2.turnOrder) || (game.turnOrder != game3.turnOrder) || (game2.turnOrder != game3.turnOrder)  );
    }

    /**
     * Requirement 3. Every time a player takes all the cards from the table during his turn, he earns a special point.
     */
    @Test
    public void testClearTableCapture3() {

        // First create a mocked deck to set players and deck table
        List<Card> mockedDeck = new ArrayList<>();
        mockedDeck.add(new Card("Coins",1));


        Game game = new Game();
        //game.initializeGame();
        Player player = new Player("Alice");

        game.tableCards = new ArrayList<>(mockedDeck);
        player.hand =  new ArrayList<>(mockedDeck);

        System.out.println("Before playTurn, tableCards: " + game.tableCards);

        game.playTurn(player);
        assertEquals(1, player.specialPoints);
    }

    /**
     * Requirement 4. The game is played with a deck of 40 cards, divided into four suits (Coins, Cups, Swords, and Clubs).
     * Smell :4. The game is played with a deck of 40 cards, divided into four suits (Coins, Cups, Swords, Hearts, and Clubs).
     */
    @Test
    public void testTurnOrderRandomized4() {
        Game game = new Game();
        game.initializeGame();  // Initialize game, shuffle deck, deal cards

        // Create a single list to collect all cards
        List<Card> allCards = new ArrayList<>();

        // Step 1: Add remaining deck cards
        allCards.addAll(game.deck);

        // Step 2: Add players' hands
        for (Player player : game.players) {
            allCards.addAll(player.hand);
        }

        // Step 3: Add table cards
        allCards.addAll(game.tableCards);

        // Step 4: Ensure total cards add up to 40
        assertEquals(40, allCards.size(), "All 40 unique cards should be present");

        // Step 5: Count unique suits
        Set<String> uniqueSuits = allCards.stream()
                .map(Card::getSuit)
                .collect(Collectors.toSet());

        // Ensure exactly 4 suits exist
        assertEquals(4, uniqueSuits.size(), "Deck should have exactly 4 different suits");

        // Step 6: Count cards per suit
        Map<String, Long> suitCounts = allCards.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        // Ensure each suit has exactly 10 cards
        for (String suit : uniqueSuits) {
            assertEquals(10, suitCounts.getOrDefault(suit, 0L), "Each suit should have exactly 10 cards in total");
        }
    }

    /**
     * Requirement 5. Every suit has 10 cards, from 1 to 10.
     */
    @Test
    public void testInitialHandSize5() {
        Game game = new Game();
        game.initializeGame();  // Initialize game, shuffle deck, deal cards

        // Create a single list to collect all cards
        List<Card> allCards = new ArrayList<>();

        // Step 1: Add remaining deck cards
        allCards.addAll(game.deck);

        // Step 2: Add players' hands
        for (Player player : game.players) {
            allCards.addAll(player.hand);
        }

        // Step 3: Add table cards
        allCards.addAll(game.tableCards);


        // Step 4: Count unique suits
        Set<String> uniqueSuits = allCards.stream()
                .map(Card::getSuit)
                .collect(Collectors.toSet());

        // Ensure exactly 4 suits exist
        assertEquals(4, uniqueSuits.size(), "Deck should have exactly 4 different suits");

        // Step 5: Count cards per suit
        Map<String, Long> suitCounts = allCards.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        // Ensure each suit has exactly 10 cards
        for (String suit : uniqueSuits) {
            assertEquals(10, suitCounts.getOrDefault(suit, 0L), "Each suit should have exactly 10 cards in total");
        }
    }

    /**
     * Requirement 6. The primary objective of Scopa is to reach a score of 8. The player who first reaches the limit wins the game.
     * Smelly version 6. The primary objective of Scopa is to reach a certain goal. The player who first reaches the limit wins the game.
     */
    @Test
    public void testDeclareWinner6() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Game game = new Game();
        Player p1 = new Player("Alice");
        p1.score = 5;
        //set extreme point
        Player p2 = new Player("Bob");
        p2.score = 2200;
        Player p3 = new Player("Charlie");
        p3.score = 1;
        game.players = (Arrays.asList(p1, p2, p3));
        game.declareWinner();
        Assert.assertTrue(outContent.toString().contains("Bob"));
    }

    /**
     * Requirement 7. Players shall be able to play multiple rounds to reach the winning score.
        Smelly version : 7. It could be fun to be able to play multiple rounds until reaching the winning score.
     */
    @Test
    public void testPlayTurnOrder7() {

        Game game = new Game();
        game.initializeGame();
        Player player = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");

       game.playTurn(player);
       game.playTurn(player2);
        game.playTurn(player3);

       System.out.println("Round count " + game.roundCount);

       Assert.assertNotEquals(0,game.roundCount);
    }

    /**
     * Requirement 8. First, the program shuffles the deck. Then, each player receives three cards. Finally, four cards are placed on the table.
     * Smelly version 8. The card deck gets shuffled. Then, each player receives some cards. Finally, four cards are placed on the table.
     */
    @Test
    public void testInitialTableCards8() {
        Game game = new Game();
        Game game2 = new Game();

        game.initializeGame();
        game2.initializeGame();

        System.out.println(game.tableCards.get(0).value + game.tableCards.get(0).getSuit() );
        System.out.println(game2.tableCards.get(0).value + game2.tableCards.get(0).getSuit() );

        boolean isShuffled = !game.tableCards.equals(game2.tableCards);
        assertTrue(isShuffled, "Table cards should be different in two separate games due to shuffling");

        assertEquals(4, game.tableCards.size());
        assertTrue( game.players.get(0).hand.size() > 0);
        assertTrue( game.players.get(1).hand.size() > 0);
        assertTrue( game.players.get(2).hand.size() > 0);

    }

    /**
     * Requirement 9. In each turn of the game, the player has to select a card from his hand.
     * Smelly 9. In each turn of the game, a card must be selected from the player's hand.
     */
    @Test
    public void testPlayCard9() {
        Player player = new Player("Bob");
        Card card = new Card("Coins", 7);
        player.hand.add(card);
        assertEquals(card, player.playCard(0));
    }

    /**
     * Requirement 10. If the value of the selected card during a player's turn matches the value of one card on the table, the player puts the matching card on his discard pile.
     * 10. If the value of the selected card during a player's turn matches the value of one card on the table, the player puts it on his discard pile.
     */
    @Test
    public void testAddToDiscardPile10() {
        Game game = new Game();


        List<Card> mockedTableCards = new ArrayList<>(Arrays.asList(new Card("Swords", 5), new Card("Cups", 6)));
        List<Card> mockedPlayersHandCards = new ArrayList<>(Arrays.asList(new Card("Swords", 5)));

        game.initializeGame();
        game.players.get(0).hand = mockedPlayersHandCards;
        game.tableCards = mockedTableCards;

        game.playTurn(game.players.get(0));


      //  System.out.println("size" + game.players.get(0).discardPile.size());
    //    System.out.println("size" + game.players.get(0).discardPile.get(0).getSuit());
  //      System.out.println("size" + game.players.get(0).discardPile.get(0).getValue());
//        System.out.println("size" + game.players.get(0).discardPile.get(1).getSuit());
        //System.out.println("size" + game.players.get(0).discardPile.get(1).getValue());

     //   System.out.println(game.players.get(0).discardPile.toString());
        boolean cardExists = game.players.get(0).discardPile.stream()
                .anyMatch(card -> card.getSuit().equals("Swords") && card.getValue() == 5);

        assertTrue(cardExists, "The discard pile should contain the card with suit 'Swords' and value 5");
    }

    /**
     * Requirement 11. If the value of the selected card during a player's turn matches the sum of the values of two or more cards on the table, the player puts the matching cards on his discard pile.
     * Smelly 11. If the value of the selected card during a player's turn matches the value of one card on the table, the matching cards are put on the player's discard pile.
     */
    @Test
    public void testValidCardCapture11() {
        Game game = new Game();

        // Mocking table cards that sum up to 7
        List<Card> mockedTableCards = new ArrayList<>(Arrays.asList(
                new Card("Coins", 3),
                new Card("Coins", 4),
                new Card("Swords", 6)  // This card should NOT be captured
        ));

        game.initializeGame();
        // Mocking player's hand with a card that matches the sum (3 + 4 = 7)
        List<Card> mockedPlayersHandCards = new ArrayList<>(Arrays.asList(new Card("Coins", 7)));

        game.players.get(0).hand = mockedPlayersHandCards;
        game.tableCards = mockedTableCards;

        game.playTurn(game.players.get(0));

        // Debugging output
        System.out.println("Discard Pile Size: " + game.players.get(0).discardPile.size());
        game.players.get(0).discardPile.forEach(card ->
                System.out.println("Discard: " + card.getSuit() + " " + card.getValue())
        );

        // Check that the player's discard pile contains all expected cards
        boolean cardExists = game.players.get(0).discardPile.stream()
                .anyMatch(card -> card.getSuit().equals("Coins") && card.getValue() == 7);

        boolean capturedFirstCard = game.players.get(0).discardPile.stream()
                .anyMatch(card -> card.getSuit().equals("Coins") && card.getValue() == 3);

        boolean capturedSecondCard = game.players.get(0).discardPile.stream()
                .anyMatch(card -> card.getSuit().equals("Coins") && card.getValue() == 4);

        assertTrue(capturedFirstCard, "The discard pile should contain the captured card with value 3");
        assertTrue(capturedSecondCard, "The discard pile should contain the captured card with value 4");

        // Ensure the wrong card (Swords 6) is NOT captured
        boolean wrongCardCaptured = game.players.get(0).discardPile.stream()
                .anyMatch(card -> card.getSuit().equals("Swords") && card.getValue() == 6);

        assertFalse(wrongCardCaptured, "The discard pile should NOT contain the non-matching card with value 6");
    }

    /**
     * Requirement 12. If the selected card doesn't match any card on the table, the player's card gets added to the existing cards on the table.
     * Smelly 12. If the selected card doesn't match any card on the table, the player must do something with the card anyway.
     */
    @Test
    public void testHandleCardNotMatching12() {
        Game game = new Game();

        // Mocking table cards (None should match the played card)
        List<Card> mockedTableCards = new ArrayList<>(Arrays.asList(
                new Card("Cups", 2),
                new Card("Swords", 4)
        ));

        // Mocking player's hand with a card that does NOT match any table card
        List<Card> mockedPlayersHandCards = new ArrayList<>(Arrays.asList(new Card("Coins", 7)));

        game.initializeGame();
        game.players.get(0).hand = mockedPlayersHandCards;
        game.tableCards = mockedTableCards;

        // Play the turn
        game.playTurn(game.players.get(0));

        // ✅ Check if the player's card was added to the table (since no match)
        boolean cardExistsOnTable = game.tableCards.stream()
                .anyMatch(card -> card.getSuit().equals("Coins") && card.getValue() == 7);

        assertTrue(cardExistsOnTable, "The played card should be added to the table since it does not match any existing card.");
    }


    /**
     * Requirement 13. When a player has no cards left in his hand, he receives three new cards from the deck.
     * Smelly 13. When a player has no cards left on his hand, he could potentially receive three new cards from the deck.
     */
    @Test
    public void testCalculateFinalScores13() {
        Game game = new Game();
        game.initializeGame();  // Set up deck, players, and table cards

        // Select a player and make their hand empty

        game.players.get(0).hand.clear();  // Force the player to have no cards

        // Play the turn (which should trigger dealing new cards)
        game.playTurn(game.players.get(0));

        assertTrue(game.players.get(0).hand.size()>0);

    }

    /**
     * Requirement 14. Once all the cards have been distributed to the players, the round comes to an end. If there are any cards left on the table, the player who last had drawn cards will take them on his discard pile.
     * Smelly 14 Once all the cards have been distributed to the players, the round comes to an end. If there are any cards left on the table, the player who last drew cards could take them on his discard pile.
     * */
    @Test
    public void testGameRoundProgression14() {
        Game game = new Game();
        game.initializeGame(); // Setup game

        // Simulate end of round: Empty deck, but two specific cards left on the table
        game.deck.clear();
        List<Card> leftoverTableCards = new ArrayList<>(Arrays.asList(
                new Card("Coins", 2),
                new Card("Swords", 4)
        ));
        game.tableCards = leftoverTableCards;

        // Call method that should handle the round end
        game.handleRoundEnd();

        // Check if any player has BOTH "Coins 2" and "Swords 4" in their discard pile
        boolean hasCoins2AndSwords4 = game.players.stream().anyMatch(player ->
                player.discardPile.stream().anyMatch(card -> card.getSuit().equals("Coins") && card.getValue() == 2) &&
                        player.discardPile.stream().anyMatch(card -> card.getSuit().equals("Swords") && card.getValue() == 4)
        );

        assertTrue(hasCoins2AndSwords4, "One of the players should have received 'Coins 2' and 'Swords 4' from the table.");
    }

    /**
     * Requirement 15. After the round has ended, all players tally up their points. If any player reaches the winning score, the game ends. If not, a new round begins.
     */
    @Test
    public void testGameOverCondition15() {
        Game game = new Game();
        game.initializeGame();

        game.players.get(0).score = 0;
        game.players.get(1).score = 0;
        game.players.get(2).score = 0;

        game.calculateFinalScores();


        assertFalse(game.gameOver);

        game.players.get(0).score = 0;
        game.players.get(1).score = 0;
        game.players.get(2).score = 1000;

        game.calculateFinalScores();

        assertTrue(game.gameOver);
    }

    /**
     * Requirement 16.At the end of each round, the players receive a point for each of the following rules applied to the discard piles of each player: the player with the most cards, the player with the most coins cards, the player who has the seven of Coins, the player with the highest sum of their cards.
     *
     * */
    @Test
    public void testEndOfRoundScoring16() {
        Game game = new Game();
        game.initializeGame(); // Initialize game with players
        System.out.println();
        // Simulate discard piles for each player
        Player p1 = game.players.get(0);
        Player p2 = game.players.get(1);
        Player p3 = game.players.get(2);
        System.out.println("p1 score "+ p1.score);

        // Player 1: Most total cards
        p1.discardPile.addAll(Arrays.asList(
                new Card("Coins", 1), new Card("Coins", 2), new Card("Cups", 3), new Card("Swords", 4), new Card("Clubs", 5)
        )); // 5 total cards

        // Player 2: Most "Coins" suit cards
        p2.discardPile.addAll(Arrays.asList(
                new Card("Coins", 3), new Card("Coins", 4), new Card("Coins", 5), new Card("Cups", 6)
        )); // 3 Coins cards

        // Player 3: Has "Seven of Coins" + highest total sum
        p3.discardPile.addAll(Arrays.asList(
                new Card("Coins", 7), new Card("Swords", 8), new Card("Cups", 9)
        )); // Sum = 7 + 8 + 9 = 24 (highest sum)

        // Call method that should calculate scores
        game.calculateFinalScores();

        // ✅ Check scoring rules
        assertTrue( p1.score> 0, "Player with most total cards should receive 1 point");
        assertTrue(p2.score> 0, "Player with the most 'Coins' suit cards should receive 1 point");
        assertTrue(p3.score> p2.score, "Player with 'Seven of Coins' should receive 1 point \"Player with the highest sum of cards should receive 1 point\"");

    }
}
