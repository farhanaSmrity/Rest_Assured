package testrunner;

import controller.User;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import setup.Setup;

import java.io.IOException;

public class UserTestRunner extends Setup {
    User user;
    @Test (priority = 1)
    public void doLogin() throws IOException, ConfigurationException {
         user = new User();
        user.callingLogInAPI();
        Assert.assertTrue(user.getMessage().contains("Login successfully"));
    }
    @Test (priority = 2)
    public void getUserById() throws IOException {
        user = new User();
       int id = user.getUserList(3);
        user.getUserById(id);
    }
    @Test (priority = 3)
    public void createUser() throws IOException, ConfigurationException {
        user = new User();
        user.createUser();
    }
}
