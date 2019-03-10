
package controllers.administrator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import controllers.AbstractController;
import domain.Configuration;
import forms.ConfigurationForm;

@Controller
@RequestMapping("/configuration/administrator")
public class ConfigurationAdministratorController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;


	//Constructor------------------------------------------------------------

	public ConfigurationAdministratorController() {

		super();

	}

	//List-------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final Configuration configuration = this.configurationService.findOne();

		result = new ModelAndView("configuration/list");

		result.addObject("configuration", configuration);

		result.addObject("requestURI", "configuration/administrator/list.do");
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;

	}

	//Edit-------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int configurationId) {

		final ModelAndView result;

		final ConfigurationForm configurationForm = new ConfigurationForm();

		final Configuration configuration = this.configurationService.findOne();

		Assert.notNull(configuration);

		configurationForm.setBannerr(configuration.getBanner());
		
		configurationForm.setId(configuration.getId());

		configurationForm.setSystemName(configuration.getSystemName());
		
		configurationForm.setCountryCode(configuration.getCountryCode());

		configurationForm.setFinderCacheTime(configuration.getFinderCacheTime());

		configurationForm.setFinderMaxResults(configuration.getFinderMaxResults());

		configurationForm.setWelcomeMessageES(configuration.getWelcomeMessage().get("ES"));

		configurationForm.setWelcomeMessageEN(configuration.getWelcomeMessage().get("EN"));

		configurationForm.setSpamWordsES(configuration.getSpamWords().get("ES"));

		configurationForm.setSpamWordsEN(configuration.getSpamWords().get("EN"));

		configurationForm.setPositiveWordsES(configuration.getPositiveWords().get("ES"));

		configurationForm.setPositiveWordsEN(configuration.getPositiveWords().get("EN"));

		configurationForm.setNegativeWordsES(configuration.getNegativeWords().get("ES"));

		configurationForm.setNegativeWordsEN(configuration.getNegativeWords().get("EN"));

		//		configurationForm.setPositionES(configuration.getPositions().get("ES"));
		//
		//		configurationForm.setPositionEN(configuration.getPositions().get("EN"));

		result = this.createEditModelAndView(configurationForm);

		return result;

	}

	//delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ConfigurationForm configurationForm, final BindingResult binding) {
		ModelAndView result;

		final Map<String, String> welcomeMessage = new HashMap<String, String>();

		welcomeMessage.put("EN", configurationForm.getWelcomeMessageEN());

		welcomeMessage.put("ES", configurationForm.getWelcomeMessageES());

		final Map<String, Collection<String>> spamWords = new HashMap<String, Collection<String>>();

		spamWords.put("EN", configurationForm.getSpamWordsEN());

		spamWords.put("ES", configurationForm.getSpamWordsES());

		final Map<String, Collection<String>> positiveWords = new HashMap<String, Collection<String>>();

		positiveWords.put("EN", configurationForm.getPositiveWordsEN());

		positiveWords.put("ES", configurationForm.getPositiveWordsES());

		final Map<String, Collection<String>> negativeWords = new HashMap<String, Collection<String>>();

		negativeWords.put("EN", configurationForm.getNegativeWordsEN());

		negativeWords.put("ES", configurationForm.getNegativeWordsES());
		//------------------------------------------------
		//------------------------------------------------
		//------------------------------------------------
		//		final Map<String, Collection<String>> positions = new HashMap<String, Collection<String>>();
		//
		//		positions.put("EN", configurationForm.getPositionEN());
		//
		//		positions.put("ES", configurationForm.getPositionES());
		//------------------------------------------------
		//------------------------------------------------
		//------------------------------------------------
		try {

			final Configuration configuration = this.configurationService.findOne();

			configuration.setBanner(configurationForm.getBannerr());

			configuration.setCountryCode(configurationForm.getCountryCode());

			configuration.setFinderCacheTime(configurationForm.getFinderCacheTime());

			configuration.setFinderMaxResults(configurationForm.getFinderMaxResults());

			configuration.setWelcomeMessage(welcomeMessage);

			configuration.setSpamWords(spamWords);

			configuration.setPositiveWords(positiveWords);

			configuration.setNegativeWords(negativeWords);

			//configuration.setPositions(positions);

			this.configurationService.delete(configuration);

			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {

			result = this.createEditModelAndView(configurationForm, "configuration.commit.error");
		}
		return result;
	}

	//Show-------------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int configurationId) {

		final ModelAndView result;

		final ConfigurationForm configurationForm = new ConfigurationForm();

		final Configuration configuration = this.configurationService.findOne();

		Assert.notNull(configuration);

		configurationForm.setId(configuration.getId());

		configurationForm.setBannerr(configuration.getBanner());

		configurationForm.setCountryCode(configuration.getCountryCode());

		configurationForm.setFinderCacheTime(configuration.getFinderCacheTime());

		configurationForm.setFinderMaxResults(configuration.getFinderMaxResults());

		configurationForm.setWelcomeMessageES(configuration.getWelcomeMessage().get("ES"));

		configurationForm.setWelcomeMessageEN(configuration.getWelcomeMessage().get("EN"));

		configurationForm.setSpamWordsES(configuration.getSpamWords().get("ES"));

		configurationForm.setSpamWordsEN(configuration.getSpamWords().get("EN"));

		configurationForm.setPositiveWordsES(configuration.getPositiveWords().get("ES"));

		configurationForm.setPositiveWordsEN(configuration.getPositiveWords().get("EN"));

		configurationForm.setNegativeWordsES(configuration.getNegativeWords().get("ES"));

		configurationForm.setNegativeWordsEN(configuration.getNegativeWords().get("EN"));

		//		configurationForm.setPositionES(configuration.getPositions().get("ES"));
		//
		//		configurationForm.setPositionEN(configuration.getPositions().get("EN"));

		result = this.createEditModelAndView(configurationForm);

		result.addObject("isRead", true);

		result.addObject("requestURI", "configuration/administrator/show.do?configurationId=" + configurationId);

		return result;

	}

	//Save-------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ConfigurationForm configurationForm, final BindingResult binding) {

		ModelAndView result;

		final Map<String, String> welcomeMessage = new HashMap<String, String>();

		welcomeMessage.put("EN", configurationForm.getWelcomeMessageEN());

		welcomeMessage.put("ES", configurationForm.getWelcomeMessageES());

		final Map<String, Collection<String>> spamWords = new HashMap<String, Collection<String>>();

		spamWords.put("EN", configurationForm.getSpamWordsEN());

		spamWords.put("ES", configurationForm.getSpamWordsES());

		final Map<String, Collection<String>> positiveWords = new HashMap<String, Collection<String>>();

		positiveWords.put("EN", configurationForm.getPositiveWordsEN());

		positiveWords.put("ES", configurationForm.getPositiveWordsES());

		final Map<String, Collection<String>> negativeWords = new HashMap<String, Collection<String>>();

		negativeWords.put("EN", configurationForm.getNegativeWordsEN());

		negativeWords.put("ES", configurationForm.getNegativeWordsES());
		//
		//		final Map<String, Collection<String>> positions = new HashMap<String, Collection<String>>();
		//		positions.put("EN", configurationForm.getPositionEN());
		//
		//		positions.put("ES", configurationForm.getPositionES());

		if (binding.hasErrors())

			result = this.createEditModelAndView(configurationForm);

		else

			try {

				final Configuration configuration = this.configurationService.findOne();

				configuration.setBanner(configurationForm.getBannerr());

				configuration.setCountryCode(configurationForm.getCountryCode());

				configuration.setFinderCacheTime(configurationForm.getFinderCacheTime());

				configuration.setFinderMaxResults(configurationForm.getFinderMaxResults());

				configuration.setWelcomeMessage(welcomeMessage);

				configuration.setSpamWords(spamWords);

				configuration.setPositiveWords(positiveWords);

				configuration.setNegativeWords(negativeWords);
				
				configuration.setSystemName(configurationForm.getSystemName());

				//configuration.setPositions(positions);

				this.configurationService.save(configuration);

				result = new ModelAndView("redirect:/configuration/administrator/list.do");

			} catch (final Throwable oops) {

				result = this.createEditModelAndView(configurationForm, "configuration.commit.error");

			}

		return result;

	}

	//ModelAndView-----------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final ConfigurationForm configurationForm) {

		ModelAndView result;

		result = this.createEditModelAndView(configurationForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final ConfigurationForm configurationForm, final String message) {

		ModelAndView result;

		result = new ModelAndView("configuration/edit");

		result.addObject("configurationForm", configurationForm);

		result.addObject("message", message);

		result.addObject("isRead", false);

		result.addObject("requestURI", "configuration/administrator/edit.do");
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;

	}

}
