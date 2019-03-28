package controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.MemberService;
import services.SocialProfileService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.SocialProfile;

@Controller
@RequestMapping("/data")
public class DownloadPDFController {

	@Autowired
	BrotherhoodService brotherhoodService;

	@Autowired
	MemberService memberService;

	@Autowired
	AdministratorService administratorService;

	@Autowired
	ChapterService chapterService;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	ActorService actorService;

	@Autowired
	SocialProfileService socialProfileService;

	@Autowired
	UserAccountService userAccountService;

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView;

		modelAndView = new ModelAndView("misc/data");
		modelAndView.addObject("requestURI", "/data/list.do");
		modelAndView.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		modelAndView.addObject("systemName", this.configurationService
				.findAll().iterator().next().getSystemName());

		return modelAndView;
	}

	@RequestMapping(value = "/downloadPersonalData")
	public void downloadPersonalData(HttpServletResponse response)
			throws IOException {

		String csvFileName = "personalData.csv";
		response.setContentType("text/csv");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);

		UserAccount userAccount = LoginService.getPrincipal();
		if (userAccount.getAuthorities().toString().contains("BROTHERHOOD")) {

			Brotherhood b1 = brotherhoodService.findByUserAccountId(userAccount
					.getId());

			List<Brotherhood> listBrotherhood = Arrays.asList(b1);

			String[] header = { "name", "middleName", "surname", "photo",
					"email", "phone", "address", "title", "establishmentDate",
					"pictures" };

			csvWriter.writeHeader(header);

			for (Brotherhood brotherhood : listBrotherhood) {
				csvWriter.write(brotherhood, header);
			}

		} else if (userAccount.getAuthorities().toString().contains("MEMBER")) {

			Member m1 = memberService.findByUserAccountId(userAccount.getId());

			List<Member> listMember = Arrays.asList(m1);

			String[] header = { "name", "middleName", "surname", "photo",
					"email", "phone", "address" };

			csvWriter.writeHeader(header);

			for (Member member : listMember) {
				csvWriter.write(member, header);
			}

		} else if (userAccount.getAuthorities().toString().contains("CHAPTER")) {

			Chapter c1 = chapterService
					.findByUserAccountId(userAccount.getId());

			List<Chapter> listChapter = Arrays.asList(c1);

			String[] header = { "name", "middleName", "surname", "photo",
					"email", "phone", "address", "title" };

			csvWriter.writeHeader(header);

			for (Chapter chapter : listChapter) {
				csvWriter.write(chapter, header);
			}

		} else if (userAccount.getAuthorities().toString().contains("ADMIN")) {

			Administrator a1 = administratorService
					.findByUseraccount(userAccount);

			List<Administrator> listAdministrator = Arrays.asList(a1);

			String[] header = { "name", "middleName", "surname", "photo",
					"email", "phone", "address" };

			csvWriter.writeHeader(header);

			for (Administrator administrator : listAdministrator) {
				csvWriter.write(administrator, header);
			}
		}

		csvWriter.close();
	}

	@RequestMapping(value = "/downloadSocialProfiles")
	public void downloadSocialProfiles(HttpServletResponse response)
			throws IOException {

		String csvFileName = "socialProfiles.csv";
		response.setContentType("text/csv");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);

		UserAccount userAccount = LoginService.getPrincipal();

		Actor a1 = actorService.findByUserAccountId(userAccount.getId());

		Collection<SocialProfile> listSocialProfile = socialProfileService
				.findByActor(a1.getId());

		String[] header = { "nick", "nameSocialNetwork", "linkSocialNetwork" };

		csvWriter.writeHeader(header);

		for (SocialProfile socialProfile : listSocialProfile) {
			csvWriter.write(socialProfile, header);
		}

		csvWriter.close();
	}

	@RequestMapping(value = "/deletePersonalData", method = RequestMethod.GET)
	public ModelAndView deletePersonalData(
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		UserAccount userAccount = LoginService.getPrincipal();

		try {
			Assert.notNull(userAccount);
			if (userAccount.getAuthorities().toString().contains("BROTHERHOOD")) {
				Brotherhood b = brotherhoodService
						.findByUserAccountId(userAccount.getId());
				Assert.notNull(b);
				b.setAddress(null);
				b.setArea(null);
				b.setEmail("null@null.null");
				b.setEstablishmentDate(new Date(System.currentTimeMillis()));
				b.setIsBanned(true);
				b.setIsSpammer(true);
				b.setMiddleName(null);
				b.setName("null");
				b.setPhone(null);
				b.setPhoto(null);
				b.setPictures("http://null.null");
				b.setSurname("null");
				b.setTitle("null");
				userAccount.setEnabled(false);
				brotherhoodService.save(b);
				userAccountService.save(userAccount);
				Collection<SocialProfile> socialProfiles = socialProfileService
						.findByActor(b.getId());
				if (!socialProfiles.isEmpty()) {
					for (SocialProfile s : socialProfiles) {
						socialProfileService.delete(s);
					}
				}

			} else if (userAccount.getAuthorities().toString()
					.contains("MEMBER")) {
				Member m = memberService.findByUserAccountId(userAccount
						.getId());
				Assert.notNull(m);
				m.setAddress(null);
				m.setEmail("null@null.null");
				m.setIsBanned(true);
				m.setIsSpammer(true);
				m.setMiddleName(null);
				m.setName("null");
				m.setPhone(null);
				m.setPhoto(null);
				m.setSurname("null");
				userAccount.setEnabled(false);
				memberService.save(m);
				userAccountService.save(userAccount);
				Collection<SocialProfile> socialProfiles = socialProfileService
						.findByActor(m.getId());
				if (!socialProfiles.isEmpty()) {
					for (SocialProfile s : socialProfiles) {
						socialProfileService.delete(s);
					}
				}

			} else if (userAccount.getAuthorities().toString()
					.contains("CHAPTER")) {
				Chapter c = chapterService.findByUserAccountId(userAccount
						.getId());
				Assert.notNull(c);
				c.setAddress(null);
				c.setEmail("null@null.null");
				c.setIsBanned(true);
				c.setIsSpammer(true);
				c.setMiddleName(null);
				c.setName("null");
				c.setPhone(null);
				c.setPhoto(null);
				c.setSurname("null");
				c.setTitle("null");
				userAccount.setEnabled(false);
				chapterService.save(c);
				userAccountService.save(userAccount);
				Collection<SocialProfile> socialProfiles = socialProfileService
						.findByActor(c.getId());
				if (!socialProfiles.isEmpty()) {
					for (SocialProfile s : socialProfiles) {
						socialProfileService.delete(s);
					}
				}
			} else {
				Assert.isTrue(false);
			}

			result = new ModelAndView("redirect:/j_spring_security_logout");

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/data/list.do");

			if (LoginService.getPrincipal().getAuthorities().toString()
					.contains("ADMIN"))
				redirectAttrs.addFlashAttribute("message",
						"misc.error.adminNotDelete");
			else
				redirectAttrs.addFlashAttribute("message", "commit.error");
		}
		return result;
	}
}