
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import security.UserAccount;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import domain.History;

@Controller
@RequestMapping("/history/")
public class HistoryController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor---------------------------------------------------------

	public HistoryController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			final Collection<History> historys = this.historyService.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("history/list");
			result.addObject("historys", historys);
			result.addObject("requestURI", "history/list.do?brotherhoodId=" + brotherhoodId);
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (this.brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message", "history.error.unexist");
		}

		return result;
	}

	@RequestMapping(value = "/listOwn", method = RequestMethod.GET)
	public ModelAndView listOwn(final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final UserAccount uA = LoginService.getPrincipal();
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(uA.getId()).getId();
		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			final Collection<History> historys = this.historyService.findByBrotherhoodId(brotherhoodId);
			result = new ModelAndView("history/list");
			result.addObject("historys", historys);
			result.addObject("requestURI", "history/listOwn.do");
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (this.brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message", "history.error.unexist");
		}
		return result;
	}
}
