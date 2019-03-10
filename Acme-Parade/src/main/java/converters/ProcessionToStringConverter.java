/*
 * CurriculaToStringConverter.java
 * 
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Floaat;

@Component
@Transactional
public class ProcessionToStringConverter implements Converter<Floaat, String> {

	@Override
	public String convert(final Floaat floaat) {
		String result;

		if (floaat == null)
			result = null;
		else
			result = String.valueOf(floaat.getId());

		return result;
	}

}
