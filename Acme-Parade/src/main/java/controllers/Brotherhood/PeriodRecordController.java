
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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Actor;
import domain.PeriodRecord;
import forms.PeriodRecordForm;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private ActorService		actorService;


	//Constructor--------------------------------------------------------------

	public PeriodRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<PeriodRecord> periodRecords = new ArrayList<>(this.periodRecordService.findPeriodRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("periodRecord/list");
		modelAndView.addObject("periodRecords", periodRecords);

		modelAndView.addObject("requestURI", "/periodRecord/brotherhood/list.do");
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
		periodRecordForm.setId(0);

		result = this.createModelAndView(periodRecordForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int periodRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(periodRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == periodRecord.getHistory().getBrotherhood().getId());
			periodRecordForm.setId(periodRecordId);
			periodRecordForm.setStartYear(periodRecord.getStartYear());
			periodRecordForm.setEndYear(periodRecord.getEndYear());
			periodRecordForm.setPhotos(periodRecord.getPhotos());
			periodRecordForm.setTitle(periodRecord.getTitle());
			periodRecordForm.setDescription(periodRecord.getDescription());
			result = this.showModelAndView(periodRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/periodRecord/brotherhood/list.do");
			if (periodRecord == null)
				redirectAttrs.addFlashAttribute("message", "periodRecord.error.unexist");
			else if (actor.getId() != periodRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "periodRecord.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		final PeriodRecordForm periodRecordForm = new PeriodRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(periodRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == periodRecord.getHistory().getBrotherhood().getId());
			periodRecordForm.setId(periodRecordId);
			periodRecordForm.setStartYear(periodRecord.getStartYear());
			periodRecordForm.setEndYear(periodRecord.getEndYear());
			periodRecordForm.setPhotos(periodRecord.getPhotos());
			periodRecordForm.setTitle(periodRecord.getTitle());
			periodRecordForm.setDescription(periodRecord.getDescription());
			result = this.editModelAndView(periodRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/periodRecord/brotherhood/list.do?historyId=" + periodRecord.getHistory().getId());
			if (periodRecord == null)
				redirectAttrs.addFlashAttribute("message", "periodRecord.error.unexist");
			else if (actor.getId() != periodRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "periodRecord.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecordForm periodRecordForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor = new Actor();
		PeriodRecord periodRecord;
		if (periodRecordForm.getId() != 0)
			periodRecord = this.periodRecordService.findOne(periodRecordForm.getId());
		else
			periodRecord = new PeriodRecord();
		if (binding.hasErrors())
			result = this.editModelAndView(periodRecordForm);
		else
			try {
				System.out.println("owo" + periodRecord);
				Assert.notNull(periodRecord);
				System.out.println("uwu" + periodRecord);
				actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
				//Assert.isTrue(actor.getId() == periodRecord.getHistory().getBrotherhood().getId());
				System.out.println("iwi" + periodRecord);
				periodRecord.setStartYear(periodRecordForm.getStartYear());
				periodRecord.setEndYear(periodRecordForm.getEndYear());
				periodRecord.setPhotos(periodRecordForm.getPhotos());
				periodRecord.setTitle(periodRecordForm.getTitle());
				periodRecord.setDescription(periodRecordForm.getDescription());
				this.periodRecordService.save(periodRecord);

				result = new ModelAndView("redirect:/periodRecord/brotherhood/list.do?historyId=" + periodRecord.getHistory().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(periodRecordForm, "periodRecord.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final PeriodRecordForm periodRecordForm, final BindingResult binding) {

		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(periodRecordForm);
		else
			try {
				periodRecord.setStartYear(periodRecordForm.getStartYear());
				periodRecord.setEndYear(periodRecordForm.getEndYear());
				periodRecord.setPhotos(periodRecordForm.getPhotos());
				periodRecord.setTitle(periodRecordForm.getTitle());
				periodRecord.setDescription(periodRecordForm.getDescription());
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/periodRecord/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(periodRecordForm, "periodRecord.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PeriodRecordForm periodRecordForm, final BindingResult binding) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordForm.getId());
		Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		try {
			Assert.notNull(periodRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == periodRecord.getHistory().getBrotherhood().getId());
			this.periodRecordService.delete(periodRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(periodRecordForm, "periodRecord.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/create");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "periodRecord/create.do");
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "periodRecord/edit.do?periodRecordId=" + periodRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", ((ActorService) this.configurationService).findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final PeriodRecordForm periodRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(periodRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(final PeriodRecordForm periodRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/show");
		result.addObject("periodRecordForm", periodRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "periodRecord/show.do?periodRecordId=" + periodRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
