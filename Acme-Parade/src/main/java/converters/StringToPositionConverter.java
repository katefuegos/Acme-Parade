
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PositionRepository;
import domain.Position;

@Component
@Transactional
public class StringToPositionConverter implements Converter<String, Position> {

	@Autowired
	PositionRepository	positionRepository;


	@Override
	public Position convert(final String source) {
		final Position position;
		int id;

		if (StringUtils.isEmpty(source))
			position = null;
		else
			try {
				id = Integer.valueOf(source);
				position = this.positionRepository.findOne(id);

			} catch (final Throwable throwable) {
				throw new IllegalArgumentException();
			}

		return position;
	}
}
