
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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private PeriodRecordService	periodRecordService;


	//Constructor--------------------------------------------------------------

	public PeriodRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<PeriodRecord> periodRecords = new ArrayList<>(this.periodRecordService.findPeriodRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("periodRecord/list");
		modelAndView.addObject("periodRecords", periodRecords);

		modelAndView.addObject("requestURI", "/periodRecord/brotherhood/list.do");
		return modelAndView;

	}

	//
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final PeriodRecord periodRecord = this.periodRecordService.create();

		modelAndView = this.createEditModelAndView(periodRecord);

		return modelAndView;
	}

	//	//Show
	//	@RequestMapping(value = "/show", method = RequestMethod.GET)
	//	private ModelAndView show(@RequestParam final int historyId) {
	//		final ModelAndView modelAndView;
	//
	//		final PeriodRecord periodRecord = this.periodRecordService.findPeriodRecordByHistoryId(historyId);
	//
	//		modelAndView = this.createEditModelAndView(periodRecord);
	//		modelAndView.addObject("isRead", true);
	//		modelAndView.addObject("requestURI", "/show.do?historyId=" + historyId);
	//
	//		return modelAndView;
	//	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		final ModelAndView modelAndView;
		final PeriodRecord periodRecord = this.periodRecordService.findOne(historyId);

		modelAndView = this.createEditModelAndView(periodRecord);
		modelAndView.addObject("isRead", false);
		return modelAndView;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try {
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
			}
		return result;
	}

	//Cancel
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(final PeriodRecord periodRecord) {

		ModelAndView result;

		if (periodRecord.getId() == 0)
			try {
				this.historyService.delete(periodRecord.getHistory());
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
			}
		else
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		return result;
	}

	//CreateModelAndView

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(periodRecord, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
		ModelAndView result;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();

		result = new ModelAndView("personal/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("message", message);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("isRead", false);
		result.addObject("requestURI", "periodRecord/brotherhood/edit.do");

		return result;
	}

}
