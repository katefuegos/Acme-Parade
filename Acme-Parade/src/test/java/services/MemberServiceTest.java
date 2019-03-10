
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	//Service----------------------------------------------------------
	@Autowired
	private MemberService	memberService;


	//Test-------------------------------------------------------------

	@Test
	public void testCreate() {
		System.out.println("========== testCreate() ==========");
		this.authenticate("member1");

		try {
			final Member member = this.memberService.create();

			Assert.notNull(member, "Member creada no puede ser nula");

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	@Test
	public void testFindOne() {
		System.out.println("========== testFindOne() ==========");

		final int memberId = this.getEntityId("member1");

		try {
			final Member member = this.memberService.findOne(memberId);
			Assert.notNull(member);

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}
	@Test
	public void testFindAll() {
		System.out.println("========== testFindAll() ==========");

		try {
			final Collection<Member> memberes = new ArrayList<>(this.memberService.findAll());
			Assert.notEmpty(memberes);

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}

	@Test
	public void testSave() {
		System.out.println("========== testSave() ==========");
		this.authenticate("member1");

		try {

			Member member = this.memberService.create();
			Assert.notNull(member);
			final int memberId = this.getEntityId("member1");
			final Member member1 = this.memberService.findOne(memberId);
			member = member1;

			final Member saved = this.memberService.save(member);

			final Collection<Member> memberes = new ArrayList<>(this.memberService.findAll());
			Assert.isTrue(memberes.contains(saved));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	//	@Test
	//	public void testDelete() {
	//		System.out.println("========== testDelete() ==========");
	//
	//		try {
	//
	//			final Member member = this.memberService.findOne(this.getEntityId("member1"));
	//
	//			Assert.notNull(member);
	//			this.memberService.delete(member);
	//			final Collection<Member> memberes = this.memberService.findAll();
	//			Assert.isTrue(!memberes.contains(member));
	//
	//			System.out.println("¡Exito!");
	//
	//		} catch (final Exception e) {
	//			System.out.println("¡Fallo," + e.getMessage() + "!");
	//		}
	//
	//		this.unauthenticate();
	//
	//	}

}
