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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;
import forms.PeriodRecordForm;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordController extends AbstractController {

	// Service----------------------------------------------------------------

	@Autowired
	private PeriodRecordService periodRecordService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor--------------------------------------------------------------

	public PeriodRecordController() {
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
			final Collection<PeriodRecord> periodRecords = new ArrayList<>(
					this.periodRecordService
							.findPeriodRecordByHistoryId(historyId));
			modelAndView = new ModelAndView("periodRecord/list");
			modelAndView.addObject("periodRecords", periodRecords);
			modelAndView.addObject("historyId", historyId);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());

			modelAndView.addObject("requestURI",
					"/periodRecord/brotherhood/list.do");
		} catch (final Throwable e) {
			modelAndView = new ModelAndView(
					"redirect:/history/brotherhood/list.do");
			if (history == null) {
				redirectAttrs.addFlashAttribute("message",
						"history.error.unexist2");
			}
		}

		return modelAndView;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		try {
			final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
			periodRecordForm.setId(0);
			final Brotherhood b = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(b);
			final History history = this.historyService
					.findByBrotherhoodIdSingle(b.getId());
			Assert.notNull(history);
			periodRecordForm.setHistory(history);

			result = this.createModelAndView(periodRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int periodRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService
				.findOne(periodRecordId);
		final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(periodRecord);
			Assert.isTrue(b.getId() == periodRecord.getHistory()
					.getBrotherhood().getId());

			periodRecordForm.setId(periodRecordId);
			periodRecordForm.setStartYear(periodRecord.getStartYear());
			periodRecordForm.setEndYear(periodRecord.getEndYear());
			periodRecordForm.setPhotos(periodRecord.getPhotos());
			periodRecordForm.setTitle(periodRecord.getTitle());
			periodRecordForm.setDescription(periodRecord.getDescription());

			result = this.showModelAndView(periodRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (periodRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"periodRecord.error.unexist");
			else if (b.getId() != periodRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"periodRecord.error.notFromActor");
		}

		return result;
	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService
				.findOne(periodRecordId);
		final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(periodRecord);
			Assert.notNull(b);
			Assert.isTrue(periodRecord.getHistory().getBrotherhood().getId() == b
					.getId());
			periodRecordForm.setId(periodRecordId);
			periodRecordForm.setHistory(periodRecord.getHistory());
			periodRecordForm.setStartYear(periodRecord.getStartYear());
			periodRecordForm.setEndYear(periodRecord.getEndYear());
			periodRecordForm.setPhotos(periodRecord.getPhotos());
			periodRecordForm.setTitle(periodRecord.getTitle());
			periodRecordForm.setDescription(periodRecord.getDescription());
			result = this.editModelAndView(periodRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (periodRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"periodRecord.error.unexist");
			else if (b.getId() != periodRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"periodRecord.error.notFromActor");
		}

		return result;
	}

	// Save
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecordForm periodRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(periodRecordForm, "commit.error");
		else
			try {
				periodRecord.setStartYear(periodRecordForm.getStartYear());
				periodRecord.setEndYear(periodRecordForm.getEndYear());
				periodRecord.setPhotos(periodRecordForm.getPhotos());
				periodRecord.setTitle(periodRecordForm.getTitle());
				periodRecord.setDescription(periodRecordForm.getDescription());
				periodRecord.setHistory(periodRecordForm.getHistory());
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView(
						"redirect:/periodRecord/brotherhood/list.do?historyId="
								+ periodRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.createModelAndView(periodRecordForm,
						"commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final PeriodRecordForm periodRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		PeriodRecord periodRecord = periodRecordService
				.findOne(periodRecordForm.getId());

		if (binding.hasErrors())
			result = this.editModelAndView(periodRecordForm, "commit.error");
		else
			try {
				Assert.notNull(periodRecord);
				Assert.notNull(b);
				Assert.isTrue(b.getId() == periodRecord.getHistory()
						.getBrotherhood().getId());

				periodRecord.setStartYear(periodRecordForm.getStartYear());
				periodRecord.setEndYear(periodRecordForm.getEndYear());
				periodRecord.setPhotos(periodRecordForm.getPhotos());
				periodRecord.setTitle(periodRecordForm.getTitle());
				periodRecord.setDescription(periodRecordForm.getDescription());
				this.periodRecordService.save(periodRecord);

				result = new ModelAndView(
						"redirect:/periodRecord/brotherhood/list.do?historyId="
								+ periodRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this
						.editModelAndView(periodRecordForm, "commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PeriodRecordForm periodRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService
				.findOne(periodRecordForm.getId());
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(periodRecord);
			Assert.isTrue(b.getId() == periodRecord.getHistory()
					.getBrotherhood().getId());
			this.periodRecordService.delete(periodRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			result = this.editModelAndView(periodRecordForm, "commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(
			final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(
			final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/create");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "periodRecord/brotherhood/create.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(
			final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(
			final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI",
				"periodRecord/brotherhood/edit.do?periodRecordId="
						+ periodRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", (this.configurationService).findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(
			final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(
			final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/show");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);
		result.addObject("requestURI",
				"periodRecord/brotherhood/show.do?periodRecordId="
						+ periodRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}
