package com.raffier.mindcards;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.FormFieldException;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.table.UserRepository;
import com.raffier.mindcards.service.EncryptionService;
import com.raffier.mindcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class UserAndEncryptionTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	@Autowired
	EncryptionService encryptionService;


	@Test
	public void TestU65() throws SQLException {
		assertThat(userService.getUser(1).getEmail().matches("admin@mindcards.com")).isTrue();
	}

	@Test
	public void TestU66() {
		assertThatThrownBy(()->userService.getUser(100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU67() throws SQLException {
		assertThat(userService.userRegister("johnny","johnny@fakemail.com","NewP4ssword").getUsername().equals("johnny")).isTrue();
	}

	@Test
	public void TestU68() {
		assertThatThrownBy(()->userService.userRegister("bo","bob@amail.com","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU69() throws SQLException {
		userService.userRegister("bob","bob@amail.com","Password123");
		assertThatCode(()->userRepository.getByEmail("bob@amail.com")).doesNotThrowAnyException();
	}

	@Test
	public void TestU70() {
		assertThatThrownBy(()->userService.userRegister("jack","thisisanemail","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU71() {
		assertThatThrownBy(()->userService.userRegister("jack","@","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU72() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","qwertyui")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU73() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","QWERT1234")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU74() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","Qwer123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU75() {
		assertThatCode(()->userService.userRegister("jack","jack@fakemail","Qwert1234")).doesNotThrowAnyException();
	}

	@Test
	public void TestU76() {
		String firstPass = encryptionService.encryptPassword("Password123", new User(1));
		String secondPass = encryptionService.encryptPassword("Password123", new User(2));

		assertThat(firstPass.matches(secondPass)).isFalse();
	}

}
