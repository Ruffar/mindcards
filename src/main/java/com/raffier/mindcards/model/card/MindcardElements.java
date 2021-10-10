package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;

import java.io.Serializable;
import java.util.List;

public class MindcardElements implements Serializable {

    private CardElement<Mindcard> mindcard;
    private List<CardElement<Infocard>> infocards;

    public MindcardElements(CardElement<Mindcard> mindcard, List<CardElement<Infocard>> infocards) {
        this.mindcard = mindcard;
        this.infocards = infocards;
    }


    public CardElement<Mindcard> getMindcard() { return mindcard; }
    public List<CardElement<Infocard>> getInfocards() {
        return infocards;
    }

    public void setMindcard(CardElement<Mindcard> mindcard) {
        this.mindcard = mindcard;
    }
    public void setInfocards(List<CardElement<Infocard>> infocards) {
        this.infocards = infocards;
    }
    public void addInfocard(CardElement<Infocard> infocard) {
        this.infocards.add(infocard);
    }

}
