
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
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Actor;
import domain.LinkRecord;
import forms.LinkRecordForm;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private ActorService		actorService;


	//Constructor--------------------------------------------------------------

	public LinkRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<LinkRecord> linkRecords = new ArrayList<>(this.linkRecordService.findLinkRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("linkRecord/list");
		modelAndView.addObject("linkRecords", linkRecords);

		modelAndView.addObject("requestURI", "/linkRecord/brotherhood/list.do");
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final LinkRecordForm linkRecordForm = new LinkRecordForm();
		linkRecordForm.setId(0);

		result = this.createModelAndView(linkRecordForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int linkRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
		final LinkRecordForm linkRecordForm = new LinkRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(linkRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == linkRecord.getHistory().getBrotherhood().getId());
			linkRecordForm.setId(linkRecordId);
			linkRecordForm.setTitle(linkRecord.getTitle());
			linkRecordForm.setDescription(linkRecord.getDescription());
			result = this.showModelAndView(linkRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/linkRecord/brotherhood/list.do");
			if (linkRecord == null)
				redirectAttrs.addFlashAttribute("message", "linkRecord.error.unexist");
			else if (actor.getId() != linkRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "linkRecord.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
		final LinkRecordForm linkRecordForm = new LinkRecordForm();
		Actor actor = null;
		try {
			Assert.notNull(linkRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == linkRecord.getHistory().getBrotherhood().getId());
			linkRecordForm.setId(linkRecordId);
			linkRecordForm.setTitle(linkRecord.getTitle());
			linkRecordForm.setDescription(linkRecord.getDescription());
			result = this.editModelAndView(linkRecordForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/linkRecord/brotherhood/list.do?historyId=" + linkRecord.getHistory().getId());
			if (linkRecord == null)
				redirectAttrs.addFlashAttribute("message", "linkRecord.error.unexist");
			else if (actor.getId() != linkRecord.getHistory().getBrotherhood().getId())
				redirectAttrs.addFlashAttribute("message", "linkRecord.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecordForm linkRecordForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor = new Actor();
		LinkRecord linkRecord;
		if (linkRecordForm.getId() != 0)
			linkRecord = this.linkRecordService.findOne(linkRecordForm.getId());
		else
			linkRecord = new LinkRecord();
		if (binding.hasErrors())
			result = this.editModelAndView(linkRecordForm);
		else
			try {
				System.out.println("owo" + linkRecord);
				Assert.notNull(linkRecord);
				System.out.println("uwu" + linkRecord);
				actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
				//Assert.isTrue(actor.getId() == linkRecord.getHistory().getBrotherhood().getId());
				System.out.println("iwi" + linkRecord);
				linkRecord.setTitle(linkRecordForm.getTitle());
				linkRecord.setDescription(linkRecordForm.getDescription());
				this.linkRecordService.save(linkRecord);

				result = new ModelAndView("redirect:/linkRecord/brotherhood/list.do?historyId=" + linkRecord.getHistory().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(linkRecordForm, "linkRecord.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final LinkRecordForm linkRecordForm, final BindingResult binding) {

		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(linkRecordForm);
		else
			try {
				linkRecord.setTitle(linkRecordForm.getTitle());
				linkRecord.setDescription(linkRecordForm.getDescription());
				this.linkRecordService.save(linkRecord);
				result = new ModelAndView("redirect:/linkRecord/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(linkRecordForm, "linkRecord.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecordForm linkRecordForm, final BindingResult binding) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordForm.getId());
		Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		try {
			Assert.notNull(linkRecord);
			actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(actor.getId() == linkRecord.getHistory().getBrotherhood().getId());
			this.linkRecordService.delete(linkRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(linkRecordForm, "linkRecord.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.createModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/create");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "linkRecord/create.do");
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.editModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "linkRecord/edit.do?linkRecordId=" + linkRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", ((ActorService) this.configurationService).findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(final LinkRecordForm linkRecordForm) {
		ModelAndView result;

		result = this.showModelAndView(linkRecordForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(final LinkRecordForm linkRecordForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/show");
		result.addObject("linkRecordForm", linkRecordForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "linkRecord/show.do?linkRecordId=" + linkRecordForm.getId());
		//result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		//result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
