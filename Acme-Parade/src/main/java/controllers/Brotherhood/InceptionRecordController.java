
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
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class InceptionRecordController extends AbstractController {

	//Service----------------------------------------------------------------

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	//Constructor--------------------------------------------------------------

	public InceptionRecordController() {
		super();
	}

	//Listing----------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView modelAndView;
		final Collection<InceptionRecord> inceptionRecords = new ArrayList<>(this.inceptionRecordService.findInceptionRecordByHistoryId(historyId));

		modelAndView = new ModelAndView("inceptionRecord/list");
		modelAndView.addObject("inceptionRecords", inceptionRecords);

		modelAndView.addObject("requestURI", "/inceptionRecord/brotherhood/list.do");
		return modelAndView;

	}

	//
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();

		modelAndView = this.createEditModelAndView(inceptionRecord);

		return modelAndView;
	}

	//	//Show
	//	@RequestMapping(value = "/show", method = RequestMethod.GET)
	//	private ModelAndView show(@RequestParam final int historyId) {
	//		final ModelAndView modelAndView;
	//
	//		final InceptionRecord inceptionRecord = this.inceptionRecordService.findInceptionRecordByHistoryId(historyId);
	//
	//		modelAndView = this.createEditModelAndView(inceptionRecord);
	//		modelAndView.addObject("isRead", true);
	//		modelAndView.addObject("requestURI", "/show.do?historyId=" + historyId);
	//
	//		return modelAndView;
	//	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		final ModelAndView modelAndView;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(historyId);

		modelAndView = this.createEditModelAndView(inceptionRecord);
		modelAndView.addObject("isRead", false);
		return modelAndView;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(inceptionRecord);
		else
			try {
				this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}
		return result;
	}

	//Cancel
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(final InceptionRecord inceptionRecord) {

		ModelAndView result;

		if (inceptionRecord.getId() == 0)
			try {
				this.historyService.delete(inceptionRecord.getHistory());
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}
		else
			result = new ModelAndView("redirect:/history/brotherhood/list.do");
		return result;
	}

	//CreateModelAndView

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(inceptionRecord, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {
		ModelAndView result;
		final int brotherhoodId = this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();

		result = new ModelAndView("personal/edit");
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("message", message);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("isRead", false);
		result.addObject("requestURI", "inceptionRecord/brotherhood/edit.do");

		return result;
	}

}
