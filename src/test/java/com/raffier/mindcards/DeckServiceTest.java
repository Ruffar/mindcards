package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.table.FavouriteRepository;
import com.raffier.mindcards.service.DeckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class DeckServiceTest {

	@Autowired
	private DeckService deckService;
	@Autowired
	private FavouriteRepository favouriteRepository;

	@Test
	public void TestU123() throws SQLException {
		deckService.addFavourite(1, 2);
		assertThat(favouriteRepository.getById(new Favourite(1, 2))).isNotNull();
	}
	@Test
	public void TestU124() {
		assertThatThrownBy(()->deckService.addFavourite(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU125() throws SQLException {
		deckService.removeFavourite(6, 3);
		assertThatThrownBy(()->favouriteRepository.getById(new Favourite(6, 3))).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU126() {
		assertThatThrownBy(()->deckService.removeFavourite(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU127() throws SQLException {
		assertThat(deckService.hasUserFavourited(1, 4)).isTrue();
	}
	@Test
	public void TestU128() throws SQLException {
		assertThat(deckService.hasUserFavourited(1, 3)).isFalse();
	}
	@Test
	public void TestU129() {
		assertThatThrownBy(()->deckService.hasUserFavourited(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU130() {
		assertThatCode(()->deckService.updateLastViewed(1, 1)).doesNotThrowAnyException();
	}
	@Test
	public void TestU131() {
		assertThatThrownBy(()->deckService.updateLastViewed(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU132() throws SQLException {
		assertThat(deckService.getTotalFavourites(5)).isEqualTo(2);
	}
	@Test
	public void TestU133() {
		assertThatThrownBy(()->deckService.getTotalFavourites(100)).isInstanceOf(EntityNotFoundException.class);
	}
}
