package controllers.Brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private HistoryService historyService;

	@Autowired
	private BrotherhoodService brotherhoodService;

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

		modelAndView = new ModelAndView("history/list");
		modelAndView.addObject("historys", historys);
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
	public ModelAndView delete(@RequestParam final int historyId) {
		ModelAndView result = null;
		try {
			final History history = this.historyService.findOne(historyId);
			this.historyService.delete(history);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
		}
		return result;
	}

}
