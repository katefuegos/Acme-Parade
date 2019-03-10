/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
public class AbstractController {

	@Autowired
	private ConfigurationService	configurationService;


	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		SimpleDateFormat formatter;
		String moment;

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		if (!ClassUtils.getShortName(new TypeMismatchException("").getClass()).equals(ClassUtils.getShortName(oops.getClass()))) {
			result = new ModelAndView("misc/panic");
			result.addObject("name", ClassUtils.getShortName(oops.getClass()));
			result.addObject("exception", oops.getMessage());
			result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} else {

			result = new ModelAndView("welcome/index");
			result.getModel().put("message", "org.hibernate.validator.constraints.URL.message");
			result.addObject("moment", moment);
			result.addObject("welomeMessage", this.configurationService.findAll().iterator().next().getWelcomeMessage().get(LocaleContextHolder.getLocale().toString().toUpperCase()));
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		}
		return result;
	}

}
