package controllers.Brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Procession;
import domain.Request;
import forms.RequestForm;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private ProcessionService processionService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public RequestBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			int brotherhoodId = brotherhoodService.findByUserAccountId(
					LoginService.getPrincipal().getId()).getId();
			Assert.notNull(brotherhoodService.findOne(brotherhoodId));
			Collection<Procession> processions = processionService
					.findByBrotherhoodId(brotherhoodId);
			Collection<Request> requests = new ArrayList<Request>();
			if (!processions.isEmpty()) {
				for (Procession p : processions) {
					requests.addAll(requestService.findRequestByProcessionId(p
							.getId()));
				}
			}
			Collection<Request> requestsPending = new ArrayList<Request>();
			Collection<Request> requestsRejected = new ArrayList<Request>();
			Collection<Request> requestsApproved = new ArrayList<Request>();

			for (Request r : requests) {
				if (r.getStatus().equals("PENDING")) {
					requestsPending.add(r);
				} else if (r.getStatus().equals("APPROVED")) {
					requestsApproved.add(r);
				} else {
					requestsRejected.add(r);
				}
			}

			result = new ModelAndView("request/list");
			result.addObject("requestsApproved", requestsApproved);
			result.addObject("requestsRejected", requestsRejected);
			result.addObject("requestsPending", requestsPending);
			result.addObject("requestURI", "request/brotherhood/list.do");
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	// ACCEPT
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(final int requestId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Request request = requestService.findOne(requestId);
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(
				LoginService.getPrincipal().getId()).getId();
		RequestForm requestForm = new RequestForm();
		try {
			Assert.notNull(brotherhoodId);
			Assert.isTrue(request != null);
			Assert.isTrue(request.getProcession().getBrotherhood().getId() == brotherhoodId);
			Assert.isTrue(request.getStatus().equals("PENDING"));
			int roow = 1;
			int coluumn = 1;
			while (requestService.findRequestByPosition(roow, coluumn, request
					.getProcession().getId()) != null) {
				if (roow >= coluumn) {
					roow = roow + 1;
				} else {
					coluumn = coluumn + 1;
				}
			}
			requestForm.setId(request.getId());
			requestForm.setRoow(roow);
			requestForm.setColuumn(coluumn);
			requestForm.setReasonReject("notNeeded");
			result = this.acceptModelAndView(requestForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/request/brotherhood/list.do");
			if (request == null)
				redirectAttrs.addFlashAttribute("message",
						"request.error.unexist");
			else if (!(request.getProcession().getBrotherhood().getId() == brotherhoodId))
				redirectAttrs.addFlashAttribute("message",
						"request.error.nobrotherhood");
			else if (request.getStatus() != "PENDING")
				redirectAttrs.addFlashAttribute("message",
						"request.error.statusNoPendingAccept");
			else
				result = this.acceptModelAndView(requestForm, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.POST, params = "save")
	public ModelAndView acceptSave(@Valid final RequestForm requestForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result = null;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(
				LoginService.getPrincipal().getId()).getId();
		Request request = requestService.findOne(requestForm.getId());
		if (binding.hasErrors())
			result = this.acceptModelAndView(requestForm, "commit.error");
		else
			try {
				Assert.notNull(brotherhoodId);
				Assert.isTrue(requestForm != null);
				Assert.isTrue(requestService.findOne(requestForm.getId())
						.getProcession().getBrotherhood().getId() == brotherhoodId);
				Assert.isTrue(requestService.findRequestByPosition(
						requestForm.getRoow(), requestForm.getColuumn(),
						request.getProcession().getId()) == null);

				request.setColuumn(requestForm.getColuumn());
				request.setRoow(requestForm.getRoow());

				request.setStatus("APPROVED");
				this.requestService.save(request);

				result = new ModelAndView(
						"redirect:/request/brotherhood/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView(
						"redirect:/request/brotherhood/list.do");
				if (request == null)
					redirectAttrs.addFlashAttribute("message",
							"request.error.unexist");
				else if (!(requestService.findOne(requestForm.getId())
						.getProcession().getBrotherhood().getId() == brotherhoodId))
					redirectAttrs.addFlashAttribute("message",
							"request.error.nobrotherhood");
				else if (!(requestService.findRequestByPosition(
						requestForm.getRoow(), requestForm.getColuumn(),
						request.getProcession().getId()) == null))
					result = this.acceptModelAndView(requestForm,
							"request.error.positionTaken");
				else
					result = this.acceptModelAndView(requestForm,
							"commit.error");
			}
		return result;
	}

	// DECLINE
	@RequestMapping(value = "/decline", method = RequestMethod.GET)
	public ModelAndView decline(final int requestId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Request request = requestService.findOne(requestId);
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(
				LoginService.getPrincipal().getId()).getId();
		RequestForm requestForm = new RequestForm();
		try {
			Assert.notNull(brotherhoodId);
			Assert.isTrue(request != null);
			Assert.isTrue(request.getProcession().getBrotherhood().getId() == brotherhoodId);
			Assert.isTrue(request.getStatus().equals("PENDING"));
			requestForm.setId(request.getId());
			requestForm.setColuumn(1);
			requestForm.setRoow(1);

			result = this.declineModelAndView(requestForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/request/brotherhood/list.do");
			if (request == null)
				redirectAttrs.addFlashAttribute("message",
						"request.error.unexist");
			else if (!(request.getProcession().getBrotherhood().getId() == brotherhoodId))
				redirectAttrs.addFlashAttribute("message",
						"request.error.nobrotherhood");
			else if (request.getStatus() != "PENDING")
				result = this.declineModelAndView(requestForm,
						"request.error.reasonRejectNeed");
			else
				result = this.declineModelAndView(requestForm, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/decline", method = RequestMethod.POST, params = "save")
	public ModelAndView declineSave(@Valid final RequestForm requestForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result = null;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(
				LoginService.getPrincipal().getId()).getId();
		if (binding.hasErrors())
			result = this.declineModelAndView(requestForm, "commit.error");
		else
			try {
				Request request = requestService.findOne(requestForm.getId());
				Assert.notNull(request);
				request.setReasonReject(requestForm.getReasonReject());

				Assert.notNull(brotherhoodId);
				Assert.isTrue(request != null);
				Assert.isTrue(request.getProcession().getBrotherhood().getId() == brotherhoodId);
				Assert.isTrue(!request.getReasonReject().equals(""));
				request.setStatus("REJECTED");
				this.requestService.save(request);

				result = new ModelAndView(
						"redirect:/request/brotherhood/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView(
						"redirect:/request/brotherhood/list.do");
				if (requestService.findOne(requestForm.getId()) == null)
					redirectAttrs.addFlashAttribute("message",
							"request.error.unexist");
				else if (!(requestService.findOne(requestForm.getId())
						.getProcession().getBrotherhood().getId() == brotherhoodId))
					redirectAttrs.addFlashAttribute("message",
							"request.error.nobrotherhood");
				else if (requestForm.getReasonReject().equals(""))
					result = this.declineModelAndView(requestForm,
							"request.error.reasonRejectNeed");
				else
					result = this.declineModelAndView(requestForm,
							"commit.error");
			}
		return result;
	}

	// METHODS
	protected ModelAndView acceptModelAndView(final RequestForm requestForm) {
		ModelAndView result;
		result = this.acceptModelAndView(requestForm, null);
		return result;
	}

	protected ModelAndView acceptModelAndView(final RequestForm requestForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("request/accept");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"request/brotherhood/accept.do?requestId="
						+ requestForm.getId());
		result.addObject("requestForm", requestForm);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView declineModelAndView(final RequestForm requestForm) {
		ModelAndView result;
		result = this.declineModelAndView(requestForm, null);
		return result;
	}

	protected ModelAndView declineModelAndView(final RequestForm requestForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("request/decline");
		result.addObject("message", message);
		result.addObject(
				"requestURI",
				"request/brotherhood/decline.do?requestId="
						+ requestForm.getId());
		result.addObject("requestForm", requestForm);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}