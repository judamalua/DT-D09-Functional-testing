
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Actor;
import domain.Administrator;
import domain.Category;
import domain.DomainService;

@Service
@Transactional
public class CategoryService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CategoryRepository	categoryRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * Create a new instance of a Category
	 * 
	 * @return the instance created of a Category7
	 * @author MJ
	 */
	public Category create() {
		Category result;

		result = new Category();

		return result;
	}

	/**
	 * Get all the Categories in the database
	 * 
	 * @return the collection of all the categories in the database
	 * @author MJ
	 */
	public Page<Category> findAll(final Pageable pageable) {

		Page<Category> result;

		Assert.notNull(this.categoryRepository);
		result = this.categoryRepository.findAll(pageable);
		Assert.notNull(result);

		return result;

	}

	/**
	 * Get the category with the id passed as parameter
	 * 
	 * @param categoryId
	 * @return the Category with id categoryId
	 * @author MJ
	 */
	public Category findOne(final int categoryId) {

		Category result;

		result = this.categoryRepository.findOne(categoryId);

		return result;

	}

	/**
	 * Saves the category passed as parameter in the database
	 * 
	 * @param category
	 * @return the saved Category in the database
	 * @author MJ
	 */
	public Category save(final Category category) {

		assert category != null;

		Category result;
		Collection<Category> subCategories;

		result = this.categoryRepository.save(category);
		this.actorService.checkUserLogin();

		subCategories = this.findSubCategories(category);
		//Updating subcategories
		for (final Category subCategory : subCategories) {
			subCategory.setFatherCategory(result);
			this.save(subCategory);
		}

		return result;
	}

	/**
	 * Delete the category passed as parameter
	 * 
	 * @param category
	 */
	public void delete(final Category category) {

		assert category != null;
		assert category.getId() != 0;

		Assert.isTrue(this.categoryRepository.exists(category.getId()));
		Actor actor;

		this.actorService.checkUserLogin();

		actor = this.actorService.findActorByPrincipal();
		Assert.isTrue(actor instanceof Administrator);

		this.deleteRecursive(category);

	}

	/**
	 * Delete the category passed as parameter and his subCategories recursively
	 * 
	 * @param category
	 */
	public void deleteRecursive(final Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getServices().size() == 0);

		Collection<Category> subCategories, subSubCategories;

		subCategories = this.findSubCategories(category);
		for (final Category subCategory : subCategories) {
			subSubCategories = this.findSubCategories(subCategory);
			if (subSubCategories.size() == 0) {//If the subCategory not have subCategories then it must be deleted
				Assert.isTrue(subCategory.getServices().size() == 0);
				this.categoryRepository.delete(subCategory);
			} else
				//In other case then call again the method
				this.deleteRecursive(subCategory);
		}

		this.categoryRepository.delete(category);
	}

	/**
	 * Get all the subCategories of the category passed as paremater
	 * 
	 * @param category
	 * @return the collection of subCategories of category
	 * @author MJ
	 */
	public Collection<Category> findSubCategories(final Category category) {
		Assert.isTrue(category.getId() != 0);

		Collection<Category> result;

		result = this.categoryRepository.findSubCategories(category.getId());

		return result;

	}

	/**
	 * Get all the subCategories of the category passed as paremater
	 * 
	 * @param category
	 * @return the collection of subCategories of category
	 * @author MJ
	 */
	public Page<Category> findSubCategories(final Category category, final Pageable pageable) {
		Assert.isTrue(category.getId() != 0);

		Page<Category> result;

		result = this.categoryRepository.findSubCategories(category.getId(), pageable);

		return result;

	}

	// Other business methods

	/**
	 * Reconstruct the Category passed as parameter and his errors if exists in binding
	 * 
	 * @param category
	 * @param binding
	 * @return the reconstructed Category and his binding
	 * @author MJ
	 */
	public Category reconstruct(final Category category, final BindingResult binding) {
		Category result;

		if (category.getId() == 0) {

			Collection<DomainService> services;
			services = new HashSet<DomainService>();

			result = category;
			result.setServices(services);

		} else {
			result = this.categoryRepository.findOne(category.getId());
			result.setDescription(category.getDescription());
			result.setFatherCategory(category.getFatherCategory());
			result.setName(result.getName());
		}

		this.validator.validate(result, binding);

		return result;
	}
}
