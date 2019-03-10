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
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Member;
import domain.Procession;
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
	private ProcessionService processionService;

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

			final Collection<Procession> processions = this.processionService
					.findAll();
			if (!requestsPending.isEmpty())
				for (final Request r : requestsPending)
					processions.remove(r.getProcession());
			if (!requestsRejected.isEmpty())
				for (final Request r : requestsRejected)
					processions.remove(r.getProcession());
			if (!requestsApproved.isEmpty())
				for (final Request r : requestsApproved)
					processions.remove(r.getProcession());

			result = new ModelAndView("request/listMember");
			result.addObject("requestsApproved", requestsApproved);
			result.addObject("requestsRejected", requestsRejected);
			result.addObject("requestsPending", requestsPending);
			result.addObject("processions", processions);
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
	public ModelAndView request(final int processionId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Procession procession = this.processionService
				.findOne(processionId);
		Member member = null;
		final Request request = this.requestService.create();
		final Collection<Procession> processions = new ArrayList<Procession>();
		try {
			Assert.notNull(procession);
			member = this.memberService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			Assert.notNull(member);
			final Collection<Request> requests = this.requestService
					.findRequestByMemberId(member.getId());

			if (!requests.isEmpty())
				for (final Request r : requests)
					processions.add(r.getProcession());
			Assert.isTrue(!processions.contains(procession));
			request.setProcession(procession);
			this.requestService.save(request);

			result = new ModelAndView("redirect:/request/member/listMember.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/request/member/listMember.do");
			if (procession == null)
				redirectAttrs.addFlashAttribute("message",
						"request.error.processionUnexists");
			else if (!request.getMember().equals(member)) {
				redirectAttrs.addFlashAttribute("message",
						"request.error.nobrotherhood");
			} else if (processions.contains(procession)) {
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
