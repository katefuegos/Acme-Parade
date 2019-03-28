
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.LinkRecordService;
import domain.LinkRecord;

@Component
@Transactional
public class StringToLinkRecordConverter implements Converter<String, LinkRecord> {

	@Autowired
	LinkRecordService	linkRecordService;


	@Override
	public LinkRecord convert(final String text) {
		LinkRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.linkRecordService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
