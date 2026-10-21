package ma.ensias.employe.controller;
import ma.ensias.employe.response.EmployeResponse;
import ma.ensias.employe.service.EmployeServiceBackup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeController {

    @Autowired
    private EmployeServiceBackup employeService;

    @GetMapping("/employes/{id}")
    public  ResponseEntity<EmployeResponse> getEmployeDetails(@PathVariable("id") int id) {
        EmployeResponse employe = employeService.getEmployeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employe);
    }
}
