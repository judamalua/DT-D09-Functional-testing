
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	//Service under test ------------------------
	@Autowired
	private CommentService	commentService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Test
	public void testCreate() {

	}

	@Test
	public void testFindAll() {

	}

	@Test
	public void testFindOne() {

	}

	@Test
	public void testSave() {

	}

	@Test
	public void testDelete() {

	}
}
