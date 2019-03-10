/*
 * ProfileController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import security.Authority;
import security.LoginService;
import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.MemberService;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import forms.ActorForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MemberService memberService;

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final ActorForm actorForm = new ActorForm();

		final Authority member = new Authority();
		member.setAuthority(Authority.MEMBER);
		final Authority brotherhood = new Authority();
		brotherhood.setAuthority(Authority.BROTHERHOOD);
		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);

		actorForm.setTitle("---");
		actorForm.setPictures("http://www.pictures.com");
		actorForm.setArea(this.areaService.findAll().iterator().next());

		try {
			final Actor a = this.actorService.findByUserAccount(LoginService
					.getPrincipal());
			Assert.notNull(a);

			if (a.getUserAccount().getAuthorities().contains(member))
				actorForm.setAuth("MEMBER");
			else if (a.getUserAccount().getAuthorities().contains(brotherhood)) {
				actorForm.setAuth("BROTHERHOOD");
				final Brotherhood bro = this.brotherhoodService
						.findByUserAccountId(a.getUserAccount().getId());
				actorForm.setTitle(bro.getTitle());
				actorForm.setPictures(bro.getPictures());
				actorForm.setArea(bro.getArea());

			} else if (a.getUserAccount().getAuthorities().contains(admin))
				actorForm.setAuth("ADMIN");

			else
				throw new NullPointerException();

			actorForm.setUserAccount(a.getUserAccount());
			actorForm.setId(a.getId());
			actorForm.setVersion(a.getVersion());
			actorForm.setName(a.getName());
			actorForm.setMiddleName(a.getMiddleName());
			actorForm.setSurname(a.getSurname());
			actorForm.setPhoto(a.getPhoto());
			actorForm.setEmail(a.getEmail());
			actorForm.setPhone(a.getPhone());
			actorForm.setAddress(a.getAddress());

			result = this.createEditModelAndView(actorForm);

		} catch (final Exception e) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	//
	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm,
			final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {

				this.actorService.update(actorForm);
				result = this.createEditModelAndView(actorForm,
						"actor.commit.ok");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actorForm,
						"actor.commit.error");

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
		ModelAndView result;
		final Authority brotherhood = new Authority();
		brotherhood.setAuthority(Authority.BROTHERHOOD);
		if (actorForm.getUserAccount().getAuthorities().contains(brotherhood)) {
			actorForm.setAuth("BROTHERHOOD");
			final Brotherhood bro = this.brotherhoodService
					.findByUserAccountId(actorForm.getUserAccount().getId());
			actorForm.setTitle(bro.getTitle());
			actorForm.setPictures(bro.getPictures());
			actorForm.setArea(bro.getArea());
		}

		result = new ModelAndView("actor/edit");
		result.addObject("actorForm", actorForm);

		result.addObject("areas", this.areaService.findAll());
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI", "actor/edit.do");
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int actorId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView = new ModelAndView("actor/edit");

		final Actor actor = this.actorService.findOne(actorId);
		final Brotherhood brother = this.brotherhoodService.findOne(actorId);

		try {
			Assert.notNull(actor);
			final ActorForm actorForm = new ActorForm();
			String title = null;
			Date estDate = null;
			if (brother != null) {
				title = brother.getTitle();
				estDate = brother.getEstablishmentDate();

				actorForm.setTitle(title);
				actorForm.setPictures(brother.getPictures());
				actorForm.setArea(brother.getArea());

			}

			actorForm.setUserAccount(actor.getUserAccount());
			actorForm.setName(actor.getName());
			actorForm.setMiddleName(actor.getMiddleName());
			actorForm.setSurname(actor.getSurname());
			actorForm.setPhoto(actor.getPhoto());
			actorForm.setEmail(actor.getEmail());
			actorForm.setPhone(actor.getPhone());
			actorForm.setAddress(actor.getAddress());

			modelAndView.addObject("actor", actor);
			modelAndView.addObject("isRead", true);
			modelAndView.addObject("establishmentDate", estDate);
			modelAndView.addObject("title", title);
			modelAndView.addObject("requestURI",
					"/actor/administrator/show.do?actorId=" + actorId);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());

		} catch (final Throwable e) {
			modelAndView = new ModelAndView("redirect:/welcome/index.do");
			if (actor == null)
				redirectAttrs.addFlashAttribute("message1",
						"actor.error.unexist");
		}
		return modelAndView;

	}

	@RequestMapping(value = "/showMember", method = RequestMethod.GET)
	public ModelAndView showMember(@RequestParam final int actorId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView = new ModelAndView("actor/showMember");
		Brotherhood b = null;
		final Member m = this.memberService.findOne(actorId);
		Collection<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();

		try {
			Assert.notNull(m);
			b = brotherhoodService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(b);
			Collection<Enrolment> enrolments = m.getEnrolments();
			if (!enrolments.isEmpty()) {
				for (Enrolment e : enrolments) {
					brotherhoods.add(e.getBrotherhood());
				}
			}

			Assert.isTrue(brotherhoods.contains(b));
			final ActorForm actorForm = new ActorForm();

			actorForm.setUserAccount(m.getUserAccount());
			actorForm.setName(m.getName());
			actorForm.setMiddleName(m.getMiddleName());
			actorForm.setSurname(m.getSurname());
			actorForm.setPhoto(m.getPhoto());
			actorForm.setEmail(m.getEmail());
			actorForm.setPhone(m.getPhone());
			actorForm.setAddress(m.getAddress());

			modelAndView.addObject("actorForm", actorForm);
			modelAndView.addObject("isRead", true);
			modelAndView.addObject("banner", this.configurationService
					.findAll().iterator().next().getBanner());
			modelAndView.addObject("systemName", this.configurationService
					.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			modelAndView = new ModelAndView("redirect:/welcome/index.do");
			if (m == null)
				redirectAttrs.addFlashAttribute("message1",
						"actor.error.unexist");
			else if (!brotherhoods.contains(b)) {
				redirectAttrs.addFlashAttribute("message1",
						"actor.error.notFromBrotherhood");
			}
		}
		return modelAndView;

	}
}
