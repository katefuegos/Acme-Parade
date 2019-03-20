
package controllers.Brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.HistoryService;
import services.LinkRecordService;
import controllers.AbstractController;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private LinkRecordService	linkRecordService;


	//Constructor--------------------------------------------------------------

	public LinkRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<LinkRecord> linkRecords = new ArrayList<>(this.linkRecordService.findLinkRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("linkRecord/list");
		modelAndView.addObject("linkRecords", linkRecords);

		modelAndView.addObject("requestURI", "/linkRecord/brotherhood/list.do");
		return modelAndView;

	}

	//
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final LinkRecord linkRecord = this.linkRecordService.create();

		modelAndView = this.createEditModelAndView(linkRecord);

		return modelAndView;
	}

	//	//Show
	//	@RequestMapping(value = "/show", method = RequestMethod.GET)
	//	private ModelAndView show(@RequestParam final int historyId) {
	//		final ModelAndView modelAndView;
	//
	//		final LinkRecord linkRecord = this.linkRecordService.findLinkRecordByHistoryId(historyId);
	//
	//		modelAndView = this.createEditModelAndView(linkRecord);
	//		modelAndView.addObject("isRead", true);
	//		modelAndView.addObject("requestURI", "/show.do?historyId=" + historyId);
	//
	//		return modelAndView;
	//	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		final ModelAndView modelAndView;
		final LinkRecord linkRecord = this.linkRecordService.findOne(historyId);

		modelAndView = this.createEditModelAndView(linkRecord);
		modelAndView.addObject("isRead", false);
		return modelAndView;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord linkRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(linkRecord);
		else
			try {
				this.linkRecordService.save(linkRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");
			}
		return result;
	}

	//Cancel
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(final LinkRecord linkRecord) {

		ModelAndView result;

		if (linkRecord.getId() == 0)
			try {
				this.historyService.delete(linkRecord.getHistory());
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");
			}
		else
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		return result;
	}

	//CreateModelAndView

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(linkRecord, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String message) {
		ModelAndView result;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();

		result = new ModelAndView("personal/edit");
		result.addObject("linkRecord", linkRecord);
		result.addObject("message", message);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("isRead", false);
		result.addObject("requestURI", "linkRecord/brotherhood/edit.do");

		return result;
	}

}
