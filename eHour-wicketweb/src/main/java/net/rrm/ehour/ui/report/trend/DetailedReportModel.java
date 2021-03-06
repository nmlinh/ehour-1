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

package net.rrm.ehour.ui.report.trend;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;
import net.rrm.ehour.ui.report.trend.node.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Detailed report
 **/

public class DetailedReportModel extends TreeReportModel
{
	private static final long serialVersionUID = -21703820501429504L;
	
	@SpringBean(name = "detailedReportService")
	private DetailedReportService detailedReportService;

	public DetailedReportModel(ReportCriteria reportCriteria)
	{
		super(reportCriteria, ReportConfig.DETAILED_REPORT);
	}


    @Override
	protected ReportData fetchReportData(ReportCriteria reportCriteria)
	{
		return getDetailedReportService().getDetailedReportData(reportCriteria);
	}

    @SuppressWarnings("unchecked")
    @Override
    protected void sort(ReportData reportData, ReportCriteria reportCriteria) {
        List<FlatReportElement> reportElements = (List<FlatReportElement>) reportData.getReportElements();

        Collections.sort(reportElements, new Comparator<FlatReportElement>() {
            @Override
            public int compare(FlatReportElement o1, FlatReportElement o2) {
                return o1.getDayDate().compareTo(o2.getDayDate());
            }
        });
    }

    private DetailedReportService getDetailedReportService()
	{
		if (detailedReportService == null)
		{
			WebUtils.springInjection(this);
		}
		
		return detailedReportService;
	}
	
	@Override
	public ReportNodeFactory<FlatReportElement> getReportNodeFactory()
	{
    	return new ReportNodeFactory<FlatReportElement>()
	    {
	        @Override
	        public ReportNode createReportNode(FlatReportElement flatElement, int hierarchyLevel)
	        {
	            switch (hierarchyLevel)
	            {
	                case 0:
                        return new FlatDateNode(flatElement);
                    case 1:
	                	return new FlatCustomerNode(flatElement);
	                case 2:
	                    return new FlatProjectNode(flatElement);
                    case 3:
                        return new FlatProjectCodeNode(flatElement);
	                case 4:
	                	return new FlatUserNode(flatElement);
	                case 5:
	                	return new FlatEntryEndNode(flatElement);
	            }
	
	            throw new RuntimeException("Hierarchy level too deep");
	        }
	
	        /**
	         * Only needed for the root node, customer
	         * @param aggregate
	         * @return
	         */
	        public Serializable getElementId(FlatReportElement flatElement)
	        {
	            return flatElement.getCustomerId();
	        }
	    };	
    }
}
