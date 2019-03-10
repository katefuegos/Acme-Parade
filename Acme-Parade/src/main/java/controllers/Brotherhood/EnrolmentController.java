package controllers.Brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import security.LoginService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.EnrolmentService;
import services.MemberService;
import services.PositionService;
import controllers.AbstractController;
import domain.Enrolment;
import forms.EnrolmentForm;
import forms.EnrolmentForm2;
import forms.EnrolmentForm3;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class EnrolmentController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public EnrolmentController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final Integer brotherhoodId = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId())
					.getId();
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));

			final Collection<Enrolment> enrolments = this.enrolmentService
					.findByBrotherhood(brotherhoodId);

			final Collection<EnrolmentForm> enrolmentForms = new ArrayList<EnrolmentForm>();
			for (final Enrolment e : enrolments)
				enrolmentForms.add(new EnrolmentForm(e, this.memberService
						.findByEnrolment(e)));

			final String lang = LocaleContextHolder.getLocale().getLanguage()
					.toUpperCase();
			result = new ModelAndView("enrolment/list");
			result.addObject("lang", lang);
			result.addObject("enrolmentForms", enrolmentForms);
			result.addObject("requestURI",
					"enrolment/brotherhood/list.do?brotherhoodId="
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

	// ENROL
	@RequestMapping(value = "/enrol", method = RequestMethod.GET)
	public ModelAndView enrol(final int enrolmentId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Integer brotherhoodId = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId())
				.getId();
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		EnrolmentForm2 enrolmentForm = new EnrolmentForm2();
		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			Assert.notNull(enrolment);
			Assert.isTrue(enrolment.getBrotherhood().equals(
					this.brotherhoodService.findOne(brotherhoodId)));
			Assert.isTrue(enrolment.isAccepted() == false);
			enrolmentForm.setId(enrolment.getId());
			result = this.enrolModelAndView(enrolmentForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");
			if (enrolment == null)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.unexist");
			else if (!enrolment.getBrotherhood().equals(
					this.brotherhoodService.findOne(brotherhoodId)))
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.noBrotherhood");
			else if (enrolment.isAccepted() == true)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.isAccepted");
			else
				result = this.enrolModelAndView(enrolmentForm, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/enrol", method = RequestMethod.POST, params = "save")
	public ModelAndView enrolSave(@Valid final EnrolmentForm2 enrolmentForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result = null;
		final Integer brotherhoodId = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId())
				.getId();
		Enrolment enrolment = enrolmentService.findOne(enrolmentForm.getId());
		if (binding.hasErrors())
			result = this.enrolModelAndView(enrolmentForm, "commit.error");
		else
			try {
				Assert.notNull(enrolment);
				Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
				Assert.notNull(enrolment);
				Assert.isTrue(enrolment.getBrotherhood().equals(
						this.brotherhoodService.findOne(brotherhoodId)));
				enrolment.setMomentDropOut(null);
				enrolment.setAccepted(true);
				enrolment.setMomentEnrol(new Date(
						System.currentTimeMillis() - 1000));
				enrolment.setPosition(enrolmentForm.getPosition());
				this.enrolmentService.save(enrolment);

				result = new ModelAndView(
						"redirect:/enrolment/brotherhood/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView(
						"redirect:/enrolment/brotherhood/list.do");
				if (enrolment == null)
					redirectAttrs.addFlashAttribute("message",
							"enrolment.error.unexist");
				else if (!enrolment.getBrotherhood().equals(
						this.brotherhoodService.findOne(brotherhoodId)))
					redirectAttrs.addFlashAttribute("message",
							"enrolment.error.noBrotherhood");
				else
					result = this.enrolModelAndView(enrolmentForm,
							"commit.error");
			}
		return result;
	}

	// DROPOUT
	@RequestMapping(value = "/dropout", method = RequestMethod.GET)
	public ModelAndView dropout(final int enrolmentId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Integer brotherhoodId = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId())
				.getId();
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		EnrolmentForm3 enrolmentForm = new EnrolmentForm3();
		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			Assert.notNull(enrolment);
			Assert.isTrue(enrolment.getBrotherhood().equals(
					this.brotherhoodService.findOne(brotherhoodId)));
			Assert.isTrue(enrolment.isAccepted() == true);
			enrolmentForm.setId(enrolment.getId());
			result = this.dropOutModelAndView(enrolmentForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");
			if (enrolment == null)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.unexist");
			else if (!enrolment.getBrotherhood().equals(
					this.brotherhoodService.findOne(brotherhoodId)))
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.noBrotherhood");
			else if (enrolment.isAccepted() == false)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.isNotAccepted");
			else
				result = this
						.dropOutModelAndView(enrolmentForm, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/dropout", method = RequestMethod.POST, params = "save")
	public ModelAndView dropOutSave(@Valid final EnrolmentForm3 enrolmentForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result = null;
		final Integer brotherhoodId = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId())
				.getId();
		Enrolment enrolment = enrolmentService.findOne(enrolmentForm.getId());
		if (binding.hasErrors())
			result = this.dropOutModelAndView(enrolmentForm, "commit.error");
		else
			try {
				Assert.notNull(enrolment);
				Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
				Assert.notNull(enrolment);
				Assert.isTrue(enrolment.getBrotherhood().equals(
						this.brotherhoodService.findOne(brotherhoodId)));
				enrolment.setMomentEnrol(null);
				enrolment.setAccepted(false);
				enrolment.setMomentDropOut(new Date(
						System.currentTimeMillis() - 1000));
				enrolment.setPosition(null);
				this.enrolmentService.save(enrolment);

				result = new ModelAndView(
						"redirect:/enrolment/brotherhood/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView(
						"redirect:/enrolment/brotherhood/list.do");
				if (enrolment == null)
					redirectAttrs.addFlashAttribute("message",
							"enrolment.error.unexist");
				else if (!enrolment.getBrotherhood().equals(
						this.brotherhoodService.findOne(brotherhoodId)))
					redirectAttrs.addFlashAttribute("message",
							"enrolment.error.noBrotherhood");
				else
					result = this.dropOutModelAndView(enrolmentForm,
							"commit.error");
			}
		return result;
	}

	protected ModelAndView enrolModelAndView(final EnrolmentForm2 enrolmentForm) {
		ModelAndView result;
		result = this.enrolModelAndView(enrolmentForm, null);
		return result;
	}

	protected ModelAndView enrolModelAndView(
			final EnrolmentForm2 enrolmentForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("enrolment/enrol");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"enrolment/brotherhood/enrol.do?enrolmentId="
						+ enrolmentForm.getId());
		result.addObject("enrolmentForm", enrolmentForm);
		result.addObject("positions", this.positionService.findAll());

		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView dropOutModelAndView(
			final EnrolmentForm3 enrolmentForm) {
		ModelAndView result;
		result = this.dropOutModelAndView(enrolmentForm, null);
		return result;
	}

	protected ModelAndView dropOutModelAndView(
			final EnrolmentForm3 enrolmentForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("enrolment/dropout");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"enrolment/brotherhood/dropout.do?enrolmentId="
						+ enrolmentForm.getId());
		result.addObject("enrolmentForm", enrolmentForm);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}
