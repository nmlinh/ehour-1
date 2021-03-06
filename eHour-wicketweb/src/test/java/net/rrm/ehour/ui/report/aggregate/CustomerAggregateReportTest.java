/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * CustomerAggregateReport Tester.
 *
 * @author <Authors name>
 * @since <pre>09/11/2007</pre>
 * @version 1.0
 */
public class CustomerAggregateReportTest extends BaseSpringWebAppTester
{
	private AggregateReportService aggregateReportService;

	@Before
	public void setup() throws Exception
	{
		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregateReportService", aggregateReportService);

	}
	
	@Test
    public void should_create_report() throws Exception
    {
		expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
			.andReturn(AggregateReportDataObjectMother.getAssignmentReportData());
		replay(aggregateReportService);
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();

        ReportCriteria rc = new ReportCriteria(userSelectedCriteria);
        
        CustomerAggregateReportModel aggReport = new CustomerAggregateReportModel(rc);

        assertEquals(6, aggReport.getReportData().getReportElements().size());
        
        verify(aggregateReportService);
    }
}
