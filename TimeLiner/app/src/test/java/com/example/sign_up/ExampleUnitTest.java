package com.example.sign_up;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Example local unit test, which will execute on the development machine
 * (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class ExampleUnitTest {



    @Mock
    Login_View view;

    @Mock
    DatabaseReference database;


    @Mock
    Login_Module module;



    @Test
    public void test_shortPassword(){
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("123456");
        Login_Presenter presenter = new Login_Presenter(view,database,module);
        presenter.loginUser();
        verify(view).displayMessage("Password too short! Should be more than 8 characters.");
    }

    @Test
    public void test_EmptyUsername(){
        when(view.getUserName()).thenReturn("");
        when(view.getUserPassword()).thenReturn("12345678");
        Login_Presenter presenter = new Login_Presenter(view,database,module);
        presenter.loginUser();
        verify(view).displayMessage("Empty Username!");
    }

    @Test
    public void test_databaseFail(){
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("password");
        when(module.get_status()).thenReturn(3);
        Login_Presenter presenter = new Login_Presenter(view,database,module);
        presenter.loginUser();
        verify(view).displayMessage("Error getting data");
    }


    @Test
    public void test_UsernameNotExist() {

        when(view.getUserName()).thenReturn("student_DNE");// the username do not exist in database
        when(view.getUserPassword()).thenReturn("password");
        when(module.get_status()).thenReturn(2);
        Login_Presenter presenter = new Login_Presenter(view,database,module);
        presenter.loginUser();
        verify(view).displayMessage("Username does not exist!");
    }

    @Test
    public void test_WrongPassword() {
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("12345678"); // Actual password is password
        Login_Presenter presenter = new Login_Presenter(view, database,module);
        when(module.get_status()).thenReturn(1);
        presenter.loginUser();
        verify(view).displayMessage("Wrong Password!");
    }
}