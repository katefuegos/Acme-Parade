
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
import services.ActorService;
import services.HistoryService;
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Actor;
import domain.History;
import domain.LegalRecord;
import forms.LegalRecordForm;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private HistoryService		historyService;


	//Constructor--------------------------------------------------------------

	public LegalRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<LegalRecord> legalRecords = new ArrayList<>(this.legalRecordService.findLegalRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("legalRecord/list");
		modelAndView.addObject("legalRecords", legalRecords);
		modelAndView.addObject("historyId", historyId);

		modelAndView.addObject("requestURI", "/legalRecord/brotherhood/list.do");
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int historyId) {
		ModelAndView result;
		final LegalRecordForm legalRecordForm = new LegalRecordForm();
		legalRecordForm.setId(0);
		final History history = this.historyService.findOne(historyId);
		legalRecordForm.setHistory(history);
		result = this.createModelAndView(legalRecordForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int legalRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		final LegalRecordForm legalRecordForm = new LegalRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(legalRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == legalRecord.getHistory().getBrotherhood().getId());
			legalRecordForm.setId(legalRecordId);
			legalRecordForm.setVATnumber(legalRecordForm.getVATnumber());
			legalRecordForm.setLegalName(legalRecordForm.getLegalName());
			legalRecordForm.setApplicableLaws(legalRecordForm.getApplicableLaws());
			legalRecordForm.setTitle(legalRecordForm.getTitle());
			legalRecordForm.setDescription(legalRecordForm.getDescription());
			result = this.showModelAndView(legalRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/legalRecord/brotherhood/list.do");
			if (legalRecord == null)
				redirectAttrs.addFlashAttribute("message", "legalRecord.error.unexist");
			else if (actor.getId() != legalRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "legalRecord.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		final LegalRecordForm legalRecordForm = new LegalRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(legalRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == legalRecord.getHistory().getBrotherhood().getId());
			legalRecordForm.setId(legalRecordId);
			legalRecordForm.setVATnumber(legalRecord.getVATnumber());
			legalRecordForm.setLegalName(legalRecord.getLegalName());
			legalRecordForm.setApplicableLaws(legalRecord.getApplicableLaws());
			legalRecordForm.setTitle(legalRecord.getTitle());
			legalRecordForm.setDescription(legalRecord.getDescription());
			legalRecordForm.setHistory(legalRecord.getHistory());
			result = this.editModelAndView(legalRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/legalRecord/brotherhood/list.do?historyId=" + legalRecord.getHistory().getId());
			if (legalRecord == null)
				redirectAttrs.addFlashAttribute("message", "legalRecord.error.unexist");
			else if (actor.getId() != legalRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "legalRecord.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecordForm legalRecordForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor = new Actor();
		LegalRecord legalRecord;
		if (legalRecordForm.getId() != 0)
			legalRecord = this.legalRecordService.findOne(legalRecordForm.getId());
		else
			legalRecord = new LegalRecord();
		if (binding.hasErrors())
			result = this.editModelAndView(legalRecordForm);
		else
			try {
				System.out.println("owo" + legalRecord);
				Assert.notNull(legalRecord);
				System.out.println("uwu" + legalRecord);
				actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
				//Assert.isTrue(actor.getId() == legalRecord.getHistory().getBrotherhood().getId());
				System.out.println("iwi" + legalRecord);
				legalRecord.setVATnumber(legalRecordForm.getVATnumber());
				legalRecord.setLegalName(legalRecordForm.getLegalName());
				legalRecord.setApplicableLaws(legalRecordForm.getApplicableLaws());
				legalRecord.setTitle(legalRecordForm.getTitle());
				legalRecord.setDescription(legalRecordForm.getDescription());
				legalRecord.setHistory(legalRecordForm.getHistory());
				this.legalRecordService.save(legalRecord);

				result = new ModelAndView("redirect:/legalRecord/brotherhood/list.do?historyId=" + legalRecord.getHistory().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(legalRecordForm, "legalRecord.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final LegalRecordForm legalRecordForm, final BindingResult binding) {

		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(legalRecordForm);
		else
			try {
				legalRecord.setVATnumber(legalRecordForm.getVATnumber());
				legalRecord.setLegalName(legalRecordForm.getLegalName());
				legalRecord.setApplicableLaws(legalRecordForm.getApplicableLaws());
				legalRecord.setTitle(legalRecordForm.getTitle());
				legalRecord.setDescription(legalRecordForm.getDescription());
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:/legalRecord/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(legalRecordForm, "legalRecord.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalRecordForm legalRecordForm, final BindingResult binding) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordForm.getId());
		Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		try {
			Assert.notNull(legalRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == legalRecord.getHistory().getBrotherhood().getId());
			this.legalRecordService.delete(legalRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(legalRecordForm, "legalRecord.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/create");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "legalRecord/create.do");
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "legalRecord/edit.do?legalRecordId=" + legalRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", ((ActorService) this.configurationService).findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final LegalRecordForm legalRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(legalRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(final LegalRecordForm legalRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/show");
		result.addObject("legalRecordForm", legalRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "legalRecord/show.do?legalRecordId=" + legalRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
