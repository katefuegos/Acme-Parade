
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.PeriodRecordService;
import domain.PeriodRecord;

@Component
@Transactional
public class StringToPeriodRecordConverter implements Converter<String, PeriodRecord> {

	@Autowired
	PeriodRecordService	periodRecordService;


	@Override
	public PeriodRecord convert(final String text) {
		PeriodRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.periodRecordService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
