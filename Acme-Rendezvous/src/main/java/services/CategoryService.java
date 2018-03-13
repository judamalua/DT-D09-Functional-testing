
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
	 * Get all the Categories in the database
	 * 
	 * @return the collection of all the categories in the database
	 * @author MJ
	 */
	public Collection<Category> findAll() {

		Collection<Category> result;

		result = this.categoryRepository.findAll();
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

		this.checkCategory(category);

		result = this.categoryRepository.save(category);
		this.actorService.checkUserLogin();

		if (category.getId() != 0) {
			subCategories = this.findSubCategories(category);
			//Updating subcategories
			for (final Category subCategory : subCategories) {
				subCategory.setFatherCategory(result);
				this.save(subCategory);
			}
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
	 * @author MJ
	 */
	public void deleteRecursive(final Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getServices().size() == 0);

		Collection<Category> subCategories, subSubCategories;

		subCategories = this.findSubCategories(category);

		//iterate in every subCategory to delete it
		for (final Category subCategory : subCategories) {
			subSubCategories = this.findSubCategories(subCategory);

			if (subSubCategories.size() == 0) {//If the subCategory not have subCategories then it must be deleted
				Assert.isTrue(subCategory.getServices().size() == 0);
				this.categoryRepository.delete(subCategory);
			} else
				//In other case then call again the method
				this.deleteRecursive(subCategory);
		}
		//Finally delete the category
		this.categoryRepository.delete(category);
	}

	/**
	 * Get the first level of categories in the system
	 * 
	 * @param pageable
	 * @return the categories with father null
	 * @author MJ
	 */
	public Page<Category> findFirstLevelCategories(final Pageable pageable) {
		Assert.notNull(pageable);
		Page<Category> result;

		result = this.categoryRepository.findFirstLevelCategories(pageable);

		return result;
	}

	/**
	 * Get the first level of categories in the system
	 * 
	 * @param pageable
	 * @return the categories with father null
	 * @author MJ
	 */
	public Collection<Category> findFirstLevelCategories() {
		Collection<Category> result;

		result = this.categoryRepository.findFirstLevelCategories();

		return result;
	}

	/**
	 * Get all the subCategories in the hierarchy of the children categories
	 * 
	 * @param category
	 * @return all the subCategories in the Category
	 * @author MJ
	 */
	public Collection<Category> findAllSubcategories(final Category category) {
		Assert.notNull(category);

		Collection<Category> result, subCategories;
		result = new HashSet<>();

		if (category.getId() != 0) {
			subCategories = this.findSubCategories(category);
			for (final Category subCategory : subCategories)
				result.addAll(this.findAllSubcategories(subCategory));

			result.addAll(subCategories);
		}

		return result;
	}
	/**
	 * Checks that the category is correct(no cycles and no repeated names in his branch)
	 * 
	 * @param category
	 * @author MJ
	 */
	private void checkCategory(final Category category) {

		final boolean result;
		final Map<String, Category> mem;

		mem = new HashMap<String, Category>();
		result = this.isInvalid(category, mem);

		Assert.isTrue(result);
	}

	/**
	 * This method returns false if there is some category with the same name in his branch or any cycle
	 * 
	 * @param category
	 * @param memory
	 * @return true if there is no cycles or repeated
	 * @author MJ
	 */
	private boolean isInvalid(final Category category, final Map<String, Category> memory) {

		boolean result;
		Collection<Category> subCategories;

		//Get the upper lever of category (Brothers)
		if (category.getFatherCategory() != null)
			subCategories = this.findSubCategories(category.getFatherCategory());
		else
			subCategories = this.findFirstLevelCategories();

		//If we are creating a category subCategory don't contains the category and we must check it
		if (category.getId() == 0) {
			Assert.isTrue(!memory.keySet().contains(category.getName()), "Name must not be the repeated");
			memory.put(category.getName(), category);
		}

		//If we are not in the root then iterate the brothers
		if (category != null)
			for (final Category brotherCategory : subCategories) {
				//If we are not iterating the same category then see that the others categories don't have the same name
				Assert.isTrue(!memory.keySet().contains(brotherCategory.getName()), "Name must not be the repeated");

				//If everething is ok then put the category in the memory
				memory.put(brotherCategory.getName(), brotherCategory);
			}

		if (category.getFatherCategory() != null)
			//Assert.isTrue(memory.get(category.getFatherCategory().getName()) == null, "No cycles");
			Assert.isTrue(!memory.keySet().contains(category.getFatherCategory().getName()), "Name must not be the repeated");
		//If the father category is in memory yet then there is cycles or if the keys contains the name of the father then the names are the same
		//if the category is the root or is subCategory of root then there is no cycles
		if (category == null || category.getFatherCategory() == null)
			result = true;
		else
			//In other case we call again to the method to the father category, if there is some cycle then in some moment the algorithm will return false
			result = this.isInvalid(category.getFatherCategory(), memory);

		Assert.notNull(result);
		return result;
	}
	/**
	 * Get all the subCategories of the category passed as parameter
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
	 * Get all the subCategories of the category passed as parameter
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
			result.setDescription(category.getName());
			result.setDescription(category.getDescription());
			result.setFatherCategory(category.getFatherCategory());
			result.setName(result.getName());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public String getAverageRatioServicesInCategory() {
		String result;

		result = this.categoryRepository.getAverageRatioServicesInCategory();

		return result;
	}

	/**
	 * That method do a flush in database
	 * 
	 */
	public void flush() {
		this.categoryRepository.flush();
	}
}
