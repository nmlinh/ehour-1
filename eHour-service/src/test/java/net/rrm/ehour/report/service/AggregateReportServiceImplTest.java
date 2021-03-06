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

package net.rrm.ehour.report.service;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElementMother;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class AggregateReportServiceImplTest {
    private AggregateReportServiceImpl aggregateReportService;
    private UserDao userDAO;
    private ProjectDao projectDAO;
    private ReportAggregatedDao reportAggregatedDAO;
    private ProjectAssignmentService assignmentService;

    @Before
    public void setUp() {
        aggregateReportService = new AggregateReportServiceImpl();

        reportAggregatedDAO = createMock(ReportAggregatedDao.class);
        aggregateReportService.setReportAggregatedDAO(reportAggregatedDAO);

        userDAO = createMock(UserDao.class);
        aggregateReportService.setUserDAO(userDAO);

        projectDAO = createMock(ProjectDao.class);
        aggregateReportService.setProjectDAO(projectDAO);

        assignmentService = createMock(ProjectAssignmentService.class);
        aggregateReportService.setProjectAssignmentService(assignmentService);
   }

    @Test
    public void testCreateProjectReportUserId() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        List<User> l = new ArrayList<User>();
        l.add(new User(1));
        uc.setUsers(l);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
        ReportCriteria rc = new ReportCriteria(uc);

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        replay(reportAggregatedDAO);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
    }

    @Test
    public void testCreateProjectReportNoUserId() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignment(isA(DateRange.class))).andReturn(pags);
        replay(reportAggregatedDAO);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
    }

    @Test
    public void testCreateProjectReportNoUserIdDptId() {
        List<User> users = new ArrayList<User>();
        User user = new User(1);
        users.add(user);

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        List<UserDepartment> l = new ArrayList<UserDepartment>();
        l.add(new UserDepartment(2));

        uc.setDepartments(l);
        uc.setOnlyActiveUsers(true);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);

        expect(userDAO.findUsersForDepartments(l, true)).andReturn(users);

        replay(reportAggregatedDAO);
        replay(userDAO);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
        verify(userDAO);
    }

    @Test
    public void testCreateProjectReport() {
        Customer cust = new Customer(1);
        List<Customer> customers = new ArrayList<Customer>();
        customers.add(cust);

        List<Project> prjs = new ArrayList<Project>();
        Project prj = new Project(1);
        prjs.add(prj);

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        uc.setCustomers(customers);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        expect(projectDAO.findProjectForCustomers(customers, true)).andReturn(prjs);

        replay(reportAggregatedDAO);
        replay(projectDAO);

        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
        verify(projectDAO);
    }

    @Test
    public void testGetProjectManagerReport() {
        Project project = new Project(1);
        project.setProjectCode("PRJ");

        List<AssignmentAggregateReportElement> elms = new ArrayList<AssignmentAggregateReportElement>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                elms.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(j, i, i));
            }
        }

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(elms);

        DateRange dr = new DateRange(new Date(), new Date());
        expect(reportAggregatedDAO.getMinMaxDateTimesheetEntry(project)).andReturn(dr);

        List<ProjectAssignment> assignments = new ArrayList<ProjectAssignment>();

        assignments.add(ProjectAssignmentObjectMother.createProjectAssignment(2));

        expect(assignmentService.getProjectAssignments(project, dr)).andReturn(assignments);

        replay(reportAggregatedDAO);
        replay(assignmentService);

        ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(project);
        verify(reportAggregatedDAO);
        verify(assignmentService);

        assertEquals(new Integer(1), report.getProject().getPK());
        assertEquals(16, report.getAggregates().size());

    }
}

