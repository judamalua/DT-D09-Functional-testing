
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SliderRepository;
import domain.Slider;

@Service
@Transactional
public class SliderService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SliderRepository	sliderRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------
	/**
	 * Creates a new slider
	 * 
	 * @author Daniel Diment
	 * @return the new slider
	 */
	public Slider create() {
		Slider result;

		result = new Slider();

		return result;
	}

	/**
	 * Gets all the sliders of the database
	 * 
	 * @author Daniel Diment
	 * @return The collection containing the all the sliders
	 */
	public Collection<Slider> findAll() {

		Collection<Slider> result;

		Assert.notNull(this.sliderRepository);
		result = this.sliderRepository.findAll();
		Assert.notNull(result);

		return result;

	}
	/**
	 * Gets the slider of the database that has that id
	 * 
	 * @param sliderId
	 *            The id you want to search
	 * @author Daniel Diment
	 * @return The slider with that id
	 */
	public Slider findOne(final int sliderId) {

		Slider result;

		result = this.sliderRepository.findOne(sliderId);

		return result;

	}

	/**
	 * Saves a slider to the database
	 * 
	 * @param slider
	 *            The slider you want to save
	 * @author Daniel Diment
	 * @return The saved slider
	 */
	public Slider save(final Slider slider) {

		assert slider != null;

		Slider result;

		result = this.sliderRepository.save(slider);

		return result;

	}

	/**
	 * Deletes a slider to the database
	 * 
	 * @author Daniel Diment
	 * @param answer
	 *            the slider to delete
	 */
	public void delete(final Slider slider) {

		assert slider != null;
		assert slider.getId() != 0;

		Assert.isTrue(this.sliderRepository.exists(slider.getId()));

		this.sliderRepository.delete(slider);

	}
}
