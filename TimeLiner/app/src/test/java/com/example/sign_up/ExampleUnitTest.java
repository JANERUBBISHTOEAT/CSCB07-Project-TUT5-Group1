package com.example.sign_up;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;

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
    private DatabaseReference mockedDatabaseReference;

    @Before
    public void before() {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    }

    @Mock
    Login_View view;

    @Mock
    DatabaseReference database;
    DatabaseReference database_real;



    @Test
    public void test_shortPassword(){
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("123456");
        Login_Presenter presenter = new Login_Presenter(view,database);
        presenter.loginUser();
        verify(view).displayMessage("Password too short! Should be more than 8 characters.");
    }

    @Test
    public void test_EmptyUsername(){
        when(view.getUserName()).thenReturn("");
        when(view.getUserPassword()).thenReturn("12345678");
        Login_Presenter presenter = new Login_Presenter(view,database);
        presenter.loginUser();
        verify(view).displayMessage("Empty Username!");
    }


    @Test
    public void test_UsernameNotExist() {
         //Get a reference to database
        database_real = FirebaseDatabase.getInstance().getReference();
        when(view.getUserName()).thenReturn("student_DNE");
        when(view.getUserPassword()).thenReturn("12345678");
        Login_Presenter presenter = new Login_Presenter(view, database_real);
        presenter.loginUser();
        verify(view).displayMessage("Username does not exist!");
    }

    @Test
    public void test_WrongPassword() {
        database_real = FirebaseDatabase.getInstance().getReference();
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("12345678"); // Actual password is password
        Login_Presenter presenter = new Login_Presenter(view, database_real);
        presenter.loginUser();
        verify(view).displayMessage("Wrong Password!");
    }
}