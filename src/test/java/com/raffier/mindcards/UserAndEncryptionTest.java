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
	public void TestU10() {
		assertThat(userService.getUser(1).getEmail().matches("admin@mindcards.com")).isTrue();
	}

	@Test
	public void TestU11() {
		assertThatThrownBy(()->userService.getUser(100)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	public void TestU12() {
		assertThat(userService.userRegister("johnny","johnny@fakemail.com","NewP4ssword").getUsername().equals("johnny")).isTrue();
	}

	@Test
	public void TestU13() {
		assertThatThrownBy(()->userService.userRegister("bo","bob@amail.com","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU14() {
		userService.userRegister("bob","bob@amail.com","Password123");
		assertThatThrownBy(()->userRepository.getByEmail("bob@amail.com")).doesNotThrowAnyException();
	}

	@Test
	public void TestU15() {
		assertThatThrownBy(()->userService.userRegister("jack","thisisanemail","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU16() {
		assertThatThrownBy(()->userService.userRegister("jack","@","Password123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU17() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","qwertyui")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU18() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","QWERT1234")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU19() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","Qwer123")).isInstanceOf(FormFieldException.class);
	}

	@Test
	public void TestU20() {
		assertThatThrownBy(()->userService.userRegister("jack","jack@fakemail","Qwert1234")).doesNotThrowAnyException();
	}

	@Test
	public void TestU21() {
		String firstPass = encryptionService.encryptPassword("Password123", new User(1));
		String secondPass = encryptionService.encryptPassword("Password123", new User(2));

		assertThat(firstPass.matches(secondPass)).isFalse();
	}

}
