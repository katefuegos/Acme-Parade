
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
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.Actor;
import domain.History;
import domain.InceptionRecord;
import forms.InceptionRecordForm;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class InceptionRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private HistoryService			historyService;


	//Constructor--------------------------------------------------------------

	public InceptionRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<InceptionRecord> inceptionRecords = new ArrayList<>(this.inceptionRecordService.findInceptionRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("inceptionRecord/list");
		modelAndView.addObject("inceptionRecords", inceptionRecords);
		modelAndView.addObject("historyId", historyId);

		modelAndView.addObject("requestURI", "/inceptionRecord/brotherhood/list.do");
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int historyId) {
		ModelAndView result;
		final InceptionRecordForm inceptionRecordForm = new InceptionRecordForm();
		inceptionRecordForm.setId(0);
		final History history = this.historyService.findOne(historyId);
		inceptionRecordForm.setHistory(history);
		result = this.createModelAndView(inceptionRecordForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int inceptionRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
		final InceptionRecordForm inceptionRecordForm = new InceptionRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(inceptionRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == inceptionRecord.getHistory().getBrotherhood().getId());
			inceptionRecordForm.setId(inceptionRecordId);
			inceptionRecordForm.setPhotos(inceptionRecord.getPhotos());
			inceptionRecordForm.setTitle(inceptionRecord.getTitle());
			inceptionRecordForm.setDescription(inceptionRecord.getDescription());
			result = this.showModelAndView(inceptionRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/inceptionRecord/brotherhood/list.do");
			if (inceptionRecord == null)
				redirectAttrs.addFlashAttribute("message", "inceptionRecord.error.unexist");
			else if (actor.getId() != inceptionRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "inceptionRecord.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
		final InceptionRecordForm inceptionRecordForm = new InceptionRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(inceptionRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == inceptionRecord.getHistory().getBrotherhood().getId());
			inceptionRecordForm.setId(inceptionRecordId);
			inceptionRecordForm.setPhotos(inceptionRecord.getPhotos());
			inceptionRecordForm.setTitle(inceptionRecord.getTitle());
			inceptionRecordForm.setDescription(inceptionRecord.getDescription());
			inceptionRecordForm.setHistory(inceptionRecord.getHistory());
			result = this.editModelAndView(inceptionRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/inceptionRecord/brotherhood/list.do?historyId=" + inceptionRecord.getHistory().getId());
			if (inceptionRecord == null)
				redirectAttrs.addFlashAttribute("message", "inceptionRecord.error.unexist");
			else if (actor.getId() != inceptionRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "inceptionRecord.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecordForm inceptionRecordForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor = new Actor();
		InceptionRecord inceptionRecord;
		if (inceptionRecordForm.getId() != 0)
			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordForm.getId());
		else
			inceptionRecord = new InceptionRecord();
		if (binding.hasErrors())
			result = this.editModelAndView(inceptionRecordForm);
		else
			try {
				System.out.println("owo" + inceptionRecord);
				Assert.notNull(inceptionRecord);
				System.out.println("uwu" + inceptionRecord);
				actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
				//Assert.isTrue(actor.getId() == inceptionRecord.getHistory().getBrotherhood().getId());
				System.out.println("iwi" + inceptionRecord);
				inceptionRecord.setPhotos(inceptionRecordForm.getPhotos());
				inceptionRecord.setTitle(inceptionRecordForm.getTitle());
				inceptionRecord.setDescription(inceptionRecordForm.getDescription());
				inceptionRecord.setHistory(inceptionRecordForm.getHistory());
				this.inceptionRecordService.save(inceptionRecord);

				result = new ModelAndView("redirect:/inceptionRecord/brotherhood/list.do?historyId=" + inceptionRecord.getHistory().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(inceptionRecordForm, "inceptionRecord.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final InceptionRecordForm inceptionRecordForm, final BindingResult binding) {

		ModelAndView result;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(inceptionRecordForm);
		else
			try {
				inceptionRecord.setPhotos(inceptionRecordForm.getPhotos());
				inceptionRecord.setTitle(inceptionRecordForm.getTitle());
				inceptionRecord.setDescription(inceptionRecordForm.getDescription());
				this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/inceptionRecord/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(inceptionRecordForm, "inceptionRecord.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final InceptionRecordForm inceptionRecordForm, final BindingResult binding) {
		ModelAndView result;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordForm.getId());
		Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		try {
			Assert.notNull(inceptionRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == inceptionRecord.getHistory().getBrotherhood().getId());
			this.inceptionRecordService.delete(inceptionRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(inceptionRecordForm, "inceptionRecord.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(final InceptionRecordForm inceptionRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(inceptionRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final InceptionRecordForm inceptionRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/create");
		result.addObject("inceptionRecordForm", inceptionRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "inceptionRecord/create.do");
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final InceptionRecordForm inceptionRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(inceptionRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final InceptionRecordForm inceptionRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("inceptionRecordForm", inceptionRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "inceptionRecord/edit.do?inceptionRecordId=" + inceptionRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", ((ActorService) this.configurationService).findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final InceptionRecordForm inceptionRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(inceptionRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(final InceptionRecordForm inceptionRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/show");
		result.addObject("inceptionRecordForm", inceptionRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "inceptionRecord/show.do?inceptionRecordId=" + inceptionRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
