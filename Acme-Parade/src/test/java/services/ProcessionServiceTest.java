package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Procession;

@ContextConfiguration(locations = {"classpath:spring/junit.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProcessionServiceTest extends AbstractTest {

	// Service----------------------------------------------------------
	@Autowired
	private ProcessionService processionService;

	// Test-------------------------------------------------------------

	@Test
	public void testAll() {
		// LOGIN COMO MEMBER
		authenticate("brotherhood1");

		// CREO REQUEST Y SETEO VALORES
		final Procession procession = processionService.create();
		procession.setDescription("description1");
		procession.setTitle("title1");

		// GUARDO REQUEST
		Procession saved = processionService.save(procession);

		// CHECK FINDALL
		Assert.isTrue(processionService.findAll().contains(saved));

		// CHECK FINDONE
		Assert.isTrue(processionService.findOne(saved.getId()) == saved);

		//EDITO PROCESSION
		saved.setDescription("description1 edited");
		Procession saved2 = processionService.save(saved);
		Assert.isTrue(processionService.findOne(saved2.getId()).getDescription().equals("description1 edited"));
		
		// BORRO PROCESSION
		processionService.delete(saved2);
		Assert.isTrue(!processionService.findAll().contains(saved2));

		unauthenticate();
	}
}