
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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.Actor;
import domain.MiscellaneousRecord;
import forms.MiscellaneousRecordForm;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private ActorService				actorService;


	//Constructor--------------------------------------------------------------

	public MiscellaneousRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>(this.miscellaneousRecordService.findMiscellaneousRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("miscellaneousRecord/list");
		modelAndView.addObject("miscellaneousRecords", miscellaneousRecords);

		modelAndView.addObject("requestURI", "/miscellaneousRecord/brotherhood/list.do");
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
		miscellaneousRecordForm.setId(0);

		result = this.createModelAndView(miscellaneousRecordForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int miscellaneousRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(miscellaneousRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == miscellaneousRecord.getHistory().getBrotherhood().getId());
			miscellaneousRecordForm.setId(miscellaneousRecordId);
			miscellaneousRecordForm.setTitle(miscellaneousRecord.getTitle());
			miscellaneousRecordForm.setDescription(miscellaneousRecord.getDescription());
			result = this.showModelAndView(miscellaneousRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/miscellaneousRecord/brotherhood/list.do");
			if (miscellaneousRecord == null)
				redirectAttrs.addFlashAttribute("message", "miscellaneousRecord.error.unexist");
			else if (actor.getId() != miscellaneousRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "miscellaneousRecord.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		final MiscellaneousRecordForm miscellaneousRecordForm = new MiscellaneousRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(miscellaneousRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == miscellaneousRecord.getHistory().getBrotherhood().getId());
			miscellaneousRecordForm.setId(miscellaneousRecordId);
			miscellaneousRecordForm.setTitle(miscellaneousRecord.getTitle());
			miscellaneousRecordForm.setDescription(miscellaneousRecord.getDescription());
			result = this.editModelAndView(miscellaneousRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/miscellaneousRecord/brotherhood/list.do?historyId=" + miscellaneousRecord.getHistory().getId());
			if (miscellaneousRecord == null)
				redirectAttrs.addFlashAttribute("message", "miscellaneousRecord.error.unexist");
			else if (actor.getId() != miscellaneousRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "miscellaneousRecord.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecordForm miscellaneousRecordForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor = new Actor();
		MiscellaneousRecord miscellaneousRecord;
		if (miscellaneousRecordForm.getId() != 0)
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordForm.getId());
		else
			miscellaneousRecord = new MiscellaneousRecord();
		if (binding.hasErrors())
			result = this.editModelAndView(miscellaneousRecordForm);
		else
			try {
				System.out.println("owo" + miscellaneousRecord);
				Assert.notNull(miscellaneousRecord);
				System.out.println("uwu" + miscellaneousRecord);
				actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
				//Assert.isTrue(actor.getId() == miscellaneousRecord.getHistory().getBrotherhood().getId());
				System.out.println("iwi" + miscellaneousRecord);

				miscellaneousRecord.setTitle(miscellaneousRecordForm.getTitle());
				miscellaneousRecord.setDescription(miscellaneousRecordForm.getDescription());
				this.miscellaneousRecordService.save(miscellaneousRecord);

				result = new ModelAndView("redirect:/miscellaneousRecord/brotherhood/list.do?historyId=" + miscellaneousRecord.getHistory().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(miscellaneousRecordForm, "miscellaneousRecord.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final MiscellaneousRecordForm miscellaneousRecordForm, final BindingResult binding) {

		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(miscellaneousRecordForm);
		else
			try {
				miscellaneousRecord.setTitle(miscellaneousRecordForm.getTitle());
				miscellaneousRecord.setDescription(miscellaneousRecordForm.getDescription());
				this.miscellaneousRecordService.save(miscellaneousRecord);
				result = new ModelAndView("redirect:/miscellaneousRecord/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(miscellaneousRecordForm, "miscellaneousRecord.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecordForm miscellaneousRecordForm, final BindingResult binding) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordForm.getId());
		Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		try {
			Assert.notNull(miscellaneousRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == miscellaneousRecord.getHistory().getBrotherhood().getId());
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(miscellaneousRecordForm, "miscellaneousRecord.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/create");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "miscellaneousRecord/create.do");
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "miscellaneousRecord/edit.do?miscellaneousRecordId=" + miscellaneousRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", ((ActorService) this.configurationService).findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(miscellaneousRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(final MiscellaneousRecordForm miscellaneousRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/show");
		result.addObject("miscellaneousRecordForm", miscellaneousRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "miscellaneousRecord/show.do?miscellaneousRecordId=" + miscellaneousRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
