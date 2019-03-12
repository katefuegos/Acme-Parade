/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AreaService;
import services.ConfigurationService;
import services.FinderService;
import services.MemberService;
import controllers.AbstractController;
import domain.Area;
import domain.Finder;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping("/finder/member")
public class FinderMemberController extends AbstractController {

	@Autowired
	private MemberService	memberService;

	@Autowired
	private FinderService	finderService;

	@Autowired
	private AreaService		areaService;
	
	@Autowired
	private ConfigurationService		configurationService;


	// Constructors -----------------------------------------------------------

	public FinderMemberController() {
		super();
	}

	// Update finder ---------------------------------------------------------------		

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updateFinder() {
		ModelAndView result;

		final Finder finder = this.finderService.findFinder();
		final Collection<Area> areas;

		areas = this.areaService.findAll();
		final String lang = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		final Collection<String> nameArea = new ArrayList<>();
		for (final Area area : areas)
			nameArea.add(area.getName());

		result = new ModelAndView("finder/member/update");
		result.addObject("finder", finder);
		result.addObject("areas", nameArea);
		result.addObject("lang", lang);
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());

		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateFinder(@Valid final Finder finder, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			final Collection<Area> areas = this.areaService.findAll();

			result = new ModelAndView("finder/member/update");
			result.addObject("finder", finder);
			result.addObject("areas", areas);

		} else
			try {
				this.finderService.save(finder);
				result = new ModelAndView("redirect:listParades.do");
			} catch (final Exception e) {
				final Collection<Area> areas = this.areaService.findAll();

				result = new ModelAndView("finder/member/update");
				result.addObject("finder", finder);
				result.addObject("areas", areas);
				result.addObject("message", "message.commit.error");
			}
		return result;
	}
	// List result finder ---------------------------------------------------------------		

	@RequestMapping("/listParades")
	public ModelAndView listParades() {
		ModelAndView result;
		Finder finder = this.finderService.findFinder();
		//Comprobar fecha ultima actualización
		finder = this.finderService.updateFinder(finder);
		//Obtener resultados fixuptasks de finder
		final Collection<Parade> parades = finder.getParades();
		final String lang = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		final Member member = this.memberService.findByUserAccountId(LoginService.getPrincipal().getId());
		result = new ModelAndView("finder/member/listParades");
		result.addObject("parades", parades);
		result.addObject("lang", lang);
		result.addObject("memberId", member.getId());
		result.addObject("requestURI", "finder/member/listParades.do");
		result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return result;
	}

}
