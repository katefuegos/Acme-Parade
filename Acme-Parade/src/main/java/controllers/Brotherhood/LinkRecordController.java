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
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;
import forms.LinkRecordForm;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordController extends AbstractController {

	// Service----------------------------------------------------------------

	@Autowired
	private LinkRecordService linkRecordService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor--------------------------------------------------------------

	public LinkRecordController() {
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
			final Collection<LinkRecord> linkRecords = new ArrayList<>(
					this.linkRecordService.findLinkRecordByHistoryId(historyId));

			modelAndView = new ModelAndView("linkRecord/list");
			modelAndView.addObject("linkRecords", linkRecords);
			modelAndView.addObject("historyId", historyId);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());
			modelAndView.addObject(
					"requestURI",
					"/linkRecord/brotherhood/list.do?historyId="
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
			final LinkRecordForm linkRecordForm = new LinkRecordForm();
			linkRecordForm.setId(0);
			final Brotherhood b = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(b);
			final History history = this.historyService
					.findByBrotherhoodIdSingle(b.getId());
			Assert.notNull(history);
			linkRecordForm.setHistory(history);

			result = this.createModelAndView(linkRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}

		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int linkRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService
				.findOne(linkRecordId);
		final LinkRecordForm linkRecordForm = new LinkRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(linkRecord);
			Assert.isTrue(b.getId() == linkRecord.getHistory().getBrotherhood()
					.getId());

			linkRecordForm.setId(linkRecordId);
			linkRecordForm.setHistory(linkRecord.getHistory());
			linkRecordForm.setBrotherhood(linkRecord.getBrotherhood());
			linkRecordForm.setTitle(linkRecord.getTitle());
			linkRecordForm.setDescription(linkRecord.getDescription());

			result = this.showModelAndView(linkRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (linkRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"linkRecord.error.unexist");
			else if (b.getId() != linkRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"linkRecord.error.notFromActor");
		}

		return result;
	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService
				.findOne(linkRecordId);
		final LinkRecordForm linkRecordForm = new LinkRecordForm();
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(linkRecord);
			Assert.notNull(b);
			Assert.isTrue(linkRecord.getHistory().getBrotherhood().getId() == b
					.getId());
			linkRecordForm.setId(linkRecordId);
			linkRecordForm.setHistory(linkRecord.getHistory());
			linkRecordForm.setTitle(linkRecord.getTitle());
			linkRecordForm.setDescription(linkRecord.getDescription());
			linkRecordForm.setBrotherhood(linkRecord.getBrotherhood());

			result = this.editModelAndView(linkRecordForm);

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
			if (linkRecord == null)
				redirectAttrs.addFlashAttribute("message",
						"linkRecord.error.unexist");
			else if (b.getId() != linkRecord.getHistory().getBrotherhood()
					.getId())
				redirectAttrs.addFlashAttribute("message",
						"linkRecord.error.notFromActor");
		}

		return result;
	}

	// Save
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecordForm linkRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(linkRecordForm, "commit.error");
		else
			try {
				linkRecord.setTitle(linkRecordForm.getTitle());
				linkRecord.setDescription(linkRecordForm.getDescription());
				linkRecord.setHistory(linkRecordForm.getHistory());
				linkRecord.setBrotherhood(linkRecordForm.getBrotherhood());

				this.linkRecordService.save(linkRecord);

				result = new ModelAndView(
						"redirect:/linkRecord/brotherhood/list.do?historyId="
								+ linkRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this
						.createModelAndView(linkRecordForm, "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final LinkRecordForm linkRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		LinkRecord linkRecord = linkRecordService.findOne(linkRecordForm
				.getId());

		if (binding.hasErrors())
			result = this.editModelAndView(linkRecordForm, "commit.error");
		else
			try {
				Assert.notNull(linkRecord);
				Assert.notNull(b);
				Assert.isTrue(b.getId() == linkRecord.getHistory()
						.getBrotherhood().getId());

				linkRecord.setTitle(linkRecordForm.getTitle());
				linkRecord.setDescription(linkRecordForm.getDescription());
				linkRecord.setBrotherhood(linkRecordForm.getBrotherhood());

				this.linkRecordService.save(linkRecord);

				result = new ModelAndView(
						"redirect:/linkRecord/brotherhood/list.do?historyId="
								+ linkRecord.getHistory().getId());

			} catch (final Throwable oops) {
				result = this.editModelAndView(linkRecordForm, "commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecordForm linkRecordForm,
			final BindingResult binding) {

		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService
				.findOne(linkRecordForm.getId());
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());

		try {
			Assert.notNull(linkRecord);
			Assert.isTrue(b.getId() == linkRecord.getHistory().getBrotherhood()
					.getId());
			this.linkRecordService.delete(linkRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			result = this.editModelAndView(linkRecordForm, "commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(
			final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(
			final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/create");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("brotherhoods", brotherhoodService.findAll());
		result.addObject("requestURI", "linkRecord/brotherhood/create.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(
			final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject(
				"requestURI",
				"linkRecord/brotherhood/edit.do?linkRecordId="
						+ linkRecordForm.getId());
		result.addObject("brotherhoods", brotherhoodService.findAll());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", (this.configurationService).findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(
			final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/show");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);
		result.addObject("brotherhoods", brotherhoodService.findAll());
		result.addObject(
				"requestURI",
				"linkRecord/brotherhood/show.do?linkRecordId="
						+ linkRecordForm.getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}
