
package controllers.administrator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ConfigurationService;
import services.PositionService;
import controllers.AbstractController;
import domain.Position;
import forms.PositionForm;

@Controller
@RequestMapping("/position/administrator")
public class PositionAdministratorController extends AbstractController {

	// Services-----------------------------------------------------------

	@Autowired
	private PositionService	positionService;
	
	@Autowired
	private ConfigurationService configurationService;


	// Constructor---------------------------------------------------------

	public PositionAdministratorController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<domain.Position> positions = this.positionService.findAll();
		final String lang = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("position/list");

		result.addObject("lang", lang);
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/administrator/list.do");
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	// CREATE
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final PositionForm positionForm = new PositionForm();

		result = this.createModelAndView(positionForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PositionForm positionForm, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createModelAndView(positionForm, "commit.error");
		else
			try {
				final Position position = this.positionService.create();
				final Map<String, String> map = new HashMap<String, String>();
				map.put("EN", positionForm.getNameEN());
				map.put("ES", positionForm.getNameES());
				position.setName(map);

				this.positionService.save(position);
				result = new ModelAndView("redirect:/position/administrator/list.do");

			} catch (final Throwable oops) {

				result = this.editModelAndView(positionForm, "commit.error");
			}
		return result;
	}

	// EDIT

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int positionId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Position position = null;
		final PositionForm positionForm = new PositionForm();

		try {
			position = this.positionService.findOne(positionId);
			Assert.isTrue(position != null);
			final Collection<Position> positions = this.positionService.findAll();
			if (position != null)
				positions.remove(position);

			positionForm.setId(position.getId());
			positionForm.setNameEN(position.getName().get("EN"));
			positionForm.setNameES(position.getName().get("ES"));

			result = new ModelAndView("position/edit");
			result.addObject("positionForm", positionForm);
			result.addObject("positions", positions);
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/position/administrator/list.do");
			if (position == null)
				redirectAttrs.addFlashAttribute("message", "position.error.unexist");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@Valid final PositionForm positionForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(positionForm, "commit.error");
		else
			try {
				final Position position = this.positionService.findOne(positionForm.getId());
				final Map<String, String> map = new HashMap<String, String>();
				map.put("EN", positionForm.getNameEN());
				map.put("ES", positionForm.getNameES());
				position.setName(map);

				this.positionService.save(position);
				result = new ModelAndView("redirect:/position/administrator/list.do");
			} catch (final Throwable oops) {
				if (oops.getMessage() == "position.error.used")
					result = this.editModelAndView(positionForm, oops.getMessage());
				else
					result = this.editModelAndView(positionForm, "commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteEdit(final PositionForm positionForm, final BindingResult binding) {
		ModelAndView result;

		try {
			final Position position = this.positionService.findOne(positionForm.getId());
			this.positionService.delete(position);

			result = new ModelAndView("redirect:/position/administrator/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage() == "position.error.used")
				result = this.editModelAndView(positionForm, oops.getMessage());
			else
				result = this.editModelAndView(positionForm, "commit.error");
		}
		return result;
	}

	// AUXILIARY METHODS

	protected ModelAndView createModelAndView(final PositionForm positionForm) {
		ModelAndView result;
		result = this.createModelAndView(positionForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(final PositionForm positionForm, final String message) {
		ModelAndView result;

		final Collection<Position> positions = this.positionService.findAll();

		result = new ModelAndView("position/create");
		result.addObject("message", message);
		result.addObject("requestURI", "position/administrator/create.do");
		result.addObject("positionForm", positionForm);
		result.addObject("positions", positions);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;
	}

	protected ModelAndView editModelAndView(final PositionForm positionForm) {
		ModelAndView result;
		result = this.editModelAndView(positionForm, null);
		return result;
	}

	protected ModelAndView editModelAndView(final PositionForm positionForm, final String message) {
		ModelAndView result;

		final Collection<Position> positions = this.positionService.findAll();
		final Position position = this.positionService.findOne(positionForm.getId());
		if (position != null)
			positions.remove(positions);

		result = new ModelAndView("position/edit");
		result.addObject("message", message);
		result.addObject("requestURI", "position/administrator/edit.do");
		result.addObject("positionForm", positionForm);
		result.addObject("positions", positions);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;
	}

}
