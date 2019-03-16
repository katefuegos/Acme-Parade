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
import services.ParadeService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import forms.ParadeForm;

@Controller
@RequestMapping("/parade/brotherhood")
public class ParadeBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ParadeService paradeService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public ParadeBrotherhoodController() {
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
			final Collection<Parade> paradesAccepted = paradeService
					.findByBrotherhoodIdAndAccepted(brotherhoodId);
			final Collection<Parade> paradesRejected = paradeService
					.findByBrotherhoodIdAndRejected(brotherhoodId);
			final Collection<Parade> paradesSubmitted = paradeService
					.findByBrotherhoodIdAndSubmitted(brotherhoodId);
			final Collection<Parade> paradesPending = paradeService
					.findByBrotherhoodIdAndPending(brotherhoodId);
			
			result = new ModelAndView("parade/list2");
			result.addObject("paradesAccepted", paradesAccepted);
			result.addObject("paradesRejected", paradesRejected);
			result.addObject("paradesSubmitted", paradesSubmitted);
			result.addObject("paradesPending", paradesPending);
			result.addObject("requestURI",
					"parade/brotherhood/list.do?brotherhoodId="
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
		ParadeForm paradeForm = new ParadeForm();
		paradeForm.setId(0);

		result = this.createModelAndView(paradeForm);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ParadeForm paradeForm,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(paradeForm, "commit.error");
		else
			try {
				Parade parade = paradeService.create();
				parade.setTitle(paradeForm.getTitle());
				parade.setDescription(paradeForm.getDescription());
				parade.setMoment(paradeForm.getMoment());
				parade.setDraftMode(paradeForm.isDraftMode());
				this.paradeService.save(parade);

				result = new ModelAndView(
						"redirect:/parade/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this
						.createModelAndView(paradeForm, "commit.error");
			}
		return result;
	}

	// EDIT
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int paradeId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Parade parade = null;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			parade = this.paradeService.findOne(paradeId);
			Assert.notNull(parade);
			Assert.isTrue(paradeService.findOne(paradeId)
					.getBrotherhood().equals(b));
			Assert.isTrue(paradeService.findOne(paradeId).isDraftMode());

			ParadeForm paradeForm = new ParadeForm();
			paradeForm.setId(parade.getId());
			paradeForm.setDescription(parade.getDescription());
			paradeForm.setDraftMode(parade.isDraftMode());
			paradeForm.setMoment(parade.getMoment());
			paradeForm.setTitle(parade.getTitle());

			result = this.editModelAndView(paradeForm);

		} catch (final Throwable e) {

			result = new ModelAndView(
					"redirect:/parade/brotherhood/list.do");
			if (paradeService.findOne(paradeId) == null)
				redirectAttrs.addFlashAttribute("message",
						"parade.error.unexist");
			else if (paradeService.findOne(paradeId).isDraftMode() == false)
				redirectAttrs.addFlashAttribute("message",
						"parade.error.notDraftMode");
			else if (!paradeService.findOne(paradeId).getBrotherhood()
					.equals(b))
				redirectAttrs.addFlashAttribute("message",
						"parade.error.notFromActor");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final ParadeForm paradeForm,
			final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(paradeForm, "commit.error");
		else
			try {
				Assert.notNull(paradeForm);
				Parade parade = paradeService
						.findOne(paradeForm.getId());
				Assert.isTrue(parade.getBrotherhood().equals(b));
				Assert.isTrue(parade.isDraftMode());
				parade.setDescription(paradeForm.getDescription());
				parade.setDraftMode(paradeForm.isDraftMode());
				parade.setMoment(paradeForm.getMoment());
				parade.setTitle(paradeForm.getTitle());

				this.paradeService.save(parade);

				result = new ModelAndView(
						"redirect:/parade/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(paradeForm, "commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final ParadeForm paradeForm,
			final BindingResult binding) {
		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.editModelAndView(paradeForm, "commit.error");
		else
			try {
				Assert.notNull(paradeForm);
				Parade parade = paradeService
						.findOne(paradeForm.getId());
				Assert.isTrue(parade.getBrotherhood().equals(b));
				Assert.isTrue(parade.isDraftMode());
				this.paradeService.delete(paradeService
						.findOne(paradeForm.getId()));

				result = new ModelAndView(
						"redirect:/parade/brotherhood/list.do");
			} catch (final Throwable oops) {

				result = this.editModelAndView(paradeForm, "commit.error");
			}
		return result;
	}

	// SHOW
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(final int paradeId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Parade parade = null;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			parade = this.paradeService.findOne(paradeId);
			Assert.notNull(parade);
			Assert.isTrue(parade.getBrotherhood().getId() == b.getId());

			ParadeForm paradeForm = new ParadeForm();
			paradeForm.setId(parade.getId());
			paradeForm.setDescription(parade.getDescription());
			paradeForm.setDraftMode(parade.isDraftMode());
			paradeForm.setMoment(parade.getMoment());
			paradeForm.setTitle(parade.getTitle());

			result = this.ShowModelAndView(paradeForm);

		} catch (final Throwable e) {

			result = new ModelAndView(
					"redirect:/parade/brotherhood/list.do");
			if (paradeService.findOne(paradeId) == null)
				redirectAttrs.addFlashAttribute("message",
						"parade.error.unexist");
			else if (paradeService.findOne(paradeId).isDraftMode() == false)
				redirectAttrs.addFlashAttribute("message",
						"parade.error.notDraftMode");
			else if (!paradeService.findOne(paradeId).getBrotherhood()
					.equals(b))
				redirectAttrs.addFlashAttribute("message",
						"parade.error.notFromActor");
		}
		return result;
	}

	// MODEL
	protected ModelAndView createModelAndView(
			final ParadeForm paradeForm) {
		ModelAndView result;
		result = this.createModelAndView(paradeForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(
			final ParadeForm paradeForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("parade/create");
		result.addObject("message1", message);
		result.addObject("requestURI", "parade/brotherhood/create.do");
		result.addObject("paradeForm", paradeForm);
		result.addObject("isRead", false);
		result.addObject("id", 0);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final ParadeForm paradeForm) {
		ModelAndView result;
		result = this.editModelAndView(paradeForm, null);
		return result;
	}

	protected ModelAndView editModelAndView(
			final ParadeForm paradeForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("parade/edit");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"parade/brotherhood/edit.do?paradeId="
						+ paradeForm.getId());
		result.addObject("paradeForm", paradeForm);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView ShowModelAndView(final ParadeForm paradeForm) {
		ModelAndView result;
		result = this.ShowModelAndView(paradeForm, null);
		return result;
	}

	protected ModelAndView ShowModelAndView(ParadeForm paradeForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("parade/show");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"parade/brotherhood/show.do?paradeId="
						+ paradeForm.getId());
		result.addObject("paradeForm", paradeForm);
		result.addObject("isRead", true);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}
