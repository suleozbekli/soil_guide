package com.example.toprak_rehberi;

import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ToprakRehberiApplicationTests {

	@Test
	void contextLoads() {
	}



	@Autowired
	private UserRepository userRepository;

	@Test
	public void testFindByUsername() {
		User user = userRepository.findByUsername("Veli");
		System.out.println("Bulunan kullanıcı: " + user);
		assertNotNull(user);
		assertEquals("Veli", user.getUsername());
	}

}
