
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"

})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	//Service ------------------------------

	@Autowired
	private SocialProfileService	SocialProfileService;


	//Test

	@Test
	public void testCreate() {

		System.out.println("========== testCreate() ==========");

		authenticate("admin");

		try {

			final SocialProfile SocialProfile = this.SocialProfileService.create();

			SocialProfile.setLinkSocialNetwork("http://SocialProfile7.com");

			SocialProfile.setNameSocialNetwork("Antonio");

			SocialProfile.setNick("piwflow");

			Assert.notNull(SocialProfile);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testFindOne() {

		System.out.println("========== testFindOne() ==========");

		final int SocialProfileId = this.getEntityId("socialProfile1");

		try {

			final SocialProfile SocialProfile = this.SocialProfileService.findOne(SocialProfileId);

			Assert.notNull(SocialProfile);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testSave() {

		System.out.println("========== testSave() ==========");

		SocialProfile SocialProfile, saved;

		this.authenticate("administrator1");

		final Collection<SocialProfile> SocialProfiles;

		authenticate("admin");

		try {

			SocialProfile = this.SocialProfileService.create();

			SocialProfile.setLinkSocialNetwork("http://SocialProfile10.com");

			SocialProfile.setNameSocialNetwork("Antonia");

			SocialProfile.setNick("piwflow2");

			saved = this.SocialProfileService.save(SocialProfile);

			SocialProfiles = this.SocialProfileService.findAll();

			Assert.isTrue(SocialProfiles.contains(saved));

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

		this.unauthenticate();

	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");

		final int SocialProfileId = this.getEntityId("socialProfile1");

		try {

			final SocialProfile SocialProfile = this.SocialProfileService.findOne(SocialProfileId);

			final Collection<SocialProfile> SocialProfiles = this.SocialProfileService.findAll();

			Assert.isTrue(SocialProfiles.contains(SocialProfile));

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testDelete() {

		System.out.println("========== testDelete() ==========");

		this.authenticate("administrator1");

		final int SocialProfileId = this.getEntityId("socialProfile1");

		try {

			final SocialProfile SocialProfile = this.SocialProfileService.findOne(SocialProfileId);

			this.SocialProfileService.delete(SocialProfile);

			final Collection<SocialProfile> SocialProfiles = this.SocialProfileService.findAll();

			Assert.notNull(SocialProfiles);

			Assert.isTrue(!SocialProfiles.contains(SocialProfile));

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

		this.unauthenticate();

	}

}
