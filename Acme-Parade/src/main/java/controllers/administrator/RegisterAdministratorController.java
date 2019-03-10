package controllers.administrator;

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
import controllers.AbstractController;
import domain.Actor;
import forms.ActorForm;

@Controller
@RequestMapping("/register/administrator")
public class RegisterAdministratorController extends AbstractController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ConfigurationService configurationService;

	// Register handyWorker
	@RequestMapping(value = "/newActor", method = RequestMethod.GET)
	public ModelAndView createHandyWorker(
			@RequestParam(required = false, defaultValue = "default") final String authority) {
		ModelAndView modelAndView;
		final ActorForm actorForm = new ActorForm();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority a = new Authority();

		try {
			// Faltan actores
			switch (authority) {
			case "ADMIN":
				// actor = this.actorService.create("ADMIN");
				a.setAuthority(Authority.ADMIN);
				actorForm.setAuth("ADMIN");
				actorForm.setTitle("---");
				actorForm.setPictures("http://www.pictures.com");
				actorForm.setArea(this.areaService.findAll().iterator().next());

				break;
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
	@RequestMapping(value = "/newActor", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm,
			final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				Assert.isTrue(actorForm.getCheckTerms(), "actor.check.true");
				final UserAccount userAccount = actorForm.getUserAccount();

				final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				userAccount.setPassword(encoder.encodePassword(
						userAccount.getPassword(), null));
				userAccount.setEnabled(true);
				actorForm.setUserAccount(userAccount);
				this.actorService.update(actorForm);

				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService
						.findActorByUsername(actorForm.getUserAccount()
								.getUsername());
				if (test != null)
					result = this.createEditModelAndView(actorForm,
							"actor.userExists");
				else if (oops.getMessage() == "actor.check.true")
					result = this.createEditModelAndView(actorForm,
							oops.getMessage());
				else
					result = this.createEditModelAndView(actorForm,
							"message.commit.error");
			}
		return result;
	}

	// CreateModelAndView
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm,
			final String message) {
		ModelAndView result = null;

		// TODO faltan actores
		final Collection<Authority> authorities = actorForm.getUserAccount()
				.getAuthorities();

		final Authority admin = new Authority();
		admin.setAuthority("ADMIN");

		if (authorities.contains(admin))
			result = new ModelAndView("administrator/admin");
		else
			throw new NullPointerException();

		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}
