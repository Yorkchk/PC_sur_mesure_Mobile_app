package com.example.pcsurmesure.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

//import org.junit.Test;

//import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
//@RunWith(AndroidJUnit4.class)
public class TestAssembler {

    private Command command;
    private Component component1;
    private Component component2;

    private Assembler assembler;

    private Map<Component, Integer> componentsMap;


    @Test
    public void testcostructor() {
        // Using the full constructor as per Component.java


        assembler = new Assembler("username", "firstName", "lastName", "email", "password");


        assertNotNull(assembler.getFirstName(), "Date of creation should not be null");
        assertNotNull(assembler.getLastName(), "Command ID should not be null");
        assertNotNull(assembler.getEmail(), "Modification date should initially be null");
        assertNotNull(assembler.getRole(), "Modification date should initially be null");
        assembler.setEmail("email2");
        assertEquals("email2", assembler.getEmail(), "Modification date should be updated");

    }
    @Test
    public void testemptyconstructor() {
            Assembler assembler = new Assembler();

            // Assert: Verify the object is not null
            assertNotNull(assembler, "Assembler instance should not be null.");

            // (Optional) Add any checks to ensure the object initializes correctly
            // Example: Check if default values of attributes are as expected
            // assertEquals("Expected default value", assembler.getSomeProperty());
        }
    @Test
    public void testSetapprCommand() {
        // Arrange: Create a test command
        Command testCommand = new Command("testRequester", new HashMap<>());
        testCommand.setCommandId("testCommandId");
        testCommand.setStatus(Command.Status.PENDING);

        // Act: Simulate approving the command
        testCommand.setStatus(Command.Status.APPROVED);

        // Assert: Verify that the command's status is now APPROVED
        assertEquals(Command.Status.APPROVED, testCommand.getStatus());
    }
    @Test
    public void testSetrejCommand() {
        // Arrange: Create a test command
        Command testCommand = new Command("testRequester", new HashMap<>());
        testCommand.setCommandId("testCommandId");
        testCommand.setStatus(Command.Status.PENDING);

        // Act: Simulate rejecting the command
        testCommand.setStatus(Command.Status.REJECTED);

        // Assert: Verify that the command's status is now REJECTED
        assertEquals(Command.Status.REJECTED, testCommand.getStatus());
    }
}


