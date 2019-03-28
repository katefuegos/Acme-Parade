package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ActorService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdministratorController extends AbstractController {

	// Service---------------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor-----------------------------------------------------

	public ActorAdministratorController() {
		super();
	}

	// List------------------------------------------------------------

	@RequestMapping(value = "/listBanneds", method = RequestMethod.GET)
	public ModelAndView listBanneds() {
		final ModelAndView modelAndView;

		final Collection<Actor> banneds = this.actorService
				.findPossibleBanned();

		modelAndView = this.listCalculateModelAndView(banneds, true);

		modelAndView.addObject("requestURI",
				"actor/administrator/listBanneds.do");
		modelAndView.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		modelAndView.addObject("systemName", this.configurationService
				.findAll().iterator().next().getSystemName());
		return modelAndView;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView();

		result = this.listCalculateModelAndView(this.actorService.findAll(),
				false);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	// Ban-------------------------------------------------------------

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int actorId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView = new ModelAndView();

		final Actor actor = this.actorService.findOne(actorId);
		try {
			Assert.notNull(actor);
			Assert.isTrue(actor.getIsBanned() == false);
			Assert.isTrue(actor.getIsSpammer() == true || actor.getPolarityScore() == -1.0);
			this.actorService.ban(actor);
			modelAndView = new ModelAndView("redirect:listBanneds.do");
		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:listBanneds.do");
			if (actor == null)
				redirectAttrs.addFlashAttribute("message",
						"actor.error.unexist");
			else if (actor.getIsBanned() == true) {
				redirectAttrs.addFlashAttribute("message",
						"actor.error.alreadyBanned");
			} else if (!((actor.getIsBanned() == false) && ((actor
					.getIsSpammer() == true) || (actor.getPolarityScore() <= -1.0))))
				redirectAttrs.addFlashAttribute("message", "actor.error.toBan");
		}

		return modelAndView;

	}

	// UnBan-------------------------------------------------------------

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final int actorId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView = null;

		final Actor actor = this.actorService.findOne(actorId);
		try {
			Assert.notNull(actor);
			Assert.isTrue(actor.getIsBanned() == true);
			this.actorService.unban(actor);
			modelAndView = new ModelAndView("redirect:listBanneds.do");

		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:listBanneds.do");

			if (actor == null)
				redirectAttrs.addFlashAttribute("message",
						"actor.error.unexist");
			else if (actor.getIsBanned() == true) {
				redirectAttrs.addFlashAttribute("message",
						"actor.error.notBanned");
			} else if (!((actor.getIsBanned() == true) && ((actor
					.getIsSpammer() == true) || (actor.getPolarityScore() <= -1.0))))
				redirectAttrs.addFlashAttribute("message",
						"actor.error.toUnban");
		}

		return modelAndView;

	}

	@RequestMapping(value = "/findSpammers", method = RequestMethod.GET)
	public ModelAndView find() {
		ModelAndView modelAndView;
		try {
			this.actorService.asignSpammers();
			modelAndView = new ModelAndView("redirect:listBanneds.do");
		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:listBanneds.do");
		}

		return modelAndView;
	}

	// Polarity

	@RequestMapping(value = "/calculatePolarity", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView modelAndView;
		try {

			this.actorService.updatePolarity();
			modelAndView = this.listCalculateModelAndView(
					this.actorService.findAll(), false, "actor.commit.ok");
		} catch (final Exception e) {
			modelAndView = this.listCalculateModelAndView(
					this.actorService.findAll(), false, "actor.commit.error");
		}

		return modelAndView;
	}

	protected ModelAndView listCalculateModelAndView(
			final Collection<Actor> actors, final boolean banneds) {
		ModelAndView result;

		result = this.listCalculateModelAndView(actors, banneds, null);

		return result;

	}

	protected ModelAndView listCalculateModelAndView(Collection<Actor> actors,
			final Boolean banneds, final String message) {
		ModelAndView result;

		if (banneds == true)
			result = new ModelAndView("administrator/banneds/list");
		else if (banneds == false)
			result = new ModelAndView("administrator/actor/list");
		else {
			actors = this.actorService.findAll();
			result = new ModelAndView("administrator/actor/list");
		}
		result.addObject("actors", actors);
		result.addObject("message", message);
		result.addObject("requestURI", "actor/administrator/list.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}
