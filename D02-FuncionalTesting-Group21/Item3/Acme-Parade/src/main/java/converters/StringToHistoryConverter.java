
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.HistoryRepository;
import domain.History;

@Component
@Transactional
public class StringToHistoryConverter implements Converter<String, History> {

	@Autowired
	HistoryRepository	historyRepository;


	@Override
	public History convert(final String text) {
		History result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.historyRepository.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
