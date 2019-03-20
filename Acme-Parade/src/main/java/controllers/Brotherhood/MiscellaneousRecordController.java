
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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private BrotherhoodService			brotherhoodService;

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	//Constructor--------------------------------------------------------------

	public MiscellaneousRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>(this.miscellaneousRecordService.findMiscellaneousRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("miscellaneousRecord/list");
		modelAndView.addObject("miscellaneousRecords", miscellaneousRecords);

		modelAndView.addObject("requestURI", "/miscellaneousRecord/brotherhood/list.do");
		return modelAndView;

	}

	//
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();

		modelAndView = this.createEditModelAndView(miscellaneousRecord);

		return modelAndView;
	}

	//	//Show
	//	@RequestMapping(value = "/show", method = RequestMethod.GET)
	//	private ModelAndView show(@RequestParam final int historyId) {
	//		final ModelAndView modelAndView;
	//
	//		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findMiscellaneousRecordByHistoryId(historyId);
	//
	//		modelAndView = this.createEditModelAndView(miscellaneousRecord);
	//		modelAndView.addObject("isRead", true);
	//		modelAndView.addObject("requestURI", "/show.do?historyId=" + historyId);
	//
	//		return modelAndView;
	//	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		final ModelAndView modelAndView;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(historyId);

		modelAndView = this.createEditModelAndView(miscellaneousRecord);
		modelAndView.addObject("isRead", false);
		return modelAndView;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord);
		else
			try {
				this.miscellaneousRecordService.save(miscellaneousRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}
		return result;
	}

	//Cancel
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(final MiscellaneousRecord miscellaneousRecord) {

		ModelAndView result;

		if (miscellaneousRecord.getId() == 0)
			try {
				this.historyService.delete(miscellaneousRecord.getHistory());
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}
		else
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		return result;
	}

	//CreateModelAndView

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(miscellaneousRecord, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {
		ModelAndView result;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();

		result = new ModelAndView("personal/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("message", message);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("isRead", false);
		result.addObject("requestURI", "miscellaneousRecord/brotherhood/edit.do");

		return result;
	}

}
