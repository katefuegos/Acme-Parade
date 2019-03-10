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
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Procession;
import forms.ProcessionForm;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ProcessionService processionService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public ProcessionBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Integer brotherhoodId = brotherhoodService.findByUserAccountId(
					LoginService.getPrincipal().getId()).getId();
			Assert.notNull(brotherhoodService.findOne(brotherhoodId));
			final Collection<Procession> processions = processionService
					.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("procession/list2");
			result.addObject("processions", processions);
			result.addObject("requestURI",
					"procession/brotherhood/list.do?brotherhoodId="
							+ brotherhoodId);
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	// CREATE
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ProcessionForm processionForm = new ProcessionForm();
		processionForm.setId(0);

		result = this.createModelAndView(processionForm);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ProcessionForm processionForm,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(processionForm, "commit.error");
		else
			try {
				Procession procession = processionService.create();
				procession.setTitle(processionForm.getTitle());
				procession.setDescription(processionForm.getDescription());
				procession.setMoment(processionForm.getMoment());
				procession.setDraftMode(processionForm.isDraftMode());
				this.processionService.save(procession);

				result = new ModelAndView(
						"redirect:/procession/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this
						.createModelAndView(processionForm, "commit.error");
			}
		return result;
	}

	// EDIT
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int processionId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Procession procession = null;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			procession = this.processionService.findOne(processionId);
			Assert.notNull(procession);
			Assert.isTrue(processionService.findOne(processionId)
					.getBrotherhood().equals(b));
			Assert.isTrue(processionService.findOne(processionId).isDraftMode());

			ProcessionForm processionForm = new ProcessionForm();
			processionForm.setId(procession.getId());
			processionForm.setDescription(procession.getDescription());
			processionForm.setDraftMode(procession.isDraftMode());
			processionForm.setMoment(procession.getMoment());
			processionForm.setTitle(procession.getTitle());

			result = this.editModelAndView(processionForm);

		} catch (final Throwable e) {

			result = new ModelAndView(
					"redirect:/procession/brotherhood/list.do");
			if (processionService.findOne(processionId) == null)
				redirectAttrs.addFlashAttribute("message",
						"procession.error.unexist");
			else if (processionService.findOne(processionId).isDraftMode() == false)
				redirectAttrs.addFlashAttribute("message",
						"procession.error.notDraftMode");
			else if (!processionService.findOne(processionId).getBrotherhood()
					.equals(b))
				redirectAttrs.addFlashAttribute("message",
						"procession.error.notFromActor");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final ProcessionForm processionForm,
			final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(processionForm, "commit.error");
		else
			try {
				Assert.notNull(processionForm);
				Procession procession = processionService
						.findOne(processionForm.getId());
				Assert.isTrue(procession.getBrotherhood().equals(b));
				Assert.isTrue(procession.isDraftMode());
				procession.setDescription(processionForm.getDescription());
				procession.setDraftMode(processionForm.isDraftMode());
				procession.setMoment(processionForm.getMoment());
				procession.setTitle(processionForm.getTitle());

				this.processionService.save(procession);

				result = new ModelAndView(
						"redirect:/procession/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(processionForm, "commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final ProcessionForm processionForm,
			final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(processionForm, "commit.error");
		else
			try {
				Assert.notNull(processionForm);
				Procession procession = processionService
						.findOne(processionForm.getId());
				Assert.isTrue(procession.getBrotherhood().equals(b));
				Assert.isTrue(procession.isDraftMode());
				this.processionService.delete(processionService
						.findOne(processionForm.getId()));

				result = new ModelAndView(
						"redirect:/procession/brotherhood/list.do");
			} catch (final Throwable oops) {

				result = this.editModelAndView(processionForm, "commit.error");
			}
		return result;
	}

	// SHOW
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(final int processionId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Procession procession = null;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			procession = this.processionService.findOne(processionId);
			Assert.notNull(procession);
			Assert.isTrue(procession.getBrotherhood().getId() == b.getId());

			ProcessionForm processionForm = new ProcessionForm();
			processionForm.setId(procession.getId());
			processionForm.setDescription(procession.getDescription());
			processionForm.setDraftMode(procession.isDraftMode());
			processionForm.setMoment(procession.getMoment());
			processionForm.setTitle(procession.getTitle());

			result = this.ShowModelAndView(processionForm);

		} catch (final Throwable e) {

			result = new ModelAndView(
					"redirect:/procession/brotherhood/list.do");
			if (processionService.findOne(processionId) == null)
				redirectAttrs.addFlashAttribute("message",
						"procession.error.unexist");
			else if (processionService.findOne(processionId).isDraftMode() == false)
				redirectAttrs.addFlashAttribute("message",
						"procession.error.notDraftMode");
			else if (!processionService.findOne(processionId).getBrotherhood()
					.equals(b))
				redirectAttrs.addFlashAttribute("message",
						"procession.error.notFromActor");
		}
		return result;
	}

	// MODEL
	protected ModelAndView createModelAndView(
			final ProcessionForm processionForm) {
		ModelAndView result;
		result = this.createModelAndView(processionForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(
			final ProcessionForm processionForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("procession/create");
		result.addObject("message1", message);
		result.addObject("requestURI", "procession/brotherhood/create.do");
		result.addObject("processionForm", processionForm);
		result.addObject("isRead", false);
		result.addObject("id", 0);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final ProcessionForm processionForm) {
		ModelAndView result;
		result = this.editModelAndView(processionForm, null);
		return result;
	}

	protected ModelAndView editModelAndView(
			final ProcessionForm processionForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("procession/edit");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"procession/brotherhood/edit.do?processionId="
						+ processionForm.getId());
		result.addObject("processionForm", processionForm);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView ShowModelAndView(final ProcessionForm processionForm) {
		ModelAndView result;
		result = this.ShowModelAndView(processionForm, null);
		return result;
	}

	protected ModelAndView ShowModelAndView(ProcessionForm processionForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("procession/show");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"procession/brotherhood/show.do?processionId="
						+ processionForm.getId());
		result.addObject("processionForm", processionForm);
		result.addObject("isRead", true);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}
