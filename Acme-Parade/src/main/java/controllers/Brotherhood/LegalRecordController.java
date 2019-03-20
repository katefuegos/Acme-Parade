
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
import services.LegalRecordService;
import controllers.AbstractController;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private LegalRecordService	legalRecordService;


	//Constructor--------------------------------------------------------------

	public LegalRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<LegalRecord> legalRecords = new ArrayList<>(this.legalRecordService.findLegalRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("legalRecord/list");
		modelAndView.addObject("legalRecords", legalRecords);

		modelAndView.addObject("requestURI", "/legalRecord/brotherhood/list.do");
		return modelAndView;

	}

	//
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final LegalRecord legalRecord = this.legalRecordService.create();

		modelAndView = this.createEditModelAndView(legalRecord);

		return modelAndView;
	}

	//	//Show
	//	@RequestMapping(value = "/show", method = RequestMethod.GET)
	//	private ModelAndView show(@RequestParam final int historyId) {
	//		final ModelAndView modelAndView;
	//
	//		final LegalRecord legalRecord = this.legalRecordService.findLegalRecordByHistoryId(historyId);
	//
	//		modelAndView = this.createEditModelAndView(legalRecord);
	//		modelAndView.addObject("isRead", true);
	//		modelAndView.addObject("requestURI", "/show.do?historyId=" + historyId);
	//
	//		return modelAndView;
	//	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		final ModelAndView modelAndView;
		final LegalRecord legalRecord = this.legalRecordService.findOne(historyId);

		modelAndView = this.createEditModelAndView(legalRecord);
		modelAndView.addObject("isRead", false);
		return modelAndView;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalRecord);
		else
			try {
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
			}
		return result;
	}

	//Cancel
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(final LegalRecord legalRecord) {

		ModelAndView result;

		if (legalRecord.getId() == 0)
			try {
				this.historyService.delete(legalRecord.getHistory());
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
			}
		else
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		return result;
	}

	//CreateModelAndView

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(legalRecord, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {
		ModelAndView result;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();

		result = new ModelAndView("personal/edit");
		result.addObject("legalRecord", legalRecord);
		result.addObject("message", message);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("isRead", false);
		result.addObject("requestURI", "legalRecord/brotherhood/edit.do");

		return result;
	}

}
