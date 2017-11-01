package com.example.randomscripturegeneratorapp;

/**
 * Created by nathan on 11/1/17.
 */


import org.junit.Test;

import static org.junit.Assert.*;

public class LoginUnitTest {
    @Test


    public void Login_works() throws Exception {
        Login test = new Login();
        assertEquals(true, test.login("test", "test"));
    }
}

