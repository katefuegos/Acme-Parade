package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.ProcessionService;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ProcessionService processionService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public ProcessionController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Assert.notNull(brotherhoodService.findOne(brotherhoodId));
			final Collection<Procession> processions = processionService
					.findByBrotherhoodIdAndNotDraft(brotherhoodId);
			result = new ModelAndView("procession/list");
			result.addObject("processions", processions);
			result.addObject("requestURI", "procession/list.do?brotherhoodId="
					+ brotherhoodId);
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message",
						"procession.error.unexist");
		}

		return result;
	}
}
