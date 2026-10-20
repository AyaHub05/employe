package ma.ensias.employe.repository;
import ma.ensias.employe.entite.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployeRepo extends JpaRepository<Employe,Integer> {
}
