package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.CardGroup;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.model.web.CardElement;
import com.raffier.mindcards.model.web.DeckElement;
import com.raffier.mindcards.repository.table.UserRepository;
import com.raffier.mindcards.service.CardElementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class CardElementTest {

	@Autowired
	private CardElementService cardElementService;
	@Autowired
	private UserRepository userRepository;


	@Test
	public void TestU77() throws SQLException {
		CardElement<Infocard> value = cardElementService.getInfocardElement(1);
		assertThat(value.getPrimaryKey()).isEqualTo(1);
		assertThat(value.getImage().getImageId()).isEqualTo(6);
	}

	@Test
	public void TestU78() {
		assertThatThrownBy(()->cardElementService.getInfocardElement(100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU79() throws SQLException {
		CardElement<Mindcard> value = cardElementService.getMindcardElement(1);
		assertThat(value.getPrimaryKey()).isEqualTo(1);
		assertThat(value.getImage().getImageId()).isEqualTo(4);
	}

	@Test
	public void TestU80() {
		assertThatThrownBy(()->cardElementService.getMindcardElement(100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU81() throws SQLException {
		CardElement<CardGroup> value = cardElementService.getCardGroupElement(1);
		assertThat(value.getPrimaryKey()).isEqualTo(1);
		assertThat(value.getImage().getImageId()).isEqualTo(7);
	}

	@Test
	public void TestU82() {
		assertThatThrownBy(()->cardElementService.getCardGroupElement(100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU83() throws SQLException {
		DeckElement value = cardElementService.getDeckElement(userRepository.getById(2), 1);
		assertThat(value.getPrimaryKey()).isEqualTo(1);
		assertThat(value.getImage().getImageId()).isEqualTo(1);
		assertThat(value.getTotalFavourites()).isEqualTo(3);
	}

	@Test
	public void TestU84() {
		assertThatThrownBy(()->cardElementService.getDeckElement(userRepository.getById(2), 100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU85() throws SQLException {
		assertThat(cardElementService.getInfocardDeck(userRepository.getById(2), 3).getPrimaryKey()).isEqualTo(6);
	}

	@Test
	public void TestU86() throws SQLException {
		assertThat(cardElementService.getMindcardDeck(userRepository.getById(2), 1).getPrimaryKey()).isEqualTo(1);
	}

	@Test
	public void TestU87() throws SQLException {
		assertThat(cardElementService.getCardGroupDeck(userRepository.getById(2), 2).getPrimaryKey()).isEqualTo(2);
	}

	@Test
	public void TestU88() throws SQLException {
		assertThat(cardElementService.getDecksFromUser(userRepository.getById(2),userRepository.getById(1)).get(0).getPrimaryKey()).isEqualTo(11);
	}

	@Test
	public void TestU89() throws SQLException {
		assertThat(cardElementService.getDecksFromUser(userRepository.getById(2),new User(100)).size()).isEqualTo(0);
	}

	@Test
	public void TestU90() throws SQLException {
		assertThat(cardElementService.getInfocardsFromMindcard(1).size()).isEqualTo(2);
	}

	@Test
	public void TestU91() throws SQLException {
		assertThat(cardElementService.getMindcardsFromCardGroup(1).size()).isEqualTo(3);
	}

	@Test
	public void TestU92() throws SQLException {
		assertThat(cardElementService.getRandomMindcardsFromDeck(1,10).size()).isEqualTo(3);
	}

	@Test
	public void TestU93() throws SQLException {
		assertThat(cardElementService.getRandomCardGroupsFromDeck(1,10).size()).isEqualTo(1);
	}

}
