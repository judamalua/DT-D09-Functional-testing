
package services;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import utilities.AbstractTest;
import controllers.ServiceController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ServiceControllerTest extends AbstractTest {

	private MockMvc			mockMvc;

	//Service under test ------------------------
	@Autowired
	private ServiceService	serviceService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ServiceController()).build();
	}

	@Test
	public void listServices() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/service/list.htm?anonymous=true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("service/list"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("/webapp/views/service/list.jsp"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(1)))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"services",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("id", Matchers.is(98304)), Matchers.hasProperty("pictureUrl", Matchers.is("http://coolwildlife.com/wp-content/uploads/galleries/post-3004/Fox%20Picture%20002.jpg")),
						Matchers.hasProperty("name", Matchers.is("Service 1")), Matchers.hasProperty("description", Matchers.is("Test service description")), Matchers.hasProperty("price", Matchers.is(10.0))))));

		Mockito.verify(this.serviceService, Mockito.times(1)).findAll();
		Mockito.verifyNoMoreInteractions(this.serviceService);
	}
}
