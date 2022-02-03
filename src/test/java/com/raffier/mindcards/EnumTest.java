package com.raffier.mindcards;

import com.raffier.mindcards.util.CardType;
import com.raffier.mindcards.util.ImageChangeType;
import com.raffier.mindcards.util.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class EnumTest {

	// Card Type
	@Test
	public void TestU1() {
		assertThat(CardType.getCardTypeFromString("none")).isEqualTo(CardType.NONE);
	}

	@Test
	public void TestU2() {
		assertThat(CardType.getCardTypeFromString("mindCARD")).isEqualTo(CardType.MINDCARD);
	}

	@Test
	public void TestU3() {
		assertThat(CardType.getCardTypeFromString("pizza")).isEqualTo(CardType.NONE);
	}


	// Image Change Type
	@Test
	public void TestU4() {
		assertThat(ImageChangeType.getImageChangeTypeFromString("none")).isEqualTo(ImageChangeType.NONE);
	}

	@Test
	public void TestU5() {
		assertThat(ImageChangeType.getImageChangeTypeFromString("UPload")).isEqualTo(ImageChangeType.UPLOAD);
	}

	@Test
	public void TestU6() {
		assertThat(ImageChangeType.getImageChangeTypeFromString("burger")).isEqualTo(ImageChangeType.NONE);
	}


	// Sort Type
	@Test
	public void TestU7() {
		assertThat(SortType.getSortTypeFromString("none")).isEqualTo(SortType.NONE);
	}

	@Test
	public void TestU8() {
		assertThat(SortType.getSortTypeFromString("pOPUlar")).isEqualTo(SortType.POPULAR);
	}

	@Test
	public void TestU9() {
		assertThat(SortType.getSortTypeFromString("soda")).isEqualTo(SortType.NONE);
	}

}
