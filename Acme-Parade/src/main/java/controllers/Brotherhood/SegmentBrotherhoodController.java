package controllers.Brotherhood;

import java.util.Collection;
import java.util.Iterator;

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
import services.PathService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Path;
import domain.Segment;
import forms.SegmentForm;

@Controller
@RequestMapping("/segment/brotherhood")
public class SegmentBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private PathService pathService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private ConfigurationService configurationService;

	// Constructor---------------------------------------------------------

	public SegmentBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int pathId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Path path = pathService.findOne(pathId);
		try {
			Assert.notNull(path);
			final Integer brotherhoodId = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId())
					.getId();
			Brotherhood b = brotherhoodService.findOne(brotherhoodId);
			Assert.notNull(b);
			Assert.isTrue(path.getParade().getBrotherhood().equals(b));
			final Collection<Segment> segments = this.segmentService
					.findByPathId(pathId);

			Boolean isEmpty = false;
			if (segments.isEmpty()) {
				isEmpty = true;
			}
			Boolean draft = path.getParade().isDraftMode();
			result = new ModelAndView("segment/list");
			result.addObject("requestURI",
					"segment/brotherhood/list.do?pathId=" + pathId);
			result.addObject("segments", segments);
			result.addObject("pathId", pathId);
			result.addObject("draft", draft);
			result.addObject("isEmpty", isEmpty);
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/path/brotherhood/list.do");
			if (path == null) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotExists");
			} else if (!path
					.getParade()
					.getBrotherhood()
					.equals(brotherhoodService.findOne(this.brotherhoodService
							.findByUserAccountId(
									LoginService.getPrincipal().getId())
							.getId()))) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotYours");
			} else {
				redirectAttrs.addFlashAttribute("message", "commit.error");
			}
		}
		return result;
	}

	// DELETE LAST
	@RequestMapping(value = "/deleteLast", method = RequestMethod.GET)
	public ModelAndView deleteLast(final int pathId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Path path = this.pathService.findOne(pathId);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Collection<Segment> segments = null;
		try {
			Assert.notNull(b);
			Assert.notNull(path);
			Assert.isTrue(path.getParade().getBrotherhood().equals(b));
			Assert.isTrue(path.getParade().isDraftMode());

			segments = segmentService.findByPathId(pathId);
			Assert.notEmpty(segments);
			Segment segment = (Segment) getLastElement(segments);
			segmentService.delete(segment);

			result = new ModelAndView(
					"redirect:/segment/brotherhood/list.do?pathId=" + pathId);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/path/brotherhood/list.do");
			if (path == null)
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotExists");
			else if (!path.getParade().getBrotherhood().equals(b)) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotYours");
			} else if (path.getParade().isDraftMode() == false) {
				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ pathId);
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotDraft");
			} else if (segments.isEmpty()) {
				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ pathId);
				redirectAttrs
						.addFlashAttribute("message", "path.error.nothing");
			} else {
				redirectAttrs.addFlashAttribute("message", "commit.error");
			}
		}
		return result;
	}

	// CREATE
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int pathId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Path path = this.pathService.findOne(pathId);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Collection<Segment> segments = null;
		try {
			Assert.notNull(b);
			Assert.notNull(path);
			Assert.isTrue(path.getParade().getBrotherhood().equals(b));
			Assert.isTrue(path.getParade().isDraftMode());

			SegmentForm segmentForm = new SegmentForm();

			segments = segmentService.findByPathId(pathId);
			if (!segments.isEmpty()) {
				Segment last = (Segment) getLastElement(segments);
				segmentForm.setOriginLatitude(last.getDestinationLatitude());
				segmentForm.setOriginLongitude(last.getDestinationLongitude());
			}

			segmentForm.setPath(path);

			result = createModelAndView(segmentForm);

		} catch (final Throwable e) {

			result = new ModelAndView(
					"redirect:/segment/brotherhood/list.do?pathId=" + pathId);
			if (path == null)
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotExists");
			else if (!path.getParade().getBrotherhood().equals(b)) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotYours");
			} else if (path.getParade().isDraftMode() == false) {
				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ pathId);
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotDraft");
			} else {
				redirectAttrs.addFlashAttribute("message", "commit.error");
			}
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SegmentForm segmentForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Path path = pathService.findOne(segmentForm.getPath().getId());
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		if (binding.hasErrors())
			result = this.createModelAndView(segmentForm, "commit.error");
		else
			try {
				Assert.notNull(b);
				Assert.notNull(path);
				Assert.isTrue(path.getParade().getBrotherhood().equals(b));
				Assert.isTrue(path.getParade().isDraftMode());

				final Segment segment = this.segmentService.create();
				segment.setApproximateTimeDes(segmentForm
						.getApproximateTimeDes());
				segment.setApproximateTimeOri(segmentForm
						.getApproximateTimeOri());
				segment.setDestinationLatitude(segmentForm
						.getDestinationLatitude());
				segment.setDestinationLongitude(segmentForm
						.getDestinationLongitude());
				segment.setOriginLatitude(segmentForm.getOriginLatitude());
				segment.setOriginLongitude(segmentForm.getOriginLongitude());
				segment.setPath(path);

				this.segmentService.save(segment);

				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ segmentForm.getPath().getId());
			} catch (final Throwable oops) {
				result = this.createModelAndView(segmentForm, "commit.error");
				if (path == null)
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotExists");
				else if (!path.getParade().getBrotherhood().equals(b)) {
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotYours");
				} else if (path.getParade().isDraftMode() == false) {
					result = new ModelAndView(
							"redirect:/segment/brotherhood/list.do?pathId="
									+ segmentForm.getPath().getId());
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotDraft");
				} else {
					redirectAttrs.addFlashAttribute("message", "commit.error");
				}
			}
		return result;

	}

	// CREATE
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int segmentId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Segment segment = segmentService.findOne(segmentId);
		try {
			Assert.notNull(b);
			Assert.notNull(segment);
			Assert.isTrue(segment.getPath().getParade().getBrotherhood()
					.equals(b));
			Assert.isTrue(segment.getPath().getParade().isDraftMode());

			SegmentForm segmentForm = new SegmentForm();
			segmentForm.setId(segment.getId());
			segmentForm.setPath(segment.getPath());
			segmentForm.setApproximateTimeDes(segment.getApproximateTimeDes());
			segmentForm.setApproximateTimeOri(segment.getApproximateTimeOri());
			segmentForm
					.setDestinationLatitude(segment.getDestinationLatitude());
			segmentForm.setDestinationLongitude(segment
					.getDestinationLongitude());
			segmentForm.setId(segment.getId());
			segmentForm.setOriginLatitude(segment.getOriginLatitude());
			segmentForm.setOriginLongitude(segment.getOriginLongitude());

			result = editModelAndView(segmentForm);

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/path/brotherhood/list.do");
			if (segment == null)
				redirectAttrs.addFlashAttribute("message",
						"segment.error.segmentNotExists");
			else if (!segment.getPath().getParade().getBrotherhood().equals(b)) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotYours");
			} else if (segment.getPath().getParade().isDraftMode() == false) {
				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ segment.getPath().getId());
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotDraft");
			} else {
				redirectAttrs.addFlashAttribute("message", "commit.error");
			}
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final SegmentForm segmentForm,
			final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		Path path = pathService.findOne(segmentForm.getPath().getId());
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		final Segment segment = this.segmentService
				.findOne(segmentForm.getId());
		if (binding.hasErrors())
			result = this.editModelAndView(segmentForm, "commit.error");
		else
			try {
				Assert.notNull(b);
				Assert.notNull(segment);
				Assert.notNull(path);
				Assert.isTrue(path.getParade().getBrotherhood().equals(b));
				Assert.isTrue(path.getParade().isDraftMode());

				segment.setApproximateTimeDes(segmentForm
						.getApproximateTimeDes());
				segment.setApproximateTimeOri(segmentForm
						.getApproximateTimeOri());
				this.segmentService.save(segment);

				result = new ModelAndView(
						"redirect:/segment/brotherhood/list.do?pathId="
								+ segmentForm.getPath().getId());
			} catch (final Throwable oops) {
				result = this.createModelAndView(segmentForm, "commit.error");
				if (segment == null)
					redirectAttrs.addFlashAttribute("message",
							"segment.error.segmentNotExists");
				else if (path == null)
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotExists");
				else if (!path.getParade().getBrotherhood().equals(b)) {
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotYours");
				} else if (path.getParade().isDraftMode() == false) {
					result = new ModelAndView(
							"redirect:/segment/brotherhood/list.do?pathId="
									+ segmentForm.getPath().getId());
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotDraft");
				} else {
					redirectAttrs.addFlashAttribute("message", "commit.error");
				}
			}
		return result;

	}

	@SuppressWarnings("rawtypes")
	private Object getLastElement(final Collection c) {
		final Iterator itr = c.iterator();
		Object lastElement = itr.next();
		while (itr.hasNext()) {
			lastElement = itr.next();
		}
		return lastElement;
	}

	// MODEL
	protected ModelAndView createModelAndView(final SegmentForm segmentForm) {
		ModelAndView result;
		result = this.createModelAndView(segmentForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(final SegmentForm segmentForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("segment/create");

		Boolean isRead = false;
		if (segmentForm.getOriginLatitude() != null) {
			isRead = true;
		}

		result.addObject("message1", message);
		result.addObject("requestURI", "segment/brotherhood/create.do");
		result.addObject("segmentForm", segmentForm);
		result.addObject("isRead", isRead);
		result.addObject("pathId", segmentForm.getPath().getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView editModelAndView(final SegmentForm segmentForm) {
		ModelAndView result;
		result = this.editModelAndView(segmentForm, null);
		return result;
	}

	protected ModelAndView editModelAndView(final SegmentForm segmentForm,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("segment/edit");

		result.addObject("message1", message);
		result.addObject("requestURI", "segment/brotherhood/edit.do?segmentId="
				+ segmentForm.getId());
		result.addObject("segmentForm", segmentForm);
		result.addObject("pathId", segmentForm.getPath().getId());
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}
}