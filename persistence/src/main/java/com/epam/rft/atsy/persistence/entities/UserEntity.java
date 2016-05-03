package com.epam.rft.atsy.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "Users", schema = "atsy", uniqueConstraints = @UniqueConstraint(columnNames = "user_name"))
public class UserEntity extends SuperEntity implements java.io.Serializable{
    @Column(name = "user_name", nullable = false, length = 255, table = "Users")
    private String userName;
    @Column(name = "user_pwd", nullable = false, length = 255, table = "Users")
    private String userPassword;

    /**
     * Empty constructor.
     */
    public UserEntity() {
    }

    /**
     * Constructor for users without id.
     * @param userName is the name of the user
     * @param userPassword is the password of the user
     */
    public UserEntity(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public UserEntity(Long id, String userName, String userPassword) {
        super(id);
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * Getter for the userName field.
     * Returns a String represents the userName.
     * @return long value represents the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for the userName field.
     * @param userName is a String for the userName field
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for the userPassword field.
     * Returns a String value represents the userPassword.
     * @return String value represents the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Setter for the userPassword field.
     * @param userPassword is a String for the userPassword field
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserEntity that = (UserEntity) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return !(userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
