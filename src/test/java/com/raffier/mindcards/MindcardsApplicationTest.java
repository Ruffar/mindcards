package com.raffier.mindcards;

import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class MindcardsApplicationTest {

	@Value("${dynamicContentDirectory}") private String directoryPath;

	@Autowired
	private AppDatabase appDatabase;

	@BeforeEach
	public void resetTestData() throws IOException {
		File testDatabase = new File(directoryPath+"/testData.db");
		File workingDatabase = new File(directoryPath+"/database.db");
		Files.copy(testDatabase.toPath(),workingDatabase.toPath(),REPLACE_EXISTING);
	}

	@Test
	public void contextLoads() throws Exception {

        System.out.println(appDatabase.getConnection().getMetaData().getURL());
		assertThat(appDatabase.getConnection()).isNotNull();//appDatabase.getConnection() != null));

	}

}
