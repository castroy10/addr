package ru.castroy10.addr.repo;

import ru.castroy10.addr.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"position", "department"})
    List<Employee> findAll();

    @Query("from Employee e " + "where " + "   lower(concat(e.lastName, ' ', e.middleName, ' ', e.firstName)) like concat('%', :name, '%')")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"position", "department"})
    List<Employee> findByName(@Param("name") String name); //метод использует sql запрос, который выше.

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"position", "department"})
    List<Employee> findByDepartmentId(int id);

}
