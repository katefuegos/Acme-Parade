package controllers.Brotherhood;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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

			result = new ModelAndView("segment/list");
			result.addObject("requestURI",
					"segment/brotherhood/list.do?pathId=" + pathId);
			result.addObject("segments", segments);
			result.addObject("pathId", pathId);
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
				result = new ModelAndView("redirect:/segment/brotherhood/list.do?pathId="+pathId);
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathNotDraft");
			} else if (segments.isEmpty()){
				result = new ModelAndView("redirect:/segment/brotherhood/list.do?pathId="+pathId);
				redirectAttrs.addFlashAttribute("message",
						"path.error.nothing");
			}
			else {	redirectAttrs.addFlashAttribute("message", "commit.error");
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
}
