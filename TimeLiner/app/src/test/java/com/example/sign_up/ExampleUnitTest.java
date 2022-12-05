package com.example.sign_up;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.firebase.database.DatabaseReference;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Login_View view;

    @Mock
    DatabaseReference database;

    @Test


    public void test_Password(){
        when(view.getUserPassword()).thenReturn("123");
        Login_Presenter presenter = new Login_Presenter(view,database);
        presenter.loginUser();
        verify(view).displayMessage("Password too short! Should be more than 8 characters.");
    }


    @Test
    public void test_EmptyUsername(){
        when(view.getUserName()).thenReturn("");
        Login_Presenter presenter = new Login_Presenter(view,database);
        presenter.loginUser();
        verify(view).displayMessage("Empty Username!");
    }
}