package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ACTIVE_CUSTOMER;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ItCustomer;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ProjectDriver {

    public static final ItProject ACTIVE_PROJECT = new ItProject("ET", "ET");
    public static final ItProject INACTIVE_PROJECT = new ItProject("VPRO", "VPRO");

    public static void loadProjectAdmin() {
        Driver.get(BASE_URL + "/eh/admin/project");
    }

    public static ItProject createActiveProjectForActiveCustomer() {
        return createActiveProjectFor(ACTIVE_CUSTOMER);
    }

    public static ItProject createActiveProjectFor(ItCustomer customer) {
        createProject(ACTIVE_PROJECT, customer, true);
        return ACTIVE_PROJECT;
    }

    public static ItProject createInActiveProjectForActiveCustomer() {
        return createInActiveProjectFor(ACTIVE_CUSTOMER);
    }

    public static ItProject createInActiveProjectFor(ItCustomer customer) {
        createProject(INACTIVE_PROJECT, customer, false);
        return INACTIVE_PROJECT;
    }

    private static void createProject(ItProject project, ItCustomer customer, boolean active) {
        loadProjectAdmin();

        fillProjectForm(project, customer, active);

        storeProject();
    }

    public static void fillProjectForm(ItProject project, ItCustomer customer, boolean active) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name").sendKeys(project.name);
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode").sendKeys(project.code);

        if (!active) {
            findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.active").click();
        }

        String cust = String.format("%s - %s", customer.code, customer.name);
        new Select(findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.customer")).selectByVisibleText(cust);
    }

    public static void storeProject() {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_submitButton").click();

        assertTrue(findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_serverMessage").getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void editProject(String projectName) {
        WebElement listFilter = Driver.findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(projectName);

        findElement("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_0").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void showOnlyActiveProjects() {
        findElement("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_filterForm_filterToggle").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void assertProjectLoaded(String projectName) {
        String input = findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name").getAttribute("value");

        assertEquals(projectName, input);
    }

    public static void newAssignment() {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_addUsers").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void editUser(int index) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_assignmentContainer_assignments_" + index + "_container").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void setRateForUser(int index, String rate) {
        String path = "tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_assignmentContainer_assignments_" + index + "_container_editForm_rate";
        findElement(path).clear();
        findElement(path).sendKeys(rate);
    }

    public static void submitAssignment(int index) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_assignmentContainer_assignments_" + index + "_container_editForm_submit").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void deleteAssignment(int index) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_assignmentContainer_assignments_" + index + "_container_editForm_delete").click();
        EhourApplicationDriver.sleep(500);
    }

    public static void assertIsActiveAssignment(int index) {
        WebElement webElement = findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_assignmentContainer_assignments_" + index + "_container_activeAssignment");
        assertEquals("Assigned", webElement.getAttribute("title"));
    }

    public static void assertNoAssignments() {
        WebElement element = findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_assignedUsers_filterContainer");
        assertEquals("no users have been assigned", element.getText());
    }

    public static void selectProjectManager(String name) {
        new Select(findElement("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectManager")).selectByVisibleText(name);
    }

    public static class ItProject {
        public String name;
        public String code;

        public ItProject(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }
}
