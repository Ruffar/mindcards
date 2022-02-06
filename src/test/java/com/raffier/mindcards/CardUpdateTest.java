package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidHyperlinkException;
import com.raffier.mindcards.model.web.ImageUpdate;
import com.raffier.mindcards.model.web.ImageUrlUpdate;
import com.raffier.mindcards.repository.table.CardGroupRepository;
import com.raffier.mindcards.repository.table.DeckRepository;
import com.raffier.mindcards.repository.table.InfocardRepository;
import com.raffier.mindcards.repository.table.MindcardRepository;
import com.raffier.mindcards.service.CardUpdateService;
import com.raffier.mindcards.util.ImageChangeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class CardUpdateTest {

	@Autowired
	private CardUpdateService cardUpdateService;

	@Autowired
	private DeckRepository deckRepository;
	@Autowired
	private CardGroupRepository cardGroupRepository;
	@Autowired
	private MindcardRepository mindcardRepository;
	@Autowired
	private InfocardRepository infocardRepository;

	@Test
	public void TestU94() throws SQLException {
		cardUpdateService.updateDeck(1, "This is a title", new ImageUpdate(ImageChangeType.REMOVE), "This is a description");
		assertThat(deckRepository.getById(1).getImageId()).isEqualTo(0);
	}
	@Test
	public void TestU95() throws SQLException {
		cardUpdateService.updateCardGroup(1, "New Title!", new ImageUpdate(ImageChangeType.NONE), "New Description!");
		assertThat(cardGroupRepository.getById(1).getImageId()).isEqualTo(7);
	}
	@Test
	public void TestU96() throws SQLException {
		cardUpdateService.updateMindcard(1, "Hello there", new ImageUrlUpdate("someimage.com"), "Hello world");
		assertThat(mindcardRepository.getById(1).getTitle().matches("Hello there")).isTrue();
	}
	@Test
	public void TestU97() throws SQLException {
		cardUpdateService.updateInfocard(1, new ImageUpdate(ImageChangeType.NONE), "Bye bye");
		assertThat(infocardRepository.getById(1).getDescription().matches("Bye bye")).isTrue();
	}
	@Test
	public void TestU98() throws SQLException {
		assertThat(cardUpdateService.addDeck(2).getDeckId()).isEqualTo(16);
	}
	@Test
	public void TestU99() {
		assertThatThrownBy(()->cardUpdateService.addDeck(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU100() throws SQLException {
		assertThat(cardUpdateService.addCardGroup(2).getCardGroupId()).isEqualTo(5);
	}
	@Test
	public void TestU101() {
		assertThatThrownBy(()->cardUpdateService.addCardGroup(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU102() throws SQLException {
		assertThat(cardUpdateService.addMindcard(2).getMindcardId()).isEqualTo(10);
	}
	@Test
	public void TestU103() {
		assertThatThrownBy(()->cardUpdateService.addMindcard(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU104() throws SQLException {
		assertThat(cardUpdateService.addInfocard(2).getInfocardId()).isEqualTo(9);
	}
	@Test
	public void TestU105() {
		assertThatThrownBy(()->cardUpdateService.addInfocard(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU106() throws SQLException {
		cardUpdateService.deleteDeck(3);
		assertThatThrownBy(()->deckRepository.getById(3)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU107() {
		assertThatThrownBy(()->cardUpdateService.deleteDeck(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU108() throws SQLException {
		cardUpdateService.deleteCardGroup(3);
		assertThatThrownBy(()->cardGroupRepository.getById(3)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU109() {
		assertThatThrownBy(()->cardUpdateService.deleteCardGroup(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU110() throws SQLException {
		cardUpdateService.deleteMindcard(3);
		assertThatThrownBy(()->mindcardRepository.getById(3)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU111() {
		assertThatThrownBy(()->cardUpdateService.deleteMindcard(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU112() throws SQLException {
		cardUpdateService.deleteInfocard(3);
		assertThatThrownBy(()->infocardRepository.getById(3)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU113() {
		assertThatThrownBy(()->cardUpdateService.deleteInfocard(100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU114() {
		assertThat(cardUpdateService.areHyperlinksValid("[hello world](mindcard/1)")).isTrue();
	}
	@Test
	public void TestU115() {
		assertThatThrownBy(()->cardUpdateService.areHyperlinksValid("[hello world](mind)")).isInstanceOf(InvalidHyperlinkException.class);
	}
	@Test
	public void TestU116() {
		assertThatThrownBy(()->cardUpdateService.areHyperlinksValid("[hello world](deck/)")).isInstanceOf(InvalidHyperlinkException.class);
	}
}
