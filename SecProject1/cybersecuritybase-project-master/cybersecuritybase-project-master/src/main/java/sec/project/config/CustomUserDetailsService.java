package sec.project.config;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.annotation.PostConstruct;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.controller.SignupController;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private SignupController signupController;
    
    @PostConstruct
    public void init() {

        signupController.initializeList2();
        
        try
        {
        String databaseAddress = "jdbc:h2:file:./database";

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        
        try
        {
            try
            {
                RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            }
            catch (Throwable t)
            {
                System.err.println(t.getMessage());
            }
            
            try
            {
                RunScript.execute(connection, new FileReader("sql/database-import.sql"));
            }
            catch (Throwable t)
            {
                System.err.println(t.getMessage());
            }
        }
        catch (Throwable t)
        {
            System.err.println(t.getMessage());
        }

       connection.close();
       }
       catch (Throwable t2) {
            System.err.println(t2.getMessage());
       }
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }
}
