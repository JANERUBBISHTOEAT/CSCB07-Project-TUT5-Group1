package com.example.sign_up;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
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
    Login_View view_WrongName;

    @Mock
    DatabaseReference database;
    DatabaseReference database_WrongName;
    DatabaseReference database_wrongPassword;
    DatabaseReference database_fail;



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
    public void test_database(){
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("password");
        Login_Presenter presenter = new Login_Presenter(view,database_fail);
        presenter.loginUser();
        verify(view).displayMessage("Error getting data");
    }

    @Test
    public void test_UsernameNotExist() {
        when(view.getUserName()).thenReturn("student_DNE");
        when(view.getUserPassword()).thenReturn("12345678");
        Login_Presenter presenter = new Login_Presenter(view,database_WrongName);
        presenter.loginUser();
        verify(view).displayMessage("User not found!");
    }

    @Test
    public void test_WrongPassword() {
        when(view.getUserName()).thenReturn("student");
        when(view.getUserPassword()).thenReturn("12345678"); // Actual password is password
        Login_Presenter presenter = new Login_Presenter(view, database_wrongPassword);
        presenter.loginUser();
        verify(view).displayMessage("Wrong Password!");
    }
}