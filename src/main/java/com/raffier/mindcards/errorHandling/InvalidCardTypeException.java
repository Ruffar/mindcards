package com.raffier.mindcards.errorHandling;

import com.raffier.mindcards.util.CardType;

public class InvalidCardTypeException extends RuntimeException {

    public InvalidCardTypeException(CardType cardType) {
        super(cardType.getName()+" is not a valid card type.");
    }

}
