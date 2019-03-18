
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.SegmentService;
import domain.Segment;

@Component
@Transactional
public class StringToSegmentConverter implements Converter<String, Segment> {

	@Autowired
	SegmentService	segmentService;


	@Override
	public Segment convert(final String text) {
		Segment result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.segmentService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
