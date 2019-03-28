package controllers;

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
import services.ConfigurationService;
import services.SocialProfileService;
import domain.Actor;
import domain.SocialProfile;
import forms.SocialProfileForm;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService socialProfileService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ConfigurationService configurationService;

	// List------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;

		final Actor a = this.actorService.findByUserAccount(LoginService
				.getPrincipal());
		final Collection<SocialProfile> socialProfiles = this.socialProfileService
				.findByActor(a.getId());

		modelAndView = new ModelAndView("socialProfile/list");
		modelAndView.addObject("socialProfiles", socialProfiles);
		modelAndView.addObject("requestURI", "/socialProfile/list.do");
		modelAndView.addObject("username", a.getUserAccount().getUsername());
		modelAndView.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		modelAndView.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return modelAndView;

	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfileForm socialProfileForm = new SocialProfileForm();
		socialProfileForm.setId(0);

		result = this.createModelAndView(socialProfileForm);
		return result;
	}

	// Show------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int socialProfileId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		SocialProfile socialProfile = socialProfileService
				.findOne(socialProfileId);
		SocialProfileForm socialProfileForm = new SocialProfileForm();
		Actor actor = null;
		try {
			Assert.notNull(socialProfile);
			actor = this.actorService.findByUserAccount(LoginService
					.getPrincipal());
			Assert.isTrue(actor.getId() == socialProfile.getActor().getId());
			socialProfileForm.setId(socialProfileId);
			socialProfileForm.setLinkSocialNetwork(socialProfile
					.getLinkSocialNetwork());
			socialProfileForm.setNameSocialNetwork(socialProfile
					.getNameSocialNetwork());
			socialProfileForm.setNick(socialProfile.getNick());
			result = this.showModelAndView(socialProfileForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/socialProfile/list.do");
			if (socialProfile == null)
				redirectAttrs.addFlashAttribute("message",
						"socialProfile.error.unexist");
			else if (actor.getId() != socialProfile.getActor().getId())
				redirectAttrs.addFlashAttribute("message",
						"socialProfile.error.notFromActor");
		}
		return result;

	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialProfileId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		SocialProfile socialProfile = socialProfileService
				.findOne(socialProfileId);
		SocialProfileForm socialProfileForm = new SocialProfileForm();
		Actor actor = null;
		try {
			Assert.notNull(socialProfile);
			actor = this.actorService.findByUserAccount(LoginService
					.getPrincipal());
			Assert.isTrue(actor.getId() == socialProfile.getActor().getId());
			socialProfileForm.setId(socialProfileId);
			socialProfileForm.setLinkSocialNetwork(socialProfile
					.getLinkSocialNetwork());
			socialProfileForm.setNameSocialNetwork(socialProfile
					.getNameSocialNetwork());
			socialProfileForm.setNick(socialProfile.getNick());
			result = this.editModelAndView(socialProfileForm);
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/socialProfile/list.do");
			if (socialProfile == null)
				redirectAttrs.addFlashAttribute("message",
						"socialProfile.error.unexist");
			else if (actor.getId() != socialProfile.getActor().getId())
				redirectAttrs.addFlashAttribute("message",
						"socialProfile.error.notFromActor");
		}
		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid SocialProfileForm socialProfileForm,
			final BindingResult binding) {

		ModelAndView result;
		SocialProfile socialProfile = socialProfileService
				.findOne(socialProfileForm.getId());
		Actor actor = null;
		if (binding.hasErrors())
			result = this.editModelAndView(socialProfileForm);
		else
			try {
				Assert.notNull(socialProfile);
				actor = this.actorService.findByUserAccount(LoginService
						.getPrincipal());
				Assert.isTrue(actor.getId() == socialProfile.getActor().getId());
				socialProfile.setLinkSocialNetwork(socialProfileForm
						.getLinkSocialNetwork());
				socialProfile.setNameSocialNetwork(socialProfileForm
						.getNameSocialNetwork());
				socialProfile.setNick(socialProfileForm.getNick());
				this.socialProfileService.save(socialProfile);
				result = new ModelAndView("redirect:/socialProfile/list.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(socialProfileForm,
						"socialProfile.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final SocialProfileForm socialProfileForm,
			final BindingResult binding) {

		ModelAndView result;
		SocialProfile socialProfile = socialProfileService.create();
		if (binding.hasErrors())
			result = this.createModelAndView(socialProfileForm);
		else
			try {
				Actor actor = actorService.findByUserAccount(LoginService
						.getPrincipal());
				Assert.notNull(actor);
				socialProfile.setActor(actor);
				socialProfile.setLinkSocialNetwork(socialProfileForm
						.getLinkSocialNetwork());
				socialProfile.setNameSocialNetwork(socialProfileForm
						.getNameSocialNetwork());
				socialProfile.setNick(socialProfileForm.getNick());
				this.socialProfileService.save(socialProfile);
				result = new ModelAndView("redirect:/socialProfile/list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(socialProfileForm,
						"socialProfile.commit.error");
			}
		return result;
	}

	// delete

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SocialProfileForm socialProfileForm,
			final BindingResult binding) {
		ModelAndView result;
		SocialProfile socialProfile = socialProfileService
				.findOne(socialProfileForm.getId());
		Actor actor = actorService.findByUserAccount(LoginService
				.getPrincipal());
		try {
			Assert.notNull(socialProfile);
			actor = this.actorService.findByUserAccount(LoginService
					.getPrincipal());
			Assert.isTrue(actor.getId() == socialProfile.getActor().getId());
			this.socialProfileService.delete(socialProfile);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(socialProfileForm,
					"socialProfile.commit.error");
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createModelAndView(
			final SocialProfileForm socialProfileForm) {
		ModelAndView result;

		result = this.createModelAndView(socialProfileForm, null);

		return result;

	}

	protected ModelAndView createModelAndView(
			final SocialProfileForm socialProfileForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("socialProfile/create");
		result.addObject("socialProfileForm", socialProfileForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "socialProfile/create.do");
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(
			final SocialProfileForm socialProfileForm) {
		ModelAndView result;

		result = this.editModelAndView(socialProfileForm, null);

		return result;

	}

	protected ModelAndView editModelAndView(
			final SocialProfileForm socialProfileForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfileForm", socialProfileForm);
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "socialProfile/edit.do?socialProfileId="
				+ socialProfileForm.getId());
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView showModelAndView(
			final SocialProfileForm socialProfileForm) {
		ModelAndView result;

		result = this.showModelAndView(socialProfileForm, null);

		return result;

	}

	protected ModelAndView showModelAndView(
			final SocialProfileForm socialProfileForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("socialProfile/show");
		result.addObject("socialProfileForm", socialProfileForm);
		result.addObject("message", message);
		result.addObject("isRead", true);

		result.addObject("requestURI", "socialProfile/show.do?socialProfileId="
				+ socialProfileForm.getId());
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
