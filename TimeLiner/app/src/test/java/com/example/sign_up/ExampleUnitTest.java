package com.example.sign_up;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Login_View view;

    @Mock
    DatabaseReference database;
    DatabaseReference database_real;

    @Mock
    Intent intent;

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
        // Get a reference to database
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