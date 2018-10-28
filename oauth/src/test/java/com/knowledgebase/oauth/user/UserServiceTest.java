package com.knowledgebase.oauth.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.knowledgebase.oauth.role.Role;
import com.knowledgebase.oauth.role.RoleName;
import com.knowledgebase.oauth.role.RoleRepository;

public class UserServiceTest {

	@Mock
	private UserRepository mockUserRepository;
	@Mock
	private RoleRepository mockRoleRepository;
	@Mock
	private BCryptPasswordEncoder mockBCryptPasswordEncoder;

	private UserService userServiceUnderTest;
	private User user;

	private Role role;

	private String email;

	@Before
	public void setUp() {

		email = "test@test.com";

		initMocks(this);
		userServiceUnderTest = new UserService(mockUserRepository, mockRoleRepository, mockBCryptPasswordEncoder);
		user = User.builder().id(1).name("oscar").email("test@test.com").build();
		role = new Role();
		role.setId(1);
		role.setName(RoleName.ROLE_ADMIN);

		Mockito.when(mockUserRepository.save(any())).thenReturn(user);
		Mockito.when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
		Mockito.when(mockRoleRepository.findByName(any())).thenReturn(Optional.of(role));
	}

	@Test
	public void testFindUserByEmail() {
		// Run the test
		final Optional<User> result = userServiceUnderTest.findUserByEmail(email);

		// Verify the results
		assertEquals(email, result.get().getEmail());
	}

	@Test
	public void testSaveUser() {
		// Run the test
		User result = userServiceUnderTest.saveUser(User.builder().build());

		// Verify the results
		assertEquals(email, result.getEmail());
	}
}