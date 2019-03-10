
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

import security.LoginService;
import security.UserAccount;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.FloaatService;
import domain.Brotherhood;
import domain.Floaat;

@Controller
@RequestMapping("/float")
public class FloaatController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private FloaatService			floaatService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor---------------------------------------------------------

	public FloaatController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			final Collection<Floaat> floaats = this.floaatService.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("floaat/list");
			result.addObject("floaats", floaats);
			result.addObject("requestURI", "float/list.do?brotherhoodId=" + brotherhoodId);
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (this.brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message", "floaat.error.unexist");
		}
		return result;
	}

	@RequestMapping(value = "/listOwn", method = RequestMethod.GET)
	public ModelAndView listOwn(final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final UserAccount uA = LoginService.getPrincipal();
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(uA.getId()).getId();
		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			final Collection<Floaat> floaats = this.floaatService.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("float/list");
			result.addObject("floaats", floaats);
			result.addObject("requestURI", "float/listOwn.do");
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (this.brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message", "floaat.error.unexist");
		}
		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floaatId) {
		ModelAndView result;
		Floaat floaat;

		floaat = this.floaatService.findOne(floaatId);
		Assert.notNull(floaat);
		result = this.createEditModelAndView(floaat);

		return result;
	}

	// Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Floaat floaat, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(floaat);
		else
			try {
				this.floaatService.save(floaat);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(floaat, "floaat.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Floaat floaat) {
		ModelAndView result;
		try {
			this.floaatService.delete(floaat);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(floaat, "floaat.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Floaat floaat) {
		ModelAndView result;

		result = this.createEditModelAndView(floaat, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Floaat floaat, final String messageCode) {
		final ModelAndView result;
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("floaat/edit");
		result.addObject("floaat", floaat);
		result.addObject("brotherhoods", brotherhoods);

		result.addObject("message", messageCode);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
