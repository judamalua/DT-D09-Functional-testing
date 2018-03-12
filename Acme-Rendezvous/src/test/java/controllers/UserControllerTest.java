
package controllers;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.ConfigurationService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class UserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private UserController			controller;

	//user under test ------------------------
	@Mock
	@Autowired
	private UserService				UserService;

	@Mock
	@Autowired
	private ConfigurationService	configurationuser;


	//Supporting Users -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.UserService);
		MockitoAnnotations.initMocks(this.configurationuser);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}
	/**
	 * Test the public list of Users in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the Users that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listUsersNotLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<User> users;
		Pageable pageable;

		super.authenticate("user1");
		pageable = new PageRequest(0, this.configurationuser.findConfiguration().getPageSize());

		users = this.UserService.getUsers(pageable);

		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=true");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasItems(Matchers.isIn(users.getContent()))));

	}

	/**
	 * Test the public list of Users in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the Users that are available in the system.
	 * 
	 * Must return 200 code.
	 * The anonymous is false and there is no one logged, the system must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@Test
	public void listUsersAnonymousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test the public list of Users in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the Users that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listUsersUserLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<User> Users;
		Pageable pageable;

		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=false");
		pageable = new PageRequest(0, this.configurationuser.findConfiguration().getPageSize());

		Users = this.UserService.getUsers(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasItems(Matchers.isIn(Users.getContent()))));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Users in the system,regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the Users that are available in the system.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listUsersManagerLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<User> Users;
		Pageable pageable;

		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=false");
		pageable = new PageRequest(0, this.configurationuser.findConfiguration().getPageSize());

		Users = this.UserService.getUsers(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasItems(Matchers.isIn(Users.getContent()))));
		super.unauthenticate();
	}
	/**
	 * Test the public list of Users in the system with pagination,regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the Users that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listUsersPageLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<User> Users;
		Pageable pageable;

		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=false&page=1");
		pageable = new PageRequest(1, this.configurationuser.findConfiguration().getPageSize());

		Users = this.UserService.getUsers(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(1))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(1))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("Users", Matchers.hasItems(Matchers.isIn(Users.getContent()))));
		super.unauthenticate();
	}
}
