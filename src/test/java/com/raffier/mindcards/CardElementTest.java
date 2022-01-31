package com.raffier.mindcards;

import com.raffier.mindcards.service.CardElementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class CardElementTest {

	@Autowired
	private CardElementService cardElementService;

	@Test
	public void TestU1() {

		assertThat(cardElementService.getInfocardElement(1).getPrimaryKey()).isEqualTo(1);//appDatabase.getConnection() != null));

	}

}
