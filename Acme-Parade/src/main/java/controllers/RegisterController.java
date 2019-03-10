
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.ActorService;
import services.AreaService;
import services.ConfigurationService;
import domain.Actor;
import forms.ActorForm;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

	// Services-----------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private AreaService		areaService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	//		Register Admin, Brotherhood and member
	@RequestMapping(value = "/actor", method = RequestMethod.GET)
	public ModelAndView createBrotherhoodAndMember(@RequestParam(required = false, defaultValue = "default") final String authority) {
		ModelAndView modelAndView;
		final ActorForm actorForm = new ActorForm();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();

		try {
			switch (authority) {
			case "BROTHERHOOD":
				//actor = this.actorService.create("BROTHERHOOD");
				a.setAuthority(Authority.BROTHERHOOD);
				actorForm.setAuth("BROTHERHOOD");
				break;
			case "MEMBER":
				//actor = this.actorService.create("MEMBER");
				a.setAuthority(Authority.MEMBER);
				actorForm.setAuth("MEMBER");
				actorForm.setTitle("---");
				actorForm.setPictures("http://www.pictures.com");
				actorForm.setArea(this.areaService.findAll().iterator().next());

				break;
			//			case "ADMIN":
			//				actor = this.actorService.create("ADMIN");
			//				break;
			default:
				throw new NullPointerException();
			}
			authorities.add(a);
			userAccount.setAuthorities(authorities);
			userAccount.setEnabled(true);
			actorForm.setUserAccount(userAccount);

			modelAndView = this.createEditModelAndView(actorForm);

		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:/welcome/index.do");
		}
		return modelAndView;
	}
	// Save
	@RequestMapping(value = "/actor", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				Assert.isTrue(actorForm.getCheckTerms(), "actor.check.true");
				if (actorForm.getAuth() == "BROTHERHOOD")
					Assert.notNull(actorForm.getArea(), "actor.area.notNull");
				final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				actorForm.getUserAccount().setPassword(encoder.encodePassword(actorForm.getUserAccount().getPassword(), null));
				this.actorService.update(actorForm);

				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(actorForm.getUserAccount().getUsername());

				if (test != null)
					result = this.createEditModelAndView(actorForm, "actor.userExists");
				else if (oops.getMessage() == "actor.check.true")
					result = this.createEditModelAndView(actorForm, oops.getMessage());
				else if (oops.getMessage() == "actor.area.notNull")
					result = this.createEditModelAndView(actorForm, oops.getMessage());
				else
					result = this.createEditModelAndView(actorForm, "message.commit.error");

			}
		return result;
	}
	// CreateModelAndView
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		ModelAndView result = null;

		// TODO faltan actores
		final Collection<Authority> authorities = actorForm.getUserAccount().getAuthorities();
		final Authority brotherhood = new Authority();
		brotherhood.setAuthority("BROTHERHOOD");
		final Authority member = new Authority();
		member.setAuthority("MEMBER");
		//		final Authority admin = new Authority();
		//		admin.setAuthority("ADMIN");

		if (authorities.contains(brotherhood))
			result = new ModelAndView("register/brotherhood");
		else if (authorities.contains(member))
			result = new ModelAndView("register/member");
		//		else if (authorities.contains(admin))
		//			result = new ModelAndView("register/admin");
		else
			throw new NullPointerException();

		result.addObject("actorForm", actorForm);
		result.addObject("areas", this.areaService.findAll());
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
