
package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.ChapterService;
import services.ConfigurationService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Parade;

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

}
