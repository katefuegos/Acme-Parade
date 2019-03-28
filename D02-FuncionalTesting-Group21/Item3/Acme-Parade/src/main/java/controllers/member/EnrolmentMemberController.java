package controllers.member;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class EnrolmentMemberController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ConfigurationService configurationService;
	
	// Constructor---------------------------------------------------------

	public EnrolmentMemberController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			Member m = memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(m);

			final Collection<Enrolment> enrolmentsAccepted = this.enrolmentService
					.findByMemberIdAccepted(m.getId());
			final Collection<Enrolment> enrolmentsPending = this.enrolmentService
					.findByMemberIdPending(m.getId());
			final Collection<Enrolment> enrolmentsDropedOut = this.enrolmentService
					.findByMemberIdDropOut(m.getId());

			Collection<Brotherhood> brotherhoods = brotherhoodService.findAll();
			if (!enrolmentsAccepted.isEmpty()) {
				for (Enrolment e : enrolmentsAccepted) {
					brotherhoods.remove(e.getBrotherhood());
				}
			}
			if (!enrolmentsPending.isEmpty()) {
				for (Enrolment e : enrolmentsPending) {
					brotherhoods.remove(e.getBrotherhood());
				}
			}
			if (!enrolmentsDropedOut.isEmpty()) {
				for (Enrolment e : enrolmentsDropedOut) {
					brotherhoods.remove(e.getBrotherhood());
				}
			}

			final String lang = LocaleContextHolder.getLocale().getLanguage()
					.toUpperCase();
			result = new ModelAndView("enrolment/list2");
			result.addObject("lang", lang);
			result.addObject("enrolmentsAccepted", enrolmentsAccepted);
			result.addObject("enrolmentsPending", enrolmentsPending);
			result.addObject("enrolmentsDropedOut", enrolmentsDropedOut);
			result.addObject("brotherhoods", brotherhoods);
			result.addObject("requestURI", "enrolment/member/list.do");
			result.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/");
		}
		
		return result;
	}

	// DROP OUT
	@RequestMapping(value = "/dropout", method = RequestMethod.GET)
	public ModelAndView dropout(final int enrolmentId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		Member member = null;
		try {
			Assert.notNull(enrolment);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			Assert.isTrue(member.getEnrolments().contains(enrolment));
			Assert.isTrue(enrolment.isAccepted());
			enrolment.setMomentEnrol(null);
			enrolment.setPosition(null);
			enrolment.setAccepted(false);
			enrolment.setMomentDropOut(new Date(
					System.currentTimeMillis() - 1000));
			this.enrolmentService.save(enrolment);

			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/enrolment/member/list.do");
			if (enrolment == null)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.processionUnexists");
			else if (!member.getEnrolments().contains(enrolment)) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.noMember");
			} else if (enrolment.isAccepted() == false) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.notAcepted");
			} else
				result = new ModelAndView("redirect:/enrolment/member/list.do");
		}

		return result;
	}

	// DROP OUT
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(final int enrolmentId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		Member member = null;
		try {
			Assert.notNull(enrolment);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			Assert.isTrue(member.getEnrolments().contains(enrolment));
			Assert.isTrue(!enrolment.isAccepted());
			Assert.isTrue(enrolment.getMomentDropOut() == null);
			member.getEnrolments().remove(enrolment);
			memberService.save(member);
			this.enrolmentService.delete(enrolment);

			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/enrolment/member/list.do");
			if (enrolment == null)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.processionUnexists");
			else if (!member.getEnrolments().contains(enrolment)) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.noMember");
			} else if (enrolment.isAccepted() == true) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.acepted");
			} else if (!(enrolment.getMomentDropOut() == null)) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.notPending");
			} else
				result = new ModelAndView("redirect:/enrolment/member/list.do");
		}
		return result;
	}

	// DROP OUT
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(final int brotherhoodId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Enrolment enrolment = this.enrolmentService.createFromMember();
		Member member = null;
		Brotherhood brotherhood = brotherhoodService.findOne(brotherhoodId);
		try {
			Assert.notNull(brotherhoodId);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			Assert.isTrue(enrolmentService.findByMemberIdAndBrotherhoodId(
					member.getId(), brotherhoodId) == null);
			enrolment.setBrotherhood(brotherhood);
			enrolment = this.enrolmentService.save(enrolment);
			member.getEnrolments().add(enrolment);
			memberService.save(member);

			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/enrolment/member/list.do");
			if (brotherhood == null)
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.brotherhoodUnexists");
			else if (enrolmentService.findByMemberIdAndBrotherhoodId(
					member.getId(), brotherhoodId) != null) {
				redirectAttrs.addFlashAttribute("message",
						"enrolment.error.joinedYet");
			} else
				result = new ModelAndView("redirect:/enrolment/member/list.do");
		}
		return result;
	}
}