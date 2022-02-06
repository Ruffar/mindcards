package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class RepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private DeckRepository deckRepository;
	@Autowired
	private MindcardRepository mindcardRepository;
	@Autowired
	private CardGroupRepository cardGroupRepository;
	@Autowired
	private InfocardRepository infocardRepository;
	@Autowired
	private GroupMindcardRepository groupMindcardRepository;
	@Autowired
	private FavouriteRepository favouriteRepository;

	//User
	@Test
	public void TestU10() throws SQLException {
		assertThat( userRepository.getById(1).getUsername().matches("mainAdmin") ).isTrue();
	}
	@Test
	public void TestU11() {
		assertThatThrownBy( ()->userRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU12() throws SQLException {
		assertThat( userRepository.add(new User(0)).getUserId() ).isEqualTo(6);
	}
	@Test
	public void TestU13() throws SQLException {
		userRepository.save( new User(1,"pizza", "burger", "food@restaurant", false) );
		assertThat( userRepository.getById(1).getUsername().matches("pizza") ).isTrue();
	}
	@Test
	public void TestU14() {
		assertThatThrownBy( ()->userRepository.save( new User(100,"pizza", "burger", "food@restaurant", false) ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU15() throws SQLException {
		userRepository.deleteById(5);
		assertThatThrownBy(()->userRepository.getById(5)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU16() {
		assertThatThrownBy(()->userRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Image
	@Test
	public void TestU17() throws SQLException {
		assertThat( imageRepository.getById(1).getImagePath().matches("CardImages/1.png") ).isTrue();
	}
	@Test
	public void TestU18() {
		assertThatThrownBy( ()->imageRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU19() throws SQLException {
		assertThat( imageRepository.add(new Image(0)).getImageId() ).isEqualTo(8);
	}
	@Test
	public void TestU20() throws SQLException {
		imageRepository.save( new Image(1,"hello world") );
		assertThat( imageRepository.getById(1).getImagePath().matches("hello world") ).isTrue();
	}
	@Test
	public void TestU21() {
		assertThatThrownBy( ()->imageRepository.save( new Image(100,"hello world") ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU22() throws SQLException {
		imageRepository.deleteById(5);
		assertThatThrownBy(()->imageRepository.getById(5)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU23() {
		assertThatThrownBy(()->imageRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Deck
	@Test
	public void TestU24() throws SQLException {
		assertThat( deckRepository.getById(1).getTitle().matches("Dave's Guide to Life") ).isTrue();
	}
	@Test
	public void TestU25() {
		assertThatThrownBy( ()->deckRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU26() throws SQLException {
		assertThat( deckRepository.add(new Deck(0)).getDeckId() ).isEqualTo(16);
	}
	@Test
	public void TestU27() throws SQLException {
		deckRepository.save( new Deck(1,4, "cool title", 2, "cool description", true, new Date(10)) );
		assertThat( deckRepository.getById(1).getTitle().matches("cool title") ).isTrue();
	}
	@Test
	public void TestU28() {
		assertThatThrownBy( ()->deckRepository.save( new Deck(100, 4, "cool title", 2, "cool description", true, new Date(10)) ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU29() throws SQLException {
		deckRepository.deleteById(13);
		assertThatThrownBy(()->deckRepository.getById(13)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU30() {
		assertThatThrownBy(()->deckRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Mindcard
	@Test
	public void TestU31() throws SQLException {
		assertThat( mindcardRepository.getById(1).getTitle().matches("Walking") ).isTrue();
	}
	@Test
	public void TestU32() {
		assertThatThrownBy( ()->mindcardRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU33() throws SQLException {
		assertThat( mindcardRepository.add(new Mindcard(0)).getMindcardId() ).isEqualTo(10);
	}
	@Test
	public void TestU34() throws SQLException {
		mindcardRepository.save( new Mindcard(1, 2, "pizza", 3, "burger") );
		assertThat( mindcardRepository.getById(1).getTitle().matches("pizza") ).isTrue();
	}
	@Test
	public void TestU35() {
		assertThatThrownBy( ()->mindcardRepository.save( new Mindcard(100, 2, "pizza", 3, "burger") ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU36() throws SQLException {
		mindcardRepository.deleteById(8);
		assertThatThrownBy(()->mindcardRepository.getById(8)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU37() {
		assertThatThrownBy(()->mindcardRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Card Group
	@Test
	public void TestU38() throws SQLException {
		assertThat( cardGroupRepository.getById(1).getTitle().matches("Everyday Knowledge") ).isTrue();
	}
	@Test
	public void TestU39() {
		assertThatThrownBy( ()->cardGroupRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU40() throws SQLException {
		assertThat( cardGroupRepository.add(new CardGroup(0)).getCardGroupId() ).isEqualTo(5);
	}
	@Test
	public void TestU41() throws SQLException {
		cardGroupRepository.save( new CardGroup(1, 2, "pizza", 3, "burger") );
		assertThat( cardGroupRepository.getById(1).getDescription().matches("burger") ).isTrue();
	}
	@Test
	public void TestU42() {
		assertThatThrownBy( ()->cardGroupRepository.save( new CardGroup(100, 2, "pizza", 3, "burger") ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU43() throws SQLException {
		cardGroupRepository.deleteById(3);
		assertThatThrownBy(()->cardGroupRepository.getById(3)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU44() {
		assertThatThrownBy(()->cardGroupRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Infocard
	@Test
	public void TestU45() throws SQLException {
		String desc = "Even if takes more time, you can be saving the environment by not taking a car";
		assertThat( infocardRepository.getById(1).getDescription().matches(desc) ).isTrue();
	}
	@Test
	public void TestU46() {
		assertThatThrownBy( ()->infocardRepository.getById(100) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU47() throws SQLException {
		assertThat( infocardRepository.add(new Infocard(0)).getInfocardId() ).isEqualTo(9);
	}
	@Test
	public void TestU48() throws SQLException {
		infocardRepository.save( new Infocard(1, 3, 4, "pizza") );
		assertThat( infocardRepository.getById(1).getImageId() ).isEqualTo(4);
	}
	@Test
	public void TestU49() {
		assertThatThrownBy( ()->infocardRepository.save( new Infocard(100, 3, 4, "pizza") ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU50() throws SQLException {
		infocardRepository.deleteById(7);
		assertThatThrownBy(()->infocardRepository.getById(7)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU51() {
		assertThatThrownBy(()->infocardRepository.deleteById(100)).isInstanceOf(EntityNotFoundException.class);
	}


	//Group Mindcard
	@Test
	public void TestU52() {
		assertThatCode( ()->groupMindcardRepository.getById(new GroupMindcard(1, 2)) ).doesNotThrowAnyException();
	}
	@Test
	public void TestU53() {
		assertThatThrownBy( ()->groupMindcardRepository.getById(new GroupMindcard(100, 100)) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU54() {
		assertThatCode( ()->groupMindcardRepository.add(new GroupMindcard(2, 3)) ).doesNotThrowAnyException();
	}
	@Test
	public void TestU55() {
		assertThatThrownBy( ()->groupMindcardRepository.add(new GroupMindcard(1, 1)) ).isInstanceOf(SQLException.class);
	}
	@Test
	public void TestU56() throws SQLException {
		groupMindcardRepository.deleteById(new GroupMindcard(1, 3));
		assertThatThrownBy(()->groupMindcardRepository.getById(new GroupMindcard(1, 3))).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU57() {
		assertThatThrownBy(()->groupMindcardRepository.deleteById(new GroupMindcard(100, 100))).isInstanceOf(EntityNotFoundException.class);
	}


	//Favourite
	@Test
	public void TestU58() throws SQLException {
		assertThat( favouriteRepository.getById( new Favourite(6, 3) ).getLastViewed().getTime() ).isEqualTo(7);
	}
	@Test
	public void TestU59() {
		assertThatThrownBy( ()->favouriteRepository.getById( new Favourite(12, 1) ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU60() throws SQLException {
		assertThat( favouriteRepository.add( new Favourite(1, 2, new Date(3)) ).getLastViewed() ).isEqualTo(new Date(3));
	}
	@Test
	public void TestU61() throws SQLException {
		favouriteRepository.save( new Favourite(1, 1, new Date(21)) );
		assertThat( favouriteRepository.getById( new Favourite(1, 1) ).getLastViewed().getTime() ).isEqualTo(21);
	}
	@Test
	public void TestU62() {
		assertThatThrownBy( ()->favouriteRepository.save( new Favourite(100, 100, new Date(21)) ) ).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU63() throws SQLException {
		favouriteRepository.deleteById(new Favourite(5, 3));
		assertThatThrownBy(()->favouriteRepository.getById(new Favourite(5, 3))).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU64() {
		assertThatThrownBy( ()->favouriteRepository.deleteById( new Favourite(100, 100) ) ).isInstanceOf(EntityNotFoundException.class);
	}

}
