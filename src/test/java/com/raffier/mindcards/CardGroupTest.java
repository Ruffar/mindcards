package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.GroupMindcard;
import com.raffier.mindcards.repository.table.GroupMindcardRepository;
import com.raffier.mindcards.service.CardGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class CardGroupTest {

	@Autowired
	private CardGroupService cardGroupService;
	@Autowired
	private GroupMindcardRepository groupMindcardRepository;

	@Test
	public void TestU117() throws SQLException {
		cardGroupService.addMindcardToCardGroup(5, 2);
		assertThat(groupMindcardRepository.getById(new GroupMindcard(2, 5))).isNotNull();
	}
	@Test
	public void TestU118() {
		assertThatThrownBy(()->cardGroupService.addMindcardToCardGroup(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU119() throws SQLException {
		cardGroupService.removeMindcardFromCardGroup(1, 1);
		assertThatThrownBy(()->groupMindcardRepository.getById(new GroupMindcard(5, 2))).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU120() {
		assertThatThrownBy(()->cardGroupService.removeMindcardFromCardGroup(100, 100)).isInstanceOf(EntityNotFoundException.class);
	}
	@Test
	public void TestU121() throws SQLException {
		assertThat(cardGroupService.isMindcardInCardGroup(2, 1)).isTrue();
	}
	@Test
	public void TestU122() throws SQLException {
		assertThat(cardGroupService.isMindcardInCardGroup(5, 1)).isFalse();
	}
}
