
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.InceptionRecordService;
import domain.InceptionRecord;

@Component
@Transactional
public class StringToInceptionRecordConverter implements Converter<String, InceptionRecord> {

	@Autowired
	InceptionRecordService	inceptionRecordService;


	@Override
	public InceptionRecord convert(final String text) {
		InceptionRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.inceptionRecordService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
