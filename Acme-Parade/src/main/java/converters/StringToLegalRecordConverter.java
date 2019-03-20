
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.LegalRecordService;
import domain.LegalRecord;

@Component
@Transactional
public class StringToLegalRecordConverter implements Converter<String, LegalRecord> {

	@Autowired
	LegalRecordService	legalRecordService;


	@Override
	public LegalRecord convert(final String text) {
		LegalRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.legalRecordService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
