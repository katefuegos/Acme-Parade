
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.MiscellaneousRecordService;
import domain.MiscellaneousRecord;

@Component
@Transactional
public class StringToMiscellaneousRecordConverter implements Converter<String, MiscellaneousRecord> {

	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;


	@Override
	public MiscellaneousRecord convert(final String text) {
		MiscellaneousRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.miscellaneousRecordService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
