package controllers.Brotherhood;

import java.util.ArrayList;
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
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import forms.LegalRecordForm;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordController extends AbstractController {

	// Service----------------------------------------------------------------

	@Autowired
	private LegalRecordService legalRecordService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor--------------------------------------------------------------

	public LegalRecordController() {
		super();
	}

	// Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView;
		History history = historyService.findOne(historyId);
		try {
			Assert.notNull(history);
			final Collection<LegalRecord> legalRecords = new ArrayList<>(
					this.legalRecordService
							.findLegalRecordByHistoryId(historyId));

			modelAndView = new ModelAndView("legalRecord/list");
			modelAndView.addObject("legalRecords", legalRecords);
			modelAndView.addObject("historyId", historyId);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());
			modelAndView.addObject(
					"requestURI",
					"/legalRecord/brotherhood/list.do?historyId="
							+ history.getId());

		} catch (final Throwable e) {
			modelAndView = new ModelAndView("redirect:/brotherhood/list.do");
			if (history == null)
				redirectAttrs.addFlashAttribute("message",
						"history.error.historyUnexists");
		}

		return modelAndView;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		try {
			final LegalRecordForm legalRecordForm = new LegalRecordForm();
			legalRecordForm.setId(0);
			final Brotherhood b = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(b);
			final History history = this.historyService
					.findByBrotherhoodIdSingle(b.getId());
			Assert.notNull(history);
			legalRecordForm.setHistory(history);

			result = this.createModelAndView(legalRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int legalRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService
				.findOne(legalRecordId);
		final LegalRecordForm legalRecordForm = new LegalRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(legalRecord);
			Assert.isTrue(b.getId() == legalRecord.getHistory()
					.getBrotherhood().getId());

			legalRecordForm.setId(legalRecordId);
			legalRecordForm.setHistory(legalRecord.getHistory());
			legalRecordForm.setApplicableLaws(legalRecord.getApplicableLaws());
			legalRecordForm.setLegalName(legalRecord.getLegalName());
			legalRecordForm.setVATnumber(legalRecord.getVATnumber());
			legalRecordForm.setTitle(legalRecord.getTitle());
			legalRecordForm.setDescription(legalRecord.getDescription());

			result = this.showModelAndView(legalRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (legalRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"legalRecord.error.unexist");
			else if (b.getId() != legalRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"legalRecord.error.notFromActor");
		}

		return result;
	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService
				.findOne(legalRecordId);
		final LegalRecordForm legalRecordForm = new LegalRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(legalRecord);
			Assert.notNull(b);
			Assert.isTrue(legalRecord.getHistory().getBrotherhood().getId() == b
					.getId());
			legalRecordForm.setId(legalRecordId);
			legalRecordForm.setHistory(legalRecord.getHistory());
			legalRecordForm.setApplicableLaws(legalRecord.getApplicableLaws());
			legalRecordForm.setLegalName(legalRecord.getLegalName());
			legalRecordForm.setVATnumber(legalRecord.getVATnumber());
			legalRecordForm.setTitle(legalRecord.getTitle());
			legalRecordForm.setDescription(legalRecord.getDescription());

			result = this.editModelAndView(legalRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (legalRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"legalRecord.error.unexist");
			else if (b.getId() != legalRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"legalRecord.error.notFromActor");
		}

		return result;
	}

	// Save
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecordForm legalRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(legalRecordForm, "commit.error");
		else
			try {
				legalRecord.setLegalName(legalRecordForm.getLegalName());
				legalRecord.setVATnumber(legalRecordForm.getVATnumber());
				legalRecord.setApplicableLaws(legalRecordForm
						.getApplicableLaws());
				legalRecord.setTitle(legalRecordForm.getTitle());
				legalRecord.setDescription(legalRecordForm.getDescription());
				legalRecord.setHistory(legalRecordForm.getHistory());

				this.legalRecordService.save(legalRecord);

				result = new ModelAndView(
						"redirect:/legalRecord/brotherhood/list.do?historyId="
								+ legalRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.createModelAndView(legalRecordForm,
						"commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final LegalRecordForm legalRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		LegalRecord legalRecord = legalRecordService.findOne(legalRecordForm
				.getId());

		if (binding.hasErrors())
			result = this.editModelAndView(legalRecordForm, "commit.error");
		else
			try {
				Assert.notNull(legalRecord);
				Assert.notNull(b);
				Assert.isTrue(b.getId() == legalRecord.getHistory()
						.getBrotherhood().getId());

				legalRecord.setVATnumber(legalRecordForm.getVATnumber());
				legalRecord.setLegalName(legalRecordForm.getLegalName());
				legalRecord.setApplicableLaws(legalRecordForm
						.getApplicableLaws());
				legalRecord.setTitle(legalRecordForm.getTitle());
				legalRecord.setDescription(legalRecordForm.getDescription());

				this.legalRecordService.save(legalRecord);

				result = new ModelAndView(
						"redirect:/legalRecord/brotherhood/list.do?historyId="
								+ legalRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.editModelAndView(legalRecordForm, "commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalRecordForm legalRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService
				.findOne(legalRecordForm.getId());
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(legalRecord);
			Assert.isTrue(b.getId() == legalRecord.getHistory()
					.getBrotherhood().getId());
			this.legalRecordService.delete(legalRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			result = this.editModelAndView(legalRecordForm, "commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(
			final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(
			final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/create");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "legalRecord/brotherhood/create.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(
			final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(
			final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI",
				"legalRecord/brotherhood/edit.do?legalRecordId="
						+ legalRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", (this.configurationService).findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(
			final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(
			final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/show");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);
		result.addObject("requestURI",
				"legalRecord/brotherhood/show.do?legalRecordId="
						+ legalRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}