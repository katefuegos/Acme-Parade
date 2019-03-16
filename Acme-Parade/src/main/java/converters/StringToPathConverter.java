
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.PathService;
import domain.Path;

@Component
@Transactional
public class StringToPathConverter implements Converter<String, Path> {

	@Autowired
	PathService	pathService;


	@Override
	public Path convert(final String text) {
		Path result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.pathService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
