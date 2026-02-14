package com.example.pcsurmesure.models;

import static com.example.pcsurmesure.models.Command.validateQuantity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Assert;
//import org.junit.Test;

import org.junit.Before;
//import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
//@RunWith(AndroidJUnit4.class)
public class TestCommand {

    private Command command;
    private Component component1;
    private Component component2;
    private Component component3;
    private Map<Component, Integer> componentsMap;


    @Test
    public void testcostructor() {
        // Using the full constructor as per Component.java

        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, 5);
        componentsMap.put(component2, 7);

        command = new Command("requesterId123", componentsMap);
        assertNotNull(command.getDateDeCreation(), "Date of creation should not be null");
        assertNotNull(command.getIdCommande(), "Command ID should not be null");
        assertNull(command.getDateDeModification(), "Modification date should initially be null");
        command.setDateDeModification("2024-11-08 15:30:00");
        assertEquals("2024-11-08 15:30:00", command.getDateDeModification(), "Modification date should be updated");

    }
    @Test
    void testSecondConstructor() {
        String idCommand = "1234";
        String requesterId = "requester789";
        String dateDeCreation = "2024-11-10 15:30:00";
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");

        Map<Component, Integer> componentsQuantities = new HashMap<>();

        componentsQuantities.put(component1, 10);

        Command command = new Command(idCommand, requesterId, dateDeCreation, componentsQuantities);

        // Verify each field is initialized correctly
        assertEquals(idCommand, command.getIdCommande(), "L'ID de commande n'est pas initialisé correctement.");
        assertEquals(requesterId, command.getRequesterId(), "L'ID du demandeur n'est pas initialisé correctement.");
        assertEquals(dateDeCreation, command.getDateDeCreation(), "La date de création n'est pas initialisée correctement.");
        assertEquals(componentsQuantities, command.getComponentsQuantities(), "Les quantités des composants ne sont pas initialisées correctement.");
        assertNull(command.getDateDeModification(), "La date de modification devrait être nulle après l'initialisation.");
    }

    @Test
    public void TestCheckQuantitiesInsufisant(){
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, 20);
        componentsMap.put(component2, 25);

        assertEquals(false,validateQuantity(componentsMap));

    }
    @Test
    public void TestCheckQuantitiesSufisant() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, 5);
        componentsMap.put(component2, 4);

        assertEquals(true, validateQuantity(componentsMap));

    }
    @Test
    public void TestCheckQuantitiesZr() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, 0);
        componentsMap.put(component2, 0);

        assertEquals(false, validateQuantity(componentsMap));

    }
    @Test
    public void TestCheckQuantitiesNeg() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, -1);
        componentsMap.put(component2, 3);

        assertEquals(false, validateQuantity(componentsMap));

    }
    @Test
    public void TestCheckQuantitiesDiff() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");

        componentsMap = new HashMap<>();
        componentsMap.put(component1, 0);
        componentsMap.put(component2, -50);

        assertEquals(false, validateQuantity(componentsMap));

    }
    @Test
    void testSetDateDeModification() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");
        componentsMap = new HashMap<>();
        componentsMap.put(component1, 5);
        componentsMap.put(component2, 7);
        command = new Command("requesterId123", componentsMap);
        String newDate = "2024-11-10 12:00:00";
        command.setDateDeModification(newDate);
        assertEquals(newDate, command.getDateDeModification(), "La date de modification n'est pas correcte.");
    }

    @Test
    void testSetRequesterId() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");
        componentsMap = new HashMap<>();
        componentsMap.put(component1, 5);
        componentsMap.put(component2, 7);
        command = new Command("requesterId123", componentsMap);

        String newRequesterId = "newRequester456";

        command.setRequesterId(newRequesterId);
        assertEquals(newRequesterId, command.getRequesterId(), "L'ID du demandeur n'est pas correct.");
    }

    @Test
    void testSetComponentsQuantities() {
        component1 = new Component("Type1", "SubTypeA", "Component 1", 10, "Sample Comment 1");
        component2 = new Component("Type2", "SubTypeB", "Component 2", 15, "Sample Comment 2");
        component3 = new Component("Type3", "SubTypeC", "Component 3", 20, "Sample Comment 3");
        componentsMap = new HashMap<>();
        componentsMap.put(component1, 5);
        componentsMap.put(component2, 7);
        command = new Command("requesterId123", componentsMap);

        Map<Component, Integer> newComponents = new HashMap<>();
        newComponents.put(component3, 8);
        command.setComponentsQuantities(newComponents);
        assertEquals(newComponents, command.getComponentsQuantities(), "Les quantités des composants ne sont pas correctes.");
    }

}