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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;
import forms.MiscellaneousRecordForm;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordController extends AbstractController {

	// Service----------------------------------------------------------------

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor--------------------------------------------------------------

	public MiscellaneousRecordController() {
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
			final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>(
					this.miscellaneousRecordService
							.findMiscellaneousRecordByHistoryId(historyId));

			modelAndView = new ModelAndView("miscellaneousRecord/list");
			modelAndView
					.addObject("miscellaneousRecords", miscellaneousRecords);
			modelAndView.addObject("historyId", historyId);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());
			modelAndView.addObject("requestURI",
					"/miscellaneousRecord/brotherhood/list.do?historyId="
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
			final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
			miscellaneousRecordForm.setId(0);
			final Brotherhood b = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(b);
			final History history = this.historyService
					.findByBrotherhoodIdSingle(b.getId());
			Assert.notNull(history);
			miscellaneousRecordForm.setHistory(history);

			result = this.createModelAndView(miscellaneousRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int miscellaneousRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
				.findOne(miscellaneousRecordId);
		final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(miscellaneousRecord);
			Assert.isTrue(b.getId() == miscellaneousRecord.getHistory()
					.getBrotherhood().getId());

			miscellaneousRecordForm.setId(miscellaneousRecordId);
			miscellaneousRecordForm
					.setHistory(miscellaneousRecord.getHistory());
			miscellaneousRecordForm.setTitle(miscellaneousRecord.getTitle());
			miscellaneousRecordForm.setDescription(miscellaneousRecord
					.getDescription());

			result = this.showModelAndView(miscellaneousRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (miscellaneousRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"miscellaneousRecord.error.unexist");
			else if (b.getId() != miscellaneousRecord.getHistory()
					.getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message",
						"miscellaneousRecord.error.notFromActor");
		}

		return result;
	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
				.findOne(miscellaneousRecordId);
		final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(miscellaneousRecord);
			Assert.notNull(b);
			Assert.isTrue(miscellaneousRecord.getHistory().getBrotherhood()
					.getId() == b.getId());
			miscellaneousRecordForm.setId(miscellaneousRecordId);
			miscellaneousRecordForm
					.setHistory(miscellaneousRecord.getHistory());
			miscellaneousRecordForm.setTitle(miscellaneousRecord.getTitle());
			miscellaneousRecordForm.setDescription(miscellaneousRecord
					.getDescription());

			result = this.editModelAndView(miscellaneousRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (miscellaneousRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"miscellaneousRecord.error.unexist");
			else if (b.getId() != miscellaneousRecord.getHistory()
					.getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message",
						"miscellaneousRecord.error.notFromActor");
		}

		return result;
	}

	// Save
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(
			@Valid final MiscellaneousRecordForm miscellaneousRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
				.create();
		if (binding.hasErrors())
			result = this.createModelAndView(miscellaneousRecordForm,
					"commit.error");
		else
			try {
				miscellaneousRecord
						.setTitle(miscellaneousRecordForm.getTitle());
				miscellaneousRecord.setDescription(miscellaneousRecordForm
						.getDescription());
				miscellaneousRecord.setHistory(miscellaneousRecordForm
						.getHistory());

				this.miscellaneousRecordService.save(miscellaneousRecord);

				result = new ModelAndView(
						"redirect:/miscellaneousRecord/brotherhood/list.do?historyId="
								+ miscellaneousRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.createModelAndView(miscellaneousRecordForm,
						"commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(
			@Valid final MiscellaneousRecordForm miscellaneousRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		MiscellaneousRecord miscellaneousRecord = miscellaneousRecordService
				.findOne(miscellaneousRecordForm.getId());

		if (binding.hasErrors())
			result = this.editModelAndView(miscellaneousRecordForm,
					"commit.error");
		else
			try {
				Assert.notNull(miscellaneousRecord);
				Assert.notNull(b);
				Assert.isTrue(b.getId() == miscellaneousRecord.getHistory()
						.getBrotherhood().getId());

				miscellaneousRecord
						.setTitle(miscellaneousRecordForm.getTitle());
				miscellaneousRecord.setDescription(miscellaneousRecordForm
						.getDescription());

				this.miscellaneousRecordService.save(miscellaneousRecord);

				result = new ModelAndView(
						"redirect:/miscellaneousRecord/brotherhood/list.do?historyId="
								+ miscellaneousRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.editModelAndView(miscellaneousRecordForm,
						"commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(
			final MiscellaneousRecordForm miscellaneousRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
				.findOne(miscellaneousRecordForm.getId());
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(miscellaneousRecord);
			Assert.isTrue(b.getId() == miscellaneousRecord.getHistory()
					.getBrotherhood().getId());
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			result = this.editModelAndView(miscellaneousRecordForm,
					"commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/create");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI",
				"miscellaneousRecord/brotherhood/create.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI",
				"miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId="
						+ miscellaneousRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", (this.configurationService).findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(
			final MiscellaneousRecordForm miscellaneousRecordForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/show");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);
		result.addObject("requestURI",
				"miscellaneousRecord/brotherhood/show.do?miscellaneousRecordId="
						+ miscellaneousRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}
