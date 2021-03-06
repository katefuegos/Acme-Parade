package controllers.Brotherhood;

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
import services.BrotherhoodService;
import services.ConfigurationService;
import services.ParadeService;
import services.PathService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Path;
import domain.Segment;

@Controller
@RequestMapping("/path/brotherhood")
public class PathBrotherhoodController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private PathService pathService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private SegmentService segmentService;

	// Constructor---------------------------------------------------------

	public PathBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		try {
			final Integer brotherhoodId = this.brotherhoodService
					.findByUserAccountId(LoginService.getPrincipal().getId())
					.getId();
			Assert.notNull(this.brotherhoodService.findOne(brotherhoodId));

			final Collection<Path> paths = this.pathService
					.findByBrotherhoodId(brotherhoodId);
			Collection<Parade> parades = paradeService
					.findByBrotherhoodIdAndPending(brotherhoodId);
			Collection<Parade> parades2 = new ArrayList<Parade>();
			
			if (!parades.isEmpty()) {
				for (Parade p:parades) {
					Path pt = pathService.findByParadeId(p.getId());
					if (pt != null) {
						parades2.add(p);
					}
				}
			}
			
			parades.removeAll(parades2);

			result = new ModelAndView("path/list");
			result.addObject("requestURI", "path/brotherhood/list.do");
			result.addObject("paths", paths);
			result.addObject("parades", parades);
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/path/brotherhood/list.do");
			redirectAttrs.addFlashAttribute("message", "commit.error");
		}
		return result;
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int paradeId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		try {
			Assert.notNull(b);
			Assert.notNull(parade);
			Assert.isTrue(paradeService.findOne(paradeId).getBrotherhood()
					.equals(b));
			Assert.isTrue(parade.getStatus().equals("PENDING"));
			Assert.isNull(pathService.findByParadeId(parade.getId()));

			Path path = pathService.create();
			path.setParade(parade);
			pathService.save(path);

			result = new ModelAndView("redirect:/path/brotherhood/list.do");

		} catch (final Throwable e) {

			result = new ModelAndView("redirect:/path/brotherhood/list.do");
			if (parade == null)
				redirectAttrs.addFlashAttribute("message",
						"parade.error.paradeUnexists");
			else if (!parade.getBrotherhood().equals(b)) {
				redirectAttrs.addFlashAttribute("message",
						"parade.error.nobrotherhood");
			} else if (!parade.getStatus().equals("PENDING")) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.notPending");
			} else if (!(pathService.findByParadeId(parade.getId())==null)) {
				redirectAttrs.addFlashAttribute("message",
						"path.error.pathExists");
			} else
				redirectAttrs.addFlashAttribute("message", "commit.error");
		}
		return result;
	}
	
	// Create ---------------------------------------------------------------
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public ModelAndView delete(final int pathId,
				final RedirectAttributes redirectAttrs) {
			ModelAndView result;
			final Path path = this.pathService.findOne(pathId);
			Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			try {
				Assert.notNull(b);
				Assert.notNull(path);
				Assert.isTrue(path.getParade().getBrotherhood()
						.equals(b));
				Assert.isTrue(path.getParade().getStatus().equals("PENDING"));

				Collection<Segment> segments = segmentService.findByPathId(pathId);
				if(!segments.isEmpty()){
					for(Segment s:segments){
						segmentService.delete(s);
					}
				}
				
				pathService.delete(path);

				result = new ModelAndView("redirect:/path/brotherhood/list.do");

			} catch (final Throwable e) {

				result = new ModelAndView("redirect:/path/brotherhood/list.do");
				if (path == null)
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotExists");
				else if (!path.getParade().getBrotherhood().equals(b)) {
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotYours");
				} else if (path.getParade().isDraftMode()==false) {
					redirectAttrs.addFlashAttribute("message",
							"path.error.pathNotDraft");
				} else
					redirectAttrs.addFlashAttribute("message", "commit.error");
			}
			return result;
		}
}
