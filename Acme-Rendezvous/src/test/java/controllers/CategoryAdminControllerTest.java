
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.CategoryService;
import services.ConfigurationService;
import utilities.AbstractTest;
import controllers.admin.CategoryAdminController;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class CategoryAdminControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private CategoryAdminController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private CategoryService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.service);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}
	/**
	 * Test edit view of Category in the system. Must return 200 code.
	 * The logged admin get the view of a Category.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void editCategoryPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("admin1");
		final int categoryId = super.getEntityId("Category3");
		request = MockMvcRequestBuilders.get("/category/admin/edit.do?categoryId=" + categoryId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("category/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("category/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("category", Matchers.hasProperty("id", Matchers.is(categoryId)))).andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(5)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test the creation of Category in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void createCategoryPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("admin1");
		request = MockMvcRequestBuilders.get("/category/admin/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("category/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("category/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("category", Matchers.hasProperty("id", Matchers.is(0)))).andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(0)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));
		super.unauthenticate();
	}

	/**
	 * Test create a Category in the system. Must return 302 code.
	 * There is user logged and must not be displayed.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void createCategoryLoggedUserNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/category/admin/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}

	/**
	 * Test create a Category in the system. Must return 302 code.
	 * There is user logged and must not be displayed.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void createCategoryLoggedManagerNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Manager1");
		request = MockMvcRequestBuilders.get("/category/admin/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}

	/**
	 * Test create a Category in the system. Must return 302 code.
	 * There is anyone logged and must not be displayed.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void createCategoryNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		request = MockMvcRequestBuilders.get("/category/admin/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}

	/**
	 * Test save a Category in the system. Must return 302 code (redirection to list).
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void saveCategoryPositive() throws Exception {
		super.authenticate("admin1");

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/category/admin/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Category").param("description", "New Description").sessionAttr("category", new Category()).param("save", ""))
			.andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/category/list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("/category/list.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test delete a Category in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void deleteCategoryPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("admin1");
		final int categoryId;
		final Category category;

		categoryId = super.getEntityId("Category3");
		category = this.service.findOne(categoryId);
		request = MockMvcRequestBuilders.post("/category/admin/edit.do").param("delete", "").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("category", category);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/category/list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("/category/list.do?pagesize=5"));

		super.unauthenticate();
	}

}
