package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.AreaService;
import services.ConfigurationService;
import domain.Area;

@Controller
@RequestMapping("/area")
public class AreaController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public AreaController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final Collection<Area> areas = this.areaService.findAll();
			result = new ModelAndView("area/list");
			result.addObject("areas", areas);
			result.addObject("requestURI", "area/list.do");
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/area/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Area area;
		area = this.areaService.create();
		result = this.createEditModelAndView(area);
		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Area area = null;
		try {
			area = this.areaService.findOne(areaId);
			Assert.notNull(area);

			result = this.createEditModelAndView(area);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/area/list.do");
			result.addObject("message",
					"org.hibernate.validator.constraints.URL.message");
			if (area == null) {
				redirectAttrs.addFlashAttribute("message",
						"area.error.unexist");
			}
		}
		return result;
	}

	// Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Area area, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(area);
		else
			try {
				this.areaService.save(area);
				result = new ModelAndView("redirect:/area/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(area, "area.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Area area, final BindingResult binding) {
		ModelAndView result;
		try {
			this.areaService.delete(area);
			result = new ModelAndView("redirect:/area/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage() == "area.error.used")
				result = this.createEditModelAndView(area, oops.getMessage());
			else
				result = this.createEditModelAndView(area, "area.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area) {
		ModelAndView result;

		result = this.createEditModelAndView(area, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area,
			final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("area/edit");
		result.addObject("area", area);

		result.addObject("message", messageCode);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		
		return result;
	}

}
