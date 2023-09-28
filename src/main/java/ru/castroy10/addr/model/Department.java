package ru.castroy10.addr.model;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "department_name")
    private String department_name;
    @Column(name = "parent_department_id")
    private int parent_department_id;
    @Column(name = "chief_employee_id")
    private int chief_employee_id;

    public Department() {
    }

    public Department(String department_name, int parent_department_id, int chief_employee_id) {
        this.department_name = department_name;
        this.parent_department_id = parent_department_id;
        this.chief_employee_id = chief_employee_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getParent_department_id() {
        return parent_department_id;
    }

    public void setParent_department_id(int parent_department_id) {
        this.parent_department_id = parent_department_id;
    }

    public int getChief_employee_id() {
        return chief_employee_id;
    }

    public void setChief_employee_id(int chief_employee_id) {
        this.chief_employee_id = chief_employee_id;
    }

    @Override
    public String toString() {
        return id + "/" + department_name + "/" + parent_department_id + "/" + chief_employee_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (id != that.id) return false;
        if (parent_department_id != that.parent_department_id) return false;
        if (chief_employee_id != that.chief_employee_id) return false;
        return department_name.equals(that.department_name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + department_name.hashCode();
        result = 31 * result + parent_department_id;
        result = 31 * result + chief_employee_id;
        return result;
    }
}
