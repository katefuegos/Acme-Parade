
package controllers.chapter;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.ChapterService;
import services.ConfigurationService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Parade;
import forms.RejectForm;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor---------------------------------------------------------

	public ParadeChapterController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final Integer chapterId = this.chapterService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();
			Assert.notNull(this.chapterService.findOne(chapterId));
			final Collection<Parade> parades = this.paradeService.findByChapterId(chapterId);
			result = new ModelAndView("parade/chapter/list");
			result.addObject("parades", parades);
			result.addObject("requestURI", "parade/chapter/list.do");

			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	// ACCEPT
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(final int paradeId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		final Chapter chapter = this.chapterService.findByUserAccountId(LoginService.getPrincipal().getId());
		try {
			Assert.notNull(chapter);
			Assert.isTrue(parade != null);
			Assert.isTrue(parade.getBrotherhood().getArea().getChapter().getId() == chapter.getId());
			final String status = parade.getStatus();

			final boolean sta = status.equals("SUBMITTED");
			Assert.isTrue(sta);

			parade.setStatus("ACCEPTED");

			this.paradeService.changeStatus(parade);

			result = new ModelAndView("redirect:/parade/chapter/list.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/parade/chapter/list.do");
			if (parade == null)
				redirectAttrs.addFlashAttribute("message", "parade.error.paradeUnexists");
			else if (!(parade.getBrotherhood().getArea().getChapter().getId() == chapter.getId()))
				redirectAttrs.addFlashAttribute("message", "parade.error.nobrotherhood");
			else if (parade.getStatus() != "SUBMITTED")
				redirectAttrs.addFlashAttribute("message", "parade.error.statusNoSubmitted");
			else
				redirectAttrs.addFlashAttribute("message", "message.commit.error");
		}
		return result;
	}

	// DECLINE
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(final int paradeId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		final Chapter chapter = this.chapterService.findByUserAccountId(LoginService.getPrincipal().getId());
		final RejectForm rejectForm = new RejectForm();
		try {
			Assert.notNull(chapter);
			Assert.isTrue(parade != null);
			Assert.isTrue(parade.getBrotherhood().getArea().getChapter().getId() == chapter.getId());

			final String status = parade.getStatus();

			final boolean sta = status.equals("SUBMITTED");
			Assert.isTrue(sta);

			rejectForm.setId(parade.getId());
			rejectForm.setVersion(parade.getVersion());

			result = this.declineModelAndView(rejectForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/parade/chapter/list.do");
			if (parade == null)
				redirectAttrs.addFlashAttribute("message", "parade.error.unexist");
			else if (!(parade.getBrotherhood().getArea().getChapter().getId() == chapter.getId()))
				redirectAttrs.addFlashAttribute("message", "parade.error.nobrotherhood");
			else if (parade.getStatus() != "SUBMITTED")
				result = this.declineModelAndView(rejectForm, "parade.error.statusNoSubmitted");
			else
				result = this.declineModelAndView(rejectForm, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView rejectSave(@Valid final forms.RejectForm rejectForm, final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result = null;
		final Chapter chapter = this.chapterService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.declineModelAndView(rejectForm, "commit.error");
		else
			try {
				final Parade parade = this.paradeService.findOne(rejectForm.getId());
				Assert.notNull(parade);

				Assert.notNull(chapter);
				Assert.isTrue(parade != null);
				Assert.isTrue(parade.getBrotherhood().getArea().getChapter().getId() == chapter.getId());
				Assert.isTrue(!(rejectForm.getReasonReject().equals("")));
				final String status = parade.getStatus();

				final boolean sta = status.equals("SUBMITTED");
				Assert.isTrue(sta);

				parade.setReasonReject(rejectForm.getReasonReject());

				parade.setStatus("REJECTED");
				this.paradeService.changeStatus(parade);

				result = new ModelAndView("redirect:/parade/chapter/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView("redirect:/parade/chapter/list.do");
				if (this.paradeService.findOne(rejectForm.getId()) == null)
					redirectAttrs.addFlashAttribute("message", "parade.error.unexist");
				else if (!(this.paradeService.findOne(rejectForm.getId()).getBrotherhood().getArea().getChapter().getId() == chapter.getId()))
					redirectAttrs.addFlashAttribute("message", "parade.error.nobrotherhood");
				else if (rejectForm.getReasonReject().equals(""))
					result = this.declineModelAndView(rejectForm, "parade.error.reasonRejectNeed");
				else
					result = this.declineModelAndView(rejectForm, "commit.error");
			}
		return result;
	}

	protected ModelAndView declineModelAndView(final RejectForm rejectForm) {
		ModelAndView result;
		result = this.declineModelAndView(rejectForm, null);
		return result;
	}

	protected ModelAndView declineModelAndView(final RejectForm rejectForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("parade/reject");
		result.addObject("message", message);
		result.addObject("requestURI", "parade/chapter/reject.do?paradeId=" + rejectForm.getId());
		result.addObject("rejectForm", rejectForm);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}
}
