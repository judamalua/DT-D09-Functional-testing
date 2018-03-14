
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.DomainService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	public ActorService		actorService;
	@Autowired
	public CategoryService	categoryService;


	//******************************************Positive Methods*******************************************************************
	/**
	 * This test checks that a administrator can create a category
	 * 
	 * @author Luis
	 */
	@Test
	public void testAdminCanCreateACategory() {
		super.authenticate("Admin1");
		Category category;

		category = this.createStandardCategory();

		this.UpdateDataBase(category);

		super.unauthenticate();

	}

	/**
	 * This test checks that a administrator can list categories
	 * 
	 * @author Luis
	 */
	@Test
	public void testAdminCanListCategories() {
		super.authenticate("Admin1");

		this.categoryService.findAll();

		super.unauthenticate();

	}
	/**
	 * This test checks that a administrator can update categories
	 * 
	 * @author Luis
	 */
	@Test
	public void testAdminCanUpdateCategories() {
		super.authenticate("Admin1");
		Category category;

		category = (Category) this.categoryService.findAll().toArray()[0];
		category.setName("First Dates");

		this.UpdateDataBase(category);

		this.categoryService.findAll();

		super.unauthenticate();

	}

	/**
	 * This test checks that a administrator can delete a category
	 * 
	 * @author Luis
	 */
	@Test
	public void testAdminCanDeleteACategory() {
		super.authenticate("Admin1");
		Category category;
		Category savedCategory;

		category = this.createStandardCategory();
		savedCategory = this.categoryService.save(category);
		this.categoryService.flush();

		this.categoryService.delete(savedCategory);
		this.categoryService.flush();

		super.unauthenticate();

	}
	/**
	 * Categories are organised in hierarchies
	 * 
	 * @author Luis
	 */
	@Test
	public void testCategoriesAreOrganisedInHierarchies() {
		super.authenticate("Admin1");
		Category category;
		Category fatherCategory;
		Category savedFatherCategory;
		Category savedCategory;
		final Collection<DomainService> services = new HashSet<DomainService>();

		fatherCategory = this.createStandardCategory();
		fatherCategory.setName("FatherCategory");
		savedFatherCategory = this.categoryService.save(fatherCategory);

		this.categoryService.flush();

		category = this.categoryService.create();
		category.setName("NewCategory");
		category.setDescription("Description 1");
		category.setServices(services);
		category.setFatherCategory(savedFatherCategory);
		savedCategory = this.categoryService.save(category);

		this.categoryService.flush();

		Assert.isTrue(savedCategory.getFatherCategory() == savedFatherCategory);

		super.unauthenticate();

	}

	//******************************************Negative Methods*******************************************************************

	/**
	 * This test checks that a administrator cannot create a category with invalid parameters
	 * 
	 * @author Luis
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testAdminCantCreateACategoryWithInavildParameters() {
		super.authenticate("Admin1");
		Category category;

		category = this.createStandardCategory();
		category.setName("");//Invalid name: is blank
		category.setDescription("");//Invalid Description:is blank;

		this.UpdateDataBase(category);

		super.unauthenticate();

	}

	/**
	 * This test checks that a no logged actor cannot create a category
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoLoggedCantCreateACategory() {
		super.authenticate(null);
		Category category;

		category = this.createStandardCategory();

		this.UpdateDataBase(category);

		super.unauthenticate();

	}

	/**
	 * This method creates a category with correct attributes
	 * 
	 * @author Luis
	 */
	private Category createStandardCategory() {
		Category category;
		final Collection<DomainService> services = new HashSet<DomainService>();

		category = this.categoryService.create();
		category.setName("Category exmaple");
		category.setDescription("A example of a a description for a category");
		category.setServices(services);

		return category;

	}

	/**
	 * This method try to update a Entity in database(in this case Manager)
	 * 
	 * @author Luis
	 */
	private void UpdateDataBase(final Category category) {
		this.categoryService.save(category);
		this.categoryService.flush();
	}

}
