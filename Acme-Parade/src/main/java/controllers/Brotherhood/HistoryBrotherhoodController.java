package controllers.Brotherhood;

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
import services.EveryRecordService;
import services.HistoryService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.EveryRecord;
import domain.History;
import forms.HistoryForm;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private HistoryService historyService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private EveryRecordService everyRecordService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public HistoryBrotherhoodController() {
		super();
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView;
		final Brotherhood brotherhood = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		final int brotherhoodId = brotherhood.getId();
		final Collection<History> historys = this.historyService
				.findByBrotherhoodId(brotherhoodId);

		Boolean vacio = false;
		if (historys.isEmpty()) {
			vacio = true;
		}

		modelAndView = new ModelAndView("history/list");
		modelAndView.addObject("historys", historys);
		modelAndView.addObject("vacio", vacio);
		modelAndView.addObject("requestURI", "/list.do?brotherhoodId="
				+ brotherhoodId);
		modelAndView.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		modelAndView.addObject("systemName", this.configurationService
				.findAll().iterator().next().getSystemName());

		return modelAndView;

	}

	// Delete
	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int historyId,
			final RedirectAttributes redirectAttrs) {

		ModelAndView result = null;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		final History history = this.historyService.findOne(historyId);
		try {
			Assert.notNull(b);
			Assert.notNull(history);
			Assert.isTrue(history.getBrotherhood().getId() == b.getId());

			Collection<EveryRecord> records = everyRecordService
					.findByHistoryId(historyId);
			if (!records.isEmpty()) {
				for (EveryRecord e : records) {
					everyRecordService.delete(e);
				}
			}

			this.historyService.delete(history);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (history == null) {
				redirectAttrs.addFlashAttribute("message",
						"history.error.unexist2");
			} else if (history.getBrotherhood().getId() != b.getId()) {
				redirectAttrs.addFlashAttribute("message",
						"history.error.nobrotherhood");
			} else {
				redirectAttrs.addFlashAttribute("message", "commit.error");
			}
		}

		return result;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final HistoryForm historyForm = new HistoryForm();
		try {
			final Brotherhood b = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(b);
			Collection<History> histories = historyService
					.findByBrotherhoodId(b.getId());
			Assert.isTrue(histories.isEmpty());
			historyForm.setBrotherhood(b);
			historyForm.setId(0);

			result = this.createModelAndView(historyForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	// Save
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final HistoryForm historyForm,
			final BindingResult binding) {

		ModelAndView result;
		final History history = this.historyService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(historyForm, "commit.error");
		else
			try {
				final Brotherhood b = this.brotherhoodService
						.findByUserAccountId(LoginService.getPrincipal()
								.getId());
				Assert.notNull(b);
				Collection<History> histories = historyService
						.findByBrotherhoodId(b.getId());
				Assert.isTrue(histories.isEmpty());
				history.setPhotos(historyForm.getPhotos());
				history.setTitle(historyForm.getTitle());
				history.setDescription(historyForm.getDescription());
				history.setBrotherhood(historyForm.getBrotherhood());
				this.historyService.save(history);
				result = new ModelAndView(
						"redirect:/history/brotherhood/list.do");

			} catch (final Throwable oops) {
				result = this.createModelAndView(historyForm, "commit.error");
			}

		return result;
	}

	// EDIT
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final History history = this.historyService.findOne(historyId);
		final HistoryForm historyForm = new HistoryForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(history);
			Assert.notNull(b);
			Assert.isTrue(history.getBrotherhood().getId() == b.getId());
			historyForm.setId(history.getId());
			historyForm.setPhotos(history.getPhotos());
			historyForm.setTitle(history.getTitle());
			historyForm.setDescription(history.getDescription());
			historyForm.setBrotherhood(history.getBrotherhood());

			result = this.editModelAndView(historyForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (history == null)
				redirectAttrs.addFlashAttribute("message",
						"history.error.historyUnexists");
			else if (b.getId() != history.getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message",
						"history.error.nobrotherhood");
			else
				redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final HistoryForm historyForm,
			final BindingResult binding) {

		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		History history = historyService.findOne(historyForm.getId());

		if (binding.hasErrors())
			result = this.editModelAndView(historyForm, "commit.error");
		else
			try {
				Assert.notNull(history);
				Assert.notNull(b);
				Assert.isTrue(b.getId() == history.getBrotherhood().getId());

				history.setPhotos(historyForm.getPhotos());
				history.setTitle(historyForm.getTitle());
				history.setDescription(historyForm.getDescription());
				this.historyService.save(history);

				result = new ModelAndView(
						"redirect:/history/brotherhood/list.do");

			} catch (final Throwable oops) {
				result = this.editModelAndView(historyForm, "commit.error");
			}

		return result;
	}

	// ModelAndView

	protected ModelAndView createModelAndView(final HistoryForm historyForm) {
		ModelAndView result;

		result = this.createModelAndView(historyForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final HistoryForm historyForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("history/create");
		result.addObject("historyForm", historyForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI", "history/brotherhood/create.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final HistoryForm historyForm) {
		ModelAndView result;

		result = this.editModelAndView(historyForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final HistoryForm historyForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("history/edit");
		result.addObject("historyForm", historyForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "history/brotherhood/edit.do?historyId="
				+ historyForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", (this.configurationService).findAll()
				.iterator().next().getSystemName());
		return result;
	}
}