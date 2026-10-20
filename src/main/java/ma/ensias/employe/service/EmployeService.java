package ma.ensias.employe.service;
import ma.ensias.employe.entite.Employe;
import ma.ensias.employe.response.EmployeResponse;
import ma.ensias.employe.repository.EmployeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

public class EmployeService {
    @Autowired
    private EmployeRepo employeRepo;
    @Autowired
    private ModelMapper mapper;
    public EmployeResponse getEmployeById(int id) {
        Optional<Employe> employe = employeRepo.findById(id);
        EmployeResponse employeResponse = mapper.map(employe, EmployeResponse.class);
        return employeResponse;
    }
}
