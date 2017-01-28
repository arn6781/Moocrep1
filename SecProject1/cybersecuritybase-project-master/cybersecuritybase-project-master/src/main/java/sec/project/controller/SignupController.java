package sec.project.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    private List<String> list2;
    
    public void initializeList2() {
        this.list2 = new ArrayList<>();
    }
    
    public void clearList2() {
        this.list2.clear();
    }
    
    private int id2;
    
    public int getid2() {
        return this.id2;
    }

    public void setid2(int id3) {
        this.id2 = id3;
        this.list2.add(Integer.toString(id3));
    }
    
    private String name2;
    
    public String getname2() {
        return this.name2;
    }

    public void setname2(String name3) {
        this.name2 = name3;
        this.list2.add(name3);
    }
    
    private String password2;
    
    public String getpassword2() {
        return this.password2;
    }

    public void setpassword2(String password3) {
        this.password2 = password3;
        this.list2.add(password3);
    }
    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String name, @RequestParam String password) {
        
        clearList2();
        
        int rightCredentials = 0;
        
        try
        {   
            String databaseAddress = "jdbc:h2:file:./database";
        
            Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
            
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM UserAccounts2 WHERE name = '" + name + "' AND password = '" + password + "'");
            
            while (resultSet.next())
            {
                setid2(resultSet.getInt("id"));
                setname2(resultSet.getString("name"));
                setpassword2(resultSet.getString("password"));
                
                model.addAttribute("list2", list2);
                
                rightCredentials = 1;
                
                break;
            }

            resultSet.close();
            connection.close();
        }
        
        catch (Throwable t)
        {
            System.err.println(t.getMessage());
        }
        
        if (rightCredentials == 1) {
            return "done";
        }
        else
        {
            return "form";
        }

    }
    
    @RequestMapping("/done")
    public String submitFormPassword(Model model, @RequestParam String typedName, @RequestParam String typedPassword) {
        
        clearList2();
        
        try
        {   
            String databaseAddress = "jdbc:h2:file:./database";
        
            Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
            
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE UserAccounts2 SET password = '" + typedPassword + "' WHERE name = '" + typedName + "'");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            setid2(getid2());
            setname2(getname2());
            setpassword2(typedPassword);

            connection.close();
        }
        
        catch (Throwable t)
        {
            System.err.println(t.getMessage());
        }
        
        model.addAttribute("list2", list2);
        
        return "done";
    }
}
