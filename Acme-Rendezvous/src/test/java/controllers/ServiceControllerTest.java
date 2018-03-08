
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
import services.ServiceService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ServiceControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ServiceController		controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ServiceService			serviceService;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.serviceService);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}
	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@Test
	public void listRendezvousesPositive() throws Exception {
		//this.serviceService = Mockito.spy(this.controller.serviceService);
		//final Pageable pageable = new PageRequest(0, this.configurationService.findConfiguration().getPageSize());

		final MockHttpServletRequestBuilder gt;

		gt = MockMvcRequestBuilders.get("/service/list.do?anonymous=true");

		this.mockMvc
			.perform(gt)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("service/list"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(1)))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"services",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("id", Matchers.is(98304)), Matchers.hasProperty("pictureUrl", Matchers.is("http://coolwildlife.com/wp-content/uploads/galleries/post-3004/Fox%20Picture%20002.jpg")),
						Matchers.hasProperty("name", Matchers.is("Service 1")), Matchers.hasProperty("description", Matchers.is("Test service description")), Matchers.hasProperty("price", Matchers.is(10.0))))));

		//Mockito.verify(this.serviceService, Mockito.times(1)).findNotCancelledServices(pageable);
		//Mockito.verifyNoMoreInteractions(this.serviceService);
	}
}
