package com.raffier.mindcards;

import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class MindcardsApplicationTest {

	@Autowired
	private AppDatabase appDatabase;

	@Test
	public void contextLoads() throws Exception {

		assertThat(appDatabase.getConnection()).isNull();//appDatabase.getConnection() != null));

	}

}
