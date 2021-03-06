package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{UserObjectMother, ProjectAssignmentObjectMother, ProjectObjectMother}
import org.apache.wicket.model.Model
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.util._
import java.util
import net.rrm.ehour.ui.common.wicket.NonEmptyLabel

class AssignedUsersPanelSpec extends AbstractSpringWebAppSpec {
  "Assigned Users panel" should {
    val assignmentService = mock[ProjectAssignmentService]
    springTester.getMockContext.putBean(assignmentService)

    val userService = mock[UserService]
    springTester.getMockContext.putBean(userService)

    "render" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
    }

    "render with (new) project without id" in {
      val project = ProjectObjectMother.createProject(1)
      project.setProjectId(null)

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()

      verify(assignmentService, never).getProjectAssignment(anyInt())
    }

    "add unassigned users to the list when add users button clicked" in {
      when(userService.getActiveUsers).thenReturn(util.Arrays.asList(UserObjectMother.createUser()))

      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.executeAjaxEvent("id:addUsers", "click")

      verify(userService).getActiveUsers
    }

    "click on an existing assignment and clicking cancel should revert to display row" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container", "click")

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container:editForm:cancel", "click")

      tester.assertComponent("id:assignmentContainer:assignments:0:container:rate", classOf[NonEmptyLabel[Float]])
      tester.assertNoErrorMessage()
    }

    "click on an existing assignment, edit rate and click okay should persist and return to display row" in {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      assignment.setHourlyRate(5f)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container", "click")

      val formTester =tester.newFormTester("id:assignmentContainer:assignments:0:container:editForm")
      formTester.setValue("rate", "10")

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container:editForm:submit", "click")

      tester.assertComponent("id:assignmentContainer:assignments:0:container:rate", classOf[NonEmptyLabel[Float]])

      assignment.getHourlyRate should be (10f)

      tester.assertNoErrorMessage()
    }

    "click on an existing assignment, edit an invalid rate and click okay should give an error message" in {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container", "click")

      val formTester =tester.newFormTester("id:assignmentContainer:assignments:0:container:editForm")
      formTester.setValue("rate", "xx")

      tester.executeAjaxEvent("id:assignmentContainer:assignments:0:container:editForm:submit", "click")

      tester.assertErrorMessages("rate.IConverter.Float")
    }
  }
}
