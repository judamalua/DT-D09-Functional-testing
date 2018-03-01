
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
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
	private ServiceService		serviceService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods --------------------------------------------------

	public Category create() {
		Category result;

		result = new Category();

		return result;
	}
	public Collection<Category> findAll() {

		Collection<Category> result;

		Assert.notNull(this.categoryRepository);
		result = this.categoryRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Category findOne(final int serviceId) {

		Category result;

		result = this.categoryRepository.findOne(serviceId);

		return result;

	}

	public Category save(final Category category) {

		assert category != null;

		Category result;
		Collection<Category> subCategories;

		result = this.categoryRepository.save(category);
		this.actorService.checkUserLogin();

		//Updating services
		for (final DomainService service : new HashSet<>(category.getServices())) {
			if (service.getCategories().contains(category))
				service.getCategories().remove(category);
			service.getCategories().add(result);
			this.serviceService.save(service);
		}
		subCategories = this.findSubCategories(category);
		//Updating subcategories
		for (final Category subCategory : subCategories) {
			subCategory.setFatherCategory(result);
			this.save(subCategory);
		}

		return result;
	}

	public void delete(final Category category) {

		assert category != null;
		assert category.getId() != 0;

		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		this.actorService.checkUserLogin();

		//Updating services
		for (final DomainService service : new HashSet<>(category.getServices())) {
			service.getCategories().remove(category);
			this.serviceService.save(service);
		}

		this.categoryRepository.delete(category);

	}

	Collection<Category> findSubCategories(final Category category) {
		Assert.isTrue(category.getId() != 0);

		Collection<Category> result;

		result = this.categoryRepository.findSubCategories(category.getId());

		return result;

	}

	// Other business methods

	public Category reconstruct(final Category category, final BindingResult binding) {
		Category result = null;

		//TODO: Complete
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
