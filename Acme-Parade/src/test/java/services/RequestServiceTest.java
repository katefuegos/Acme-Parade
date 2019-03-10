package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Request;

@ContextConfiguration(locations = {"classpath:spring/junit.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestServiceTest extends AbstractTest {

	// Service----------------------------------------------------------
	@Autowired
	private RequestService requestService;
	@Autowired
	private ProcessionService processionService;

	// Test-------------------------------------------------------------

	@Test
	public void testAll() {
		// LOGIN COMO MEMBER
		authenticate("member1");

		// CREO REQUEST Y SETEO VALORES
		final int processionId = this.getEntityId("procession1");
		final Request request = requestService.create();
		request.setProcession(processionService.findOne(processionId));

		// GUARDO REQUEST
		Request saved = requestService.save(request);

		// CHECK FINDALL
		Assert.isTrue(requestService.findAll().contains(saved));

		// CHECK FINDONE
		Assert.isTrue(requestService.findOne(saved.getId()) == saved);

		unauthenticate();

		// LOGIN COMO BROTHERHOOD Y ACCEPTO REQUEST
		authenticate("brotherhood1");
		saved.setStatus("APPROVED");
		saved.setRoow(1);
		saved.setColuumn(2);
		Request saved2 = requestService.save(saved);
		Assert.isTrue(requestService.findOne(saved2.getId()).getRoow() == 1);

		unauthenticate();

		// BORRO REQUEST
		authenticate("member1");
		requestService.delete(saved2);
		Assert.isTrue(!requestService.findAll().contains(saved2));

		unauthenticate();
	}
}