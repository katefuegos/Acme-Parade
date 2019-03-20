
package controllers.administrator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Member;
import domain.Parade;
import forms.AreaQueryB1Form;
import forms.PositionCountForm;
import forms.QueryForm;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services-----------------------------------------------------------
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructor-------------------------------------------------------

	public DashboardAdministratorController() {
		super();
	}

	// Dashboard---------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {
		final ModelAndView modelAndView = new ModelAndView("administrator/dashboard");
		final DecimalFormat df = new DecimalFormat("0.00");

		final String nulo = "n/a";

		// QueryC1
		final Object[] result = this.administratorService.queryC1();

		final Double avgC1 = (Double) result[0];
		final Double minC1 = (Double) result[1];
		final Double maxC1 = (Double) result[2];
		final Double stddevC1 = (Double) result[3];

		if (avgC1 != null)
			modelAndView.addObject("avgC1", df.format(avgC1));
		else
			modelAndView.addObject("avgC1", nulo);

		if (avgC1 != null)
			modelAndView.addObject("maxC1", df.format(maxC1));
		else
			modelAndView.addObject("maxC1", nulo);

		if (minC1 != null)
			modelAndView.addObject("minC1", df.format(minC1));
		else
			modelAndView.addObject("minC1", nulo);

		if (stddevC1 != null)
			modelAndView.addObject("stddevC1", df.format(stddevC1));
		else
			modelAndView.addObject("stddevC1", nulo);

		// QueryC2 - The largest brotherhood, minimum 1
		final Collection<Object[]> resultC2 = this.administratorService.queryC2();

		Integer idLargest = null;
		String nameLargest = null;
		Long countLargest = null;

		if (!resultC2.isEmpty()) {
			final Object[] largest = resultC2.iterator().next();
			idLargest = (Integer) largest[0];
			nameLargest = String.valueOf(largest[1]);
			countLargest = (Long) largest[2];
		}

		if (idLargest != null && nameLargest != null && countLargest != null) {
			modelAndView.addObject("idLargest", idLargest);
			modelAndView.addObject("nameLargest", nameLargest);
			modelAndView.addObject("countLargest", countLargest);
		}

		// QueryC3 - The smallest brotherhood, minimum 1
		final Collection<Object[]> resultC3 = this.administratorService.queryC3();

		Integer idSmallest = null;
		String nameSmallest = null;
		Long countSmallest = null;

		if (!resultC3.isEmpty()) {
			final Object[] smallest = resultC3.iterator().next();
			idSmallest = (Integer) smallest[0];
			nameSmallest = String.valueOf(smallest[1]);
			countSmallest = (Long) smallest[2];
		}

		if (idSmallest != null && nameSmallest != null && countSmallest != null) {
			modelAndView.addObject("idSmallest", idSmallest);
			modelAndView.addObject("nameSmallest", nameSmallest);
			modelAndView.addObject("countSmallest", countSmallest);
		}

		// QueryC4 - The ratio of requests to march in a parade, grouped by
		// their status.
		try {
			final Map<String, String> statusCount = new TreeMap<>();
			for (final Object[] resultC : this.administratorService.queryC4())
				statusCount.put(((String) resultC[0]).toUpperCase(), df.format(resultC[1]));

			modelAndView.addObject("statusCount", statusCount);

		} catch (final Exception e) {
			modelAndView.addObject("sizeC4", 0);
		}
		// QueryC5 - The parades that are going to be organised in 30 days
		// or less.
		try {
			final Collection<Parade> parades = this.administratorService.queryC5();

			modelAndView.addObject("paradesC5", parades);
		} catch (final Exception e) {
			modelAndView.addObject("message", "message.commit.error");
			modelAndView.addObject("excP", e.getMessage());
		}

		// QueryC71
		try {
			final Collection<Member> queryC7 = this.administratorService.queryC7();
			modelAndView.addObject("queryC7", queryC7);
		} catch (final Exception e) {
			modelAndView.addObject("message", "message.commit.error");
			modelAndView.addObject("excM", e.getMessage());
		}

		// QueryC8

		final Collection<PositionCountForm> queryC8 = this.administratorService.queryC8();

		if (queryC8 != null) {
			modelAndView.addObject("position", queryC8);
			modelAndView.addObject("lang", LocaleContextHolder.getLocale().getLanguage().toUpperCase());
		}

		// Query B1
		final Object[] queryB1B = this.administratorService.queryB1B();

		final Double avgB1 = (Double) queryB1B[0];
		final Double minB1 = (Double) queryB1B[1];
		final Double maxB1 = (Double) queryB1B[2];
		final Double stddevB1 = (Double) queryB1B[3];

		if (avgB1 != null)
			modelAndView.addObject("avgB1", df.format(avgB1));
		else
			modelAndView.addObject("avgB1", nulo);

		if (avgB1 != null)
			modelAndView.addObject("maxB1", df.format(maxB1));
		else
			modelAndView.addObject("maxB1", nulo);

		if (minB1 != null)
			modelAndView.addObject("minB1", df.format(minB1));
		else
			modelAndView.addObject("minB1", nulo);

		if (stddevB1 != null)
			modelAndView.addObject("stddevB1", df.format(stddevB1));
		else
			modelAndView.addObject("stddevB1", nulo);

		final Collection<AreaQueryB1Form> queryB1A = this.administratorService.queryB1A();

		modelAndView.addObject("areaQueryB1", queryB1A);

		// Query B2
		final Object[] queryB2 = this.administratorService.queryB2();

		final Double avgB2 = (Double) queryB2[0];
		final Double minB2 = (Double) queryB2[1];
		final Double maxB2 = (Double) queryB2[2];
		final Double stddevB2 = (Double) queryB2[3];

		if (avgB2 != null)
			modelAndView.addObject("avgB2", df.format(avgB2));
		else
			modelAndView.addObject("avgB2", nulo);

		if (avgB2 != null)
			modelAndView.addObject("maxB2", df.format(maxB2));
		else
			modelAndView.addObject("maxB2", nulo);

		if (minB2 != null)
			modelAndView.addObject("minB2", df.format(minB2));
		else
			modelAndView.addObject("minB2", nulo);

		if (stddevB2 != null)
			modelAndView.addObject("stddevB2", df.format(stddevB2));
		else
			modelAndView.addObject("stddevB2", nulo);

		// Query B3
		final Double queryB3Empty = this.administratorService.queryB3Empty();

		if (queryB3Empty != null)
			modelAndView.addObject("queryB3FinderResultEmpty", df.format(queryB3Empty));
		else
			modelAndView.addObject("queryB3FinderResultEmpty", nulo);

		final Double queryB3NotEmpty = this.administratorService.queryB3NotEmpty();

		if (queryB3NotEmpty != null)
			modelAndView.addObject("queryB3FinderResultNotEmpty", df.format(queryB3NotEmpty));
		else
			modelAndView.addObject("queryB3FinderResultNotEmpty", nulo);

		// ----------------------------------------------------------------------
		// --------------------- NEW FUNTIONS -----------------------
		// ----------------------------------------------------------------------

		// Query New C1
		final Object[] queryNewC1 = this.administratorService.queryNewC1();

		final Double avgNewC1 = (Double) queryNewC1[0];
		final Double minNewC1 = (Double) queryNewC1[1];
		final Double maxNewC1 = (Double) queryNewC1[2];
		final Double stddevNewC1 = (Double) queryNewC1[3];

		if (avgNewC1 != null)
			modelAndView.addObject("avgNewC1", df.format(avgNewC1));
		else
			modelAndView.addObject("avgNewC1", nulo);

		if (minNewC1 != null)
			modelAndView.addObject("minNewC1", df.format(minNewC1));
		else
			modelAndView.addObject("minNewC1", nulo);

		if (maxNewC1 != null)
			modelAndView.addObject("maxNewC1", df.format(maxNewC1));
		else
			modelAndView.addObject("maxNewC1", nulo);

		if (stddevNewC1 != null)
			modelAndView.addObject("stddevNewC1", df.format(stddevNewC1));
		else
			modelAndView.addObject("stddevNewC1", nulo);

		// Query New C2
		try {
			final Collection<domain.Brotherhood> queryNewC2 = this.administratorService.queryNewC2();
			modelAndView.addObject("queryNewC2", queryNewC2);
		} catch (final Exception e) {
			modelAndView.addObject("queryNewC2", new ArrayList<domain.Brotherhood>());
			modelAndView.addObject("message", "message.commit.error");
		}

		// Query New C3
		try {
			final Collection<domain.Brotherhood> queryNewC3 = this.administratorService.queryNewC3();
			modelAndView.addObject("queryNewC3", queryNewC3);
		} catch (final Exception e) {
			modelAndView.addObject("queryNewC3", new ArrayList<domain.Brotherhood>());
			modelAndView.addObject("message", "message.commit.error");
		}

		// Query New B1
		final Double queryNewB1 = this.administratorService.queryNewB1();

		if (queryNewB1 != null)
			modelAndView.addObject("queryNewB1", df.format(queryNewB1));
		else
			modelAndView.addObject("queryNewB1", nulo);

		// Query New B2
		final Object[] queryNewB2 = this.administratorService.queryNewB2();

		final Double avgNewB2 = (Double) queryNewB2[0];
		final Double minNewB2 = (Double) queryNewB2[1];
		final Double maxNewB2 = (Double) queryNewB2[2];
		final Double stddevNewB2 = (Double) queryNewB2[3];

		if (avgNewB2 != null)
			modelAndView.addObject("avgNewB2", df.format(avgNewB2));
		else
			modelAndView.addObject("avgNewB2", nulo);

		if (minNewB2 != null)
			modelAndView.addObject("minNewB2", df.format(minNewB2));
		else
			modelAndView.addObject("minNewB2", nulo);

		if (maxNewB2 != null)
			modelAndView.addObject("maxNewB2", df.format(maxNewB2));
		else
			modelAndView.addObject("maxNewB2", nulo);

		if (stddevNewB2 != null)
			modelAndView.addObject("stddevNewB2", df.format(stddevNewB2));
		else
			modelAndView.addObject("stddevNewB2", nulo);

		// Query New B3
		try {
			final Collection<domain.Chapter> queryNewB3 = this.administratorService.queryNewB3();
			modelAndView.addObject("queryNewB3", queryNewB3);
		} catch (final Exception e) {
			modelAndView.addObject("queryNewB3", new ArrayList<domain.Chapter>());
			modelAndView.addObject("message", "message.commit.error");
		}

		// Query New B4
		final Object[] queryNewB4 = this.administratorService.queryNewB4();

		final Double ratioDraftMode = (Double) queryNewB4[0];
		final Double ratioFinalMode = (Double) queryNewB4[1];

		if (ratioDraftMode != null)
			modelAndView.addObject("ratioDraftMode", df.format(ratioDraftMode));
		else
			modelAndView.addObject("ratioDraftMode", nulo);

		if (ratioFinalMode != null)
			modelAndView.addObject("ratioFinalMode", df.format(ratioFinalMode));
		else
			modelAndView.addObject("ratioFinalMode", nulo);

		// Query New B5
		try {
			final Collection<QueryForm> queryNewB5 = this.administratorService.queryNewB5();
			modelAndView.addObject("queryNewB5", queryNewB5);
		} catch (final Exception e) {
			modelAndView.addObject("queryNewB5", new ArrayList<QueryForm>());
			modelAndView.addObject("message", "message.commit.error");
		}

		// --------------------------------
		modelAndView.addObject("banner", this.configurationService.findAll().iterator().next().getBanner());
		modelAndView.addObject("systemName", this.configurationService.findAll().iterator().next().getSystemName());
		return modelAndView;
	}
}
