package controllers.member;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.ConfigurationService;
import services.MemberService;
import services.ParadeService;
import services.RequestService;
import controllers.AbstractController;
import domain.Member;
import domain.Parade;
import domain.Request;

@Controller
@RequestMapping("/request/member")
public class requestMemberController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private RequestService requestService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public requestMemberController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/listMember", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final int memberId = this.memberService.findByUserAccountId(
					LoginService.getPrincipal().getId()).getId();
			Assert.notNull(this.memberService.findOne(memberId));

			final Collection<Request> requests = this.requestService
					.findRequestByMemberId(memberId);

			final Collection<Request> requestsPending = new ArrayList<Request>();
			final Collection<Request> requestsRejected = new ArrayList<Request>();
			final Collection<Request> requestsApproved = new ArrayList<Request>();

			if (!requests.isEmpty())
				for (final Request r : requests)
					if (r.getStatus().equals("PENDING"))
						requestsPending.add(r);
					else if (r.getStatus().equals("APPROVED"))
						requestsApproved.add(r);
					else
						requestsRejected.add(r);

			final Collection<Parade> parades = this.paradeService
					.findAccepted();
			if (!requestsPending.isEmpty())
				for (final Request r : requestsPending)
					parades.remove(r.getParade());
			if (!requestsRejected.isEmpty())
				for (final Request r : requestsRejected)
					parades.remove(r.getParade());
			if (!requestsApproved.isEmpty())
				for (final Request r : requestsApproved)
					parades.remove(r.getParade());

			result = new ModelAndView("request/listMember");
			result.addObject("requestsApproved", requestsApproved);
			result.addObject("requestsRejected", requestsRejected);
			result.addObject("requestsPending", requestsPending);
			result.addObject("parades", parades);
			result.addObject("requestURI", "request/member/listMember.do");
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/request/member/listMember.do");
		}

		return result;
	}

	// REQUEST
	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public ModelAndView request(final int paradeId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Parade parade = this.paradeService
				.findOne(paradeId);
		Member member = null;
		final Request request = this.requestService.create();
		final Collection<Parade> parades = new ArrayList<Parade>();
		try {
			Assert.notNull(parade);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			Assert.isTrue(parade.getStatus().equals("ACCEPTED"));
			final Collection<Request> requests = this.requestService
					.findRequestByMemberId(member.getId());

			if (!requests.isEmpty())
				for (final Request r : requests)
					parades.add(r.getParade());
			Assert.isTrue(!parades.contains(parade));
			request.setParade(parade);
			this.requestService.save(request);

			result = new ModelAndView("redirect:/request/member/listMember.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/request/member/listMember.do");
			if (parade == null)
				redirectAttrs.addFlashAttribute("message",
						"request.error.paradeUnexists");
			else if(!parade.getStatus().equals("ACCEPTED"))
				redirectAttrs.addFlashAttribute("message",
						"request.error.notAccepted");
			else if (!request.getMember().equals(member)) {
				redirectAttrs.addFlashAttribute("message",
						"request.error.nobrotherhood");
			} else if (parades.contains(parade)) {
				redirectAttrs.addFlashAttribute("message",
						"request.error.alreadyRequest");
			} else
				result = new ModelAndView(
						"redirect:/request/member/listMember.do");
		}
		return result;
	}

	// REQUEST
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int requestId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Request request = this.requestService.findOne(requestId);
		Member member = null;
		try {
			Assert.notNull(request);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			Assert.isTrue(request.getMember().getId() == member.getId());
			Assert.isTrue(request.getStatus().equals("PENDING"));
			this.requestService.delete(request);

			result = new ModelAndView("redirect:/request/member/listMember.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/request/member/listMember.do");
			if (request == null)
				redirectAttrs.addFlashAttribute("message",
						"request.error.unexist");
			else if (!request.getMember().equals(member)) {
				redirectAttrs.addFlashAttribute("message",
						"request.error.nobrotherhood");
			} else
				result = new ModelAndView(
						"redirect:/request/member/listMember.do");
		}
		return result;
	}
}
