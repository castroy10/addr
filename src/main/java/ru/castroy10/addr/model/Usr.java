package ru.castroy10.addr.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usr")
public class Usr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Column(name = "accountexperied")
    boolean accountexperied;
    @Column(name = "accountlocked")
    boolean accountlocked;
    @Column(name = "credentialsexperied")
    boolean credentialsexperied;
    @JoinColumn(name = "employee_id")
    @OneToOne
    Employee employee;

    public Usr() {
    }

    public Usr(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountexperied() {
        return accountexperied;
    }

    public void setAccountexperied(boolean accountexperied) {
        this.accountexperied = accountexperied;
    }

    public boolean isAccountlocked() {
        return accountlocked;
    }

    public void setAccountlocked(boolean accountlocked) {
        this.accountlocked = accountlocked;
    }

    public boolean isCredentialsexperied() {
        return credentialsexperied;
    }

    public void setCredentialsexperied(boolean credentialsexperied) {
        this.credentialsexperied = credentialsexperied;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return id + "/" + username + "/" + password + "/" + accountexperied + "/" + accountlocked + "/" + credentialsexperied;
    }
}
