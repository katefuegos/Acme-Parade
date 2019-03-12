package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = {"classpath:spring/junit.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// Service----------------------------------------------------------
	@Autowired
	private ParadeService paradeService;

	// Test-------------------------------------------------------------

	@Test
	public void testAll() {
		// LOGIN COMO MEMBER
		authenticate("brotherhood1");

		// CREO REQUEST Y SETEO VALORES
		final Parade parade = paradeService.create();
		parade.setDescription("description1");
		parade.setTitle("title1");

		// GUARDO REQUEST
		Parade saved = paradeService.save(parade);

		// CHECK FINDALL
		Assert.isTrue(paradeService.findAll().contains(saved));

		// CHECK FINDONE
		Assert.isTrue(paradeService.findOne(saved.getId()) == saved);

		//EDITO PROCESSION
		saved.setDescription("description1 edited");
		Parade saved2 = paradeService.save(saved);
		Assert.isTrue(paradeService.findOne(saved2.getId()).getDescription().equals("description1 edited"));
		
		// BORRO PROCESSION
		paradeService.delete(saved2);
		Assert.isTrue(!paradeService.findAll().contains(saved2));

		unauthenticate();
	}
}