package in.deloitte.screening.app.user.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_desc")
    private String roleDesc;

    @Column(name = "role_shortname")
    private String roleShortName;

    @Column(name = "role_code", unique = true)
    private String roleCode;

    public UserRoles(Long roleId, String roleDesc, String roleShortName, String roleCode) {
        this.roleId = roleId;
        this.roleDesc = roleDesc;
        this.roleShortName = roleShortName;
        this.roleCode = roleCode;
    }

    public UserRoles() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleShortName() {
        return roleShortName;
    }

    public void setRoleShortName(String roleShortName) {
        this.roleShortName = roleShortName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }


}
