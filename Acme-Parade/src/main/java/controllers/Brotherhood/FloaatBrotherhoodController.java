
package controllers.Brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.FloaatService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Floaat;
import domain.Procession;
import forms.FloaatForm;

@Controller
@RequestMapping("/float/brotherhood")
public class FloaatBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private FloaatService			floaatService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor---------------------------------------------------------

	public FloaatBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final Integer brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			result = new ModelAndView("floaat/list");
			final Collection<Floaat> floaats = this.floaatService.findByBrotherhoodId(brotherhoodId);
			result.addObject("requestURI", "float/brotherhood/list.do");
			result.addObject("floaats", floaats);
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/float/brotherhood/list.do");
		}
		return result;
	}

	// CREATE
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final FloaatForm floaatForm = new FloaatForm();
		floaatForm.setId(0);

		result = this.createModelAndView(floaatForm);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final FloaatForm floaatForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(floaatForm, "commit.error");
		else
			try {
				final Floaat floaat = this.floaatService.create();
				floaat.setTitle(floaatForm.getTitle());
				floaat.setDescription(floaatForm.getDescription());
				floaat.setPictures(floaatForm.getPictures());
				this.floaatService.save(floaat);

				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(floaatForm, "commit.error");
			}
		return result;
	}

	// EDIT
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int floaatId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Floaat floaat = null;
		final Brotherhood b = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			floaat = this.floaatService.findOne(floaatId);
			Assert.notNull(floaat);
			Assert.isTrue(this.floaatService.findOne(floaatId).getBrotherhood().equals(b));

			final FloaatForm floaatForm = new FloaatForm();
			floaatForm.setId(floaat.getId());
			floaatForm.setDescription(floaat.getDescription());
			floaatForm.setTitle(floaat.getTitle());
			floaatForm.setPictures(floaat.getPictures());

			result = this.editModelAndView(floaatForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/float/brotherhood/list.do");
			if (this.floaatService.findOne(floaatId) == null)
				redirectAttrs.addFlashAttribute("message", "floaat.error.unexist");
			else if (!this.floaatService.findOne(floaatId).getBrotherhood().equals(b))
				redirectAttrs.addFlashAttribute("message", "floaat.error.notFromActor");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final FloaatForm floaatForm, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(floaatForm, "commit.error");
		else
			try {
				Assert.notNull(floaatForm);
				final Floaat floaat = this.floaatService.findOne(floaatForm.getId());
				Assert.isTrue(floaat.getBrotherhood().equals(b));
				floaat.setDescription(floaatForm.getDescription());
				floaat.setPictures(floaatForm.getPictures());
				floaat.setTitle(floaatForm.getTitle());

				this.floaatService.save(floaat);

				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(floaatForm, "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final FloaatForm floaatForm, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(floaatForm, "commit.error");
		else
			try {
				Assert.notNull(floaatForm);
				final Floaat floaat = this.floaatService.findOne(floaatForm.getId());
				Assert.isTrue(floaat.getBrotherhood().equals(b));
				final Collection<Procession> processions = this.processionService.findByFloaat(floaat);
				if (!processions.isEmpty())
					for (final Procession p : processions) {
						p.getFloats().remove(floaat);
						this.processionService.save(p);
					}

				this.floaatService.delete(this.floaatService.findOne(floaatForm.getId()));

				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			} catch (final Throwable oops) {

				result = this.editModelAndView(floaatForm, "commit.error");
			}
		return result;
	}

	// SHOW
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final int floaatId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Floaat floaat = null;
		final Brotherhood b = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			floaat = this.floaatService.findOne(floaatId);
			Assert.notNull(floaat);
			Assert.isTrue(floaat.getBrotherhood().getId() == b.getId());

			final FloaatForm floaatForm = new FloaatForm();
			floaatForm.setId(floaat.getId());
			floaatForm.setDescription(floaat.getDescription());
			floaatForm.setTitle(floaat.getTitle());
			floaatForm.setPictures(floaat.getPictures());

			result = this.DisplayModelAndView(floaatForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/float/brotherhood/list.do");
			if (this.floaatService.findOne(floaatId) == null)
				redirectAttrs.addFlashAttribute("message", "floaat.error.unexist");
			else if (!this.floaatService.findOne(floaatId).getBrotherhood().equals(b))
				redirectAttrs.addFlashAttribute("message", "floaat.error.notFromActor");
		}
		return result;
	}

	// MODEL
	protected ModelAndView createModelAndView(final FloaatForm floaatForm) {
		ModelAndView result;
		result = this.createModelAndView(floaatForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(final FloaatForm floaatForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("floaat/create");
		result.addObject("message", message);
		result.addObject("requestURI", "float/brotherhood/create.do");
		result.addObject("floaatForm", floaatForm);
		result.addObject("isRead", false);
		result.addObject("id", 0);

		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final FloaatForm floaatForm) {
		ModelAndView result;
		result = this.editModelAndView(floaatForm, null);
		return result;
	}

	protected ModelAndView editModelAndView(final FloaatForm floaatForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("floaat/edit");
		result.addObject("message", message);
		result.addObject("requestURI", "float/brotherhood/edit.do?floaatId=" + floaatForm.getId());
		result.addObject("floaatForm", floaatForm);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;
	}

	protected ModelAndView DisplayModelAndView(final FloaatForm floaatForm) {
		ModelAndView result;
		result = this.DisplayModelAndView(floaatForm, null);
		return result;
	}

	protected ModelAndView DisplayModelAndView(final FloaatForm floaatForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("floaat/display");
		result.addObject("message", message);
		result.addObject("requestURI", "float/brotherhood/display.do?floaatId=" + floaatForm.getId());
		result.addObject("floaatForm", floaatForm);
		result.addObject("isRead", true);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;
	}
}
