
package services;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	//Service----------------------------------------------------------
	@Autowired
	private AdministratorService	administratorService;


	//Test-------------------------------------------------------------

	@Test
	public void testDashBoardQueryC2() {
		System.out.println("========== testDashBoardQueryC2() ==========");

		try {

			final Collection<Object[]> result = this.administratorService.queryC4();
			Assert.notNull(result);
			final Map<String, Double> statusCount = new TreeMap<>();
			for (final Object[] objects : result) {
				final String data1 = (String) objects[0];
				statusCount.put(data1, (Double) objects[1]);

				System.out.println(data1 + " --- " + statusCount.get(data1));

			}
			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}
	}
}
