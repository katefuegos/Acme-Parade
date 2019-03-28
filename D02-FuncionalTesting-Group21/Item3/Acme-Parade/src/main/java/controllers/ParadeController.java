
package controllers;

import java.util.ArrayList;
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
import services.ParadeService;
import domain.Floaat;
import domain.Parade;
import forms.FloaatForm;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor---------------------------------------------------------

	public ParadeController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));
			final Collection<Parade> parades = this.paradeService.findByBrotherhoodIdAndNotDraftAndAccepted(brotherhoodId);
			result = new ModelAndView("parade/list");
			result.addObject("parades", parades);
			result.addObject("requestURI", "parade/list.do?brotherhoodId=" + brotherhoodId);
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/brotherhood/list.do");
			if (this.brotherhoodService.findOne(brotherhoodId) == null)
				redirectAttrs.addFlashAttribute("message", "parade.error.unexist");
		}

		return result;
	}
	// Show floats
	@RequestMapping(value = "/showFloat.do", method = RequestMethod.GET)
	public ModelAndView showFloats(final int paradeId, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Parade parade = null;
		try {
			parade = this.paradeService.findOne(paradeId);
			Assert.notNull(parade);

			Assert.isTrue(!this.paradeService.findOne(paradeId).isDraftMode());

			final Collection<FloaatForm> collection = new ArrayList<>();

			final Collection<Floaat> removeFloaats = parade.getFloats();

			for (final Floaat floaat : removeFloaats) {
				final FloaatForm floaatForm = new FloaatForm();

				floaatForm.setAdd(true);
				floaatForm.setParadeId(parade.getId());
				floaatForm.setId(floaat.getId());

				floaatForm.setTitle(floaat.getTitle());
				floaatForm.setDescription(floaat.getDescription());
				floaatForm.setPictures(floaat.getPictures());

				collection.add(floaatForm);
			}

			result = new ModelAndView("floaat/showByParade");
			result.addObject("parades", parade);
			result.addObject("floaatForm", collection);
			result.addObject("show", true);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/welcome/index.do");
			if (this.paradeService.findOne(paradeId) == null)
				redirectAttrs.addFlashAttribute("message", "parade.error.unexist");
			else if (parade.isDraftMode() == true)
				redirectAttrs.addFlashAttribute("message", "parade.error.DraftMode");
			else
				redirectAttrs.addFlashAttribute("message", "message.commit.error");
		}
		return result;
	}

}
