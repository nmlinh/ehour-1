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

package net.rrm.ehour.ui.admin.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

/**
 * Backing bean for users
 */

public class UserAdminBackingBean extends AdminBackingBeanImpl {
    private static final long serialVersionUID = 2781902854421696575L;
    private final User user;
    private String originalUsername;
    private final boolean editMode;

    private boolean showAssignments;

    public UserAdminBackingBean(User user) {
        this(user, true);

        this.originalUsername = user.getUsername();
    }

    public UserAdminBackingBean() {
        this(new User(), false);

        user.setActive(true);
    }

    public boolean isShowAssignments() {
        return showAssignments;
    }

    public void setShowAssignments(boolean showAssignments) {
        this.showAssignments = showAssignments;
    }

    private UserAdminBackingBean(User user, boolean editMode) {
        this.editMode = editMode;
        this.user = user;

    }

    public boolean isEditMode() {
        return editMode;
    }

    public User getUser() {
        return user;
    }

    public String getOriginalUsername() {
        return originalUsername;
    }

    public User getDomainObject() {
        return getUser();
    }
}
