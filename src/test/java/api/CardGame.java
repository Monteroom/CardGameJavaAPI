package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CardGame {
    int cardValue(String card) {

        //to calculate the value of the card
        if (card.equals("ACE")) {
            return 11;
        } else if (card.equals("KING") || card.equals("QUEEN") || card.equals("JACK")) {
            return 10;
        } else {
            return Integer.parseInt(card);
        }
    }
    //to check for blackjack
    boolean hasBlackJack(String card1, String card2, String card3) {
        return cardValue(card1) + cardValue(card2) + cardValue(card3) == 21;
    }
    @Test
    public void blackjack() {

        //New Deck
        String newDeckURL = "https://deckofcardsapi.com/api/deck/new/";
        Response response = RestAssured.get(newDeckURL);
        response.prettyPrint();
        String deckID = response.jsonPath().getString("deck_id");
        System.out.println(deckID);

// shuffle card
        boolean shuffle = response.jsonPath().getBoolean("shuffled");
        Assert.assertFalse(shuffle);

        String shuffleURL = "https://deckofcardsapi.com/api/deck/"+ deckID + "/shuffle/";
        response = RestAssured.get(shuffleURL);
        response.prettyPrint();
        shuffle = response.jsonPath().getBoolean("shuffled");
        Assert.assertTrue(shuffle);

        //Draw cards

        String drawURL = "https://deckofcardsapi.com/api/deck/" + deckID + "/draw/";
        Map<String, Object> params = new HashMap<>();
        params.put("count", 3);


        //first player draw cards
        response = RestAssured.given().params(params).get(drawURL);
        response.prettyPrint();

        System.out.println("First Player Cards");
        System.out.println(response.jsonPath().getString("cards[0].value"));
        System.out.println(response.jsonPath().getString("cards[1].value"));
        System.out.println(response.jsonPath().getString("cards[2].value"));

        String firstCardPlayerOne = response.jsonPath().getString("cards[0].value");
        String secondCardPlayerOne = response.jsonPath().getString("cards[1].value");
        String thirdCardPlayerOne = response.jsonPath().getString("cards[2].value");

        //check for blackjack player one
        boolean firstPlayerBlackJack = hasBlackJack(firstCardPlayerOne, secondCardPlayerOne, thirdCardPlayerOne);
        System.out.println("First Player has BlackJack: " + firstPlayerBlackJack);

        //second player draw cards
        response = RestAssured.given().params(params).get(drawURL);

        System.out.println("Second Player Cards");
        System.out.println(response.jsonPath().getString("cards[0].value"));
        System.out.println(response.jsonPath().getString("cards[1].value"));
        System.out.println(response.jsonPath().getString("cards[2].value"));

        String firstCardPlayerTwo = response.jsonPath().getString("cards[0].value");
        String secondCardPlayerTwo = response.jsonPath().getString("cards[1].value");
        String thirdCardPlayerTwo = response.jsonPath().getString("cards[2].value");

        //check for blackjack player two
        boolean secondPlayerBlackJack = hasBlackJack(firstCardPlayerTwo, secondCardPlayerTwo, thirdCardPlayerTwo);
        System.out.println("Second Player has BlackJack: " + secondPlayerBlackJack);

        //Verify that served cards are not in card deck
        int remaining = response.jsonPath().getInt("remaining");
        Assert.assertEquals(46, remaining);


    }
}
