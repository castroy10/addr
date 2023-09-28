package ru.castroy10.addr.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "position_name")
    private String position_name;
    @Column(name = "parent_position_id")
    private int parent_position_id;
    @Column(name = "department_id")
    private int department_id;
    @Column(name = "chief_employee_id")
    private int chief_employee_id;

    public Position() {
    }

    public Position(String position_name, int parent_position_id, int department_id, int chief_employee_id) {
        this.position_name = position_name;
        this.parent_position_id = parent_position_id;
        this.department_id = department_id;
        this.chief_employee_id = chief_employee_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public int getParent_position_id() {
        return parent_position_id;
    }

    public void setParent_position_id(int parent_position_id) {
        this.parent_position_id = parent_position_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getChief_employee_id() {
        return chief_employee_id;
    }

    public void setChief_employee_id(int chief_employee_id) {
        this.chief_employee_id = chief_employee_id;
    }

    @Override
    public String toString() {
        return id + "/" + position_name + "/" + parent_position_id + "/" + department_id + " " + chief_employee_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (id != position.id) return false;
        if (parent_position_id != position.parent_position_id) return false;
        if (department_id != position.department_id) return false;
        if (chief_employee_id != position.chief_employee_id) return false;
        return Objects.equals(position_name, position.position_name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (position_name != null ? position_name.hashCode() : 0);
        result = 31 * result + parent_position_id;
        result = 31 * result + department_id;
        result = 31 * result + chief_employee_id;
        return result;
    }
}
