package ma.ensias.employe.configuration;
import ma.ensias.employe.service.EmployeServiceBackup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class EmployeConfig {
    @Bean
    public EmployeServiceBackup employeBean() {
        return new EmployeServiceBackup();
    }
    @Bean
    public ModelMapper modelMapperBean() {
        return new ModelMapper();
    }
}
