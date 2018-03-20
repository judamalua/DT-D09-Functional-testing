/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.SliderService;
import domain.Configuration;
import domain.Slider;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}


	@Autowired
	ConfigurationService	configurationService;

	@Autowired
	SliderService			sliderService;


	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") final String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("name", name);
		result.addObject("moment", moment);
		final Configuration configuration = this.configurationService.findConfiguration();
		result.addObject("cookies_es", configuration.getCookies_es());
		result.addObject("cookies_en", configuration.getCookies_eng());
		try {
			final Collection<Slider> sliders = this.sliderService.findAll();
			if (sliders.size() == 0)
				sliders.add(this.generateSampleSlider());
			result.addObject("sliders", sliders);

		} catch (final Throwable e) {
		}

		return result;
	}
	/**
	 * This method generates a sample slider in the case the admin haven't add any slider
	 * 
	 * @author Daniel Diment
	 * @return
	 *         The sample slide
	 */
	private Slider generateSampleSlider() {
		final Slider slider = this.sliderService.create();
		slider.setTitle_en("This is a sample slide");
		slider.setTitle_es("Esta es una diapositiva de ejemplo");
		slider.setText_en("To change the slides, login as an administrator go to configuration and press the configurate slides button");
		slider.setText_es("Para cambiar las diapositivas, entra en el sistema como administrador ve a la configuración y pulsa el botón de configurar diapositivas");
		slider.setPictureUrl("https://webhostingmedia.net/wp-content/uploads/2018/01/http-error-404-not-found.png");
		slider.setAlign("center");
		return slider;
	}
}
