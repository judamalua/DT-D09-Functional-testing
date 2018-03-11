
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.ConfigurationService;
import services.CategoryService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class CategoryControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private CategoryController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private CategoryService		service;

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
	 * Test the public list of Categories in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listCategoryPositive() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/category/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("category/list")).andExpect(MockMvcResultMatchers.forwardedUrl("category/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(4)))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("category/list.do?")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0)))
			.andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(1)));
	}
	
	
	/**
	 * Test the public list of subcategories from a Category in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listSubCategoryPositive() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/category/list.do?categoryId="  + super.getEntityId("Category1"));

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("category/list")).andExpect(MockMvcResultMatchers.forwardedUrl("category/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.containsString("category/list.do?categoryId=")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0)))
			.andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(1)));
	}
	

	

}
