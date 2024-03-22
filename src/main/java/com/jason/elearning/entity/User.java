package com.jason.elearning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.entity.constants.UserActive;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Size(max = 20)
    @Column(name = "phone")
    private String phone;

    @Size(max = 40)
    @Column(name = "email")
    private String email;

    @Size(max = 40)
    private String name;

    private String password;


    private Long avatarId;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avatarId", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile avatar;


    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private UserActive active;

    @Transient
    private String accessToken;





    @Transient
    private RoleName roleName;

    public UserActive getActive() {
        return active;
    }

    public void setActive(UserActive active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Role> getRoles() {
        return new ArrayList<>(roles);
    }

    @JsonIgnore
    public Set<Role> getSetRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public UploadFile getAvatar() {
        return avatar;
    }

    public void setAvatar(UploadFile avatar) {
        this.avatar = avatar;
    }





    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

}
