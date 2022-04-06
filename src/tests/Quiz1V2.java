package tests;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

import org.junit.Assert;
import org.junit.Test;

import model.world.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class Quiz1V2 {

	String gearClassPath = "model.world.Gear";
	String humanClassPath = "model.world.Human";
	String leaderClassPath = "model.world.Leader";
	String assassinClassPath = "model.world.Assassin";
	String conditionClassPath = "model.world.Condition";
	String gearTypeClassPath = "model.world.GearType";

	@Test(timeout = 100)
	public void testEnumGearType() throws ClassNotFoundException {
		testIsEnum(Class.forName(gearTypeClassPath));
	}

	@Test(timeout = 100)
	public void testEnumGearTypeValues() throws ClassNotFoundException {
		try {
			Enum.valueOf((Class<Enum>) Class.forName(gearTypeClassPath), "WEAPON");
		} catch (IllegalArgumentException e) {
			fail("Gear Type can be WEAPON");
		}

		try {
			Enum.valueOf((Class<Enum>) Class.forName(gearTypeClassPath), "SHIELDING");
		} catch (IllegalArgumentException e) {
			fail("Gear Type can be SHIELDING");
		}

	}

	@Test(timeout = 100)
	public void testClassIsSubclassAssassin() throws Exception {
		testClassIsSubclass(Class.forName(assassinClassPath), Class.forName(humanClassPath));
	}

	@Test(timeout = 100)
	public void testClassIsSubclassLeader() throws Exception {
		testClassIsSubclass(Class.forName(leaderClassPath), Class.forName(humanClassPath));
	}

	@Test
	public void testConstructorGear() throws Exception {
		Class.forName(gearClassPath).getConstructor(String.class, int.class, Class.forName(gearTypeClassPath));
	}

	@Test
	public void testConstructorHuman() throws Exception {
		Class.forName(humanClassPath).getConstructor(int.class);
	}

	@Test
	public void testConstructorLeader() throws Exception {
		Class.forName(leaderClassPath).getConstructor(int.class);
	}

	@Test
	public void testConstructorAssassin() throws Exception {
		Class.forName(assassinClassPath).getConstructor(int.class);
	}

	@Test(timeout = 100)
	public void testConstructorGearNameInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(gearClassPath).getConstructor(String.class, int.class,
				Class.forName(gearTypeClassPath));

		int randomName = (int) (Math.random() * 10) + 1;
		int randomDurability = (int) (Math.random() * 10) + 1;
		int randomGearType = (int) (Math.random() * 2);

		Class<?> gearType = Class.forName(gearTypeClassPath);

		Object b = constructor.newInstance("Name_" + randomName, randomDurability,
				gearType.getEnumConstants()[randomGearType]);
		String[] varNames = { "name"};
		Object[] varValues = { "Name_" + randomName};
		testConstructorInitialization(b, varNames, varValues);
	}
	
	@Test(timeout = 100)
	public void testConstructorGearDurabilityInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(gearClassPath).getConstructor(String.class, int.class,
				Class.forName(gearTypeClassPath));

		int randomName = (int) (Math.random() * 10) + 1;
		int randomDurability = (int) (Math.random() * 10) + 1;
		int randomGearType = (int) (Math.random() * 2);

		Class<?> gearType = Class.forName(gearTypeClassPath);

		Object b = constructor.newInstance("Name_" + randomName, randomDurability,
				gearType.getEnumConstants()[randomGearType]);
		String[] varNames = {"durability"};
		Object[] varValues = {randomDurability};
		testConstructorInitialization(b, varNames, varValues);
	}
	
	@Test(timeout = 100)
	public void testConstructorGearTypeInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(gearClassPath).getConstructor(String.class, int.class,
				Class.forName(gearTypeClassPath));

		int randomName = (int) (Math.random() * 10) + 1;
		int randomDurability = (int) (Math.random() * 10) + 1;
		int randomGearType = (int) (Math.random() * 2);

		Class<?> gearType = Class.forName(gearTypeClassPath);

		Object b = constructor.newInstance("Name_" + randomName, randomDurability,
				gearType.getEnumConstants()[randomGearType]);
		String[] varNames = {"type" };
		Object[] varValues = {gearType.getEnumConstants()[randomGearType] };
		testConstructorInitialization(b, varNames, varValues);
	}

	@Test(timeout = 100)
	public void testConstructorHumanMaxInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = { "maxHealth"};
		Object[] varValues = { randomMaxHealth};
		testConstructorInitialization(b, varNames, varValues);
	}
	
	@Test(timeout = 100)
	public void testConstructorHumanCurrentInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = {"currentHealth"};
		Object[] varValues = {randomMaxHealth};
		testConstructorInitialization(b, varNames, varValues);
	}
	
	@Test(timeout = 100)
	public void testConstructorHumanGearsInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = {"gears" };
		Object[] varValues = {new ArrayList<Object>() };
		testConstructorInitialization(b, varNames, varValues);
	}

	@Test(timeout = 100)
	public void testConstructorLeaderInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(leaderClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = { "maxHealth", "currentHealth", "gears" };
		Object[] varValues = { randomMaxHealth, randomMaxHealth, new ArrayList<Object>() };
		testConstructorInitialization(b, varNames, varValues);
	}

	@Test(timeout = 100)
	public void testConstructorAssassinGeneralInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(assassinClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = {"maxHealth", "currentHealth", "gears"};
		Object[] varValues = { randomMaxHealth, randomMaxHealth, new ArrayList<Object>()};
		testConstructorInitialization(b, varNames, varValues);
	}
	
	@Test(timeout = 100)
	public void testConstructorAssassinVisibleInitialization() throws Exception {
		Constructor<?> constructor = Class.forName(assassinClassPath).getConstructor(int.class);

		int randomMaxHealth = (int) (Math.random() * 10) + 1;

		Object b = constructor.newInstance(randomMaxHealth);
		String[] varNames = { "maxHealth", "currentHealth", "gears", "visible" };
		Object[] varValues = { randomMaxHealth, randomMaxHealth, new ArrayList<Object>(), true };
		testConstructorInitialization(b, varNames, varValues);
	}

	/**********************************************************************************************/

	@Test
	public void testInstanceVariablesGearName() throws Exception {
		testInstanceVariablesArePresent(Class.forName(gearClassPath), "name", true);
		testInstanceVariableIsPrivate(Class.forName(gearClassPath), "name");
		testInstanceVariableType(Class.forName(gearClassPath), "name", String.class);
	}

	@Test
	public void testInstanceVariablesGearDurability() throws Exception {
		testInstanceVariablesArePresent(Class.forName(gearClassPath), "durability", true);
		testInstanceVariableIsPrivate(Class.forName(gearClassPath), "durability");
		testInstanceVariableType(Class.forName(gearClassPath), "durability", int.class);
	}

	@Test
	public void testInstanceVariablesGearType() throws Exception {
		testInstanceVariablesArePresent(Class.forName(gearClassPath), "type", true);
		testInstanceVariableIsPrivate(Class.forName(gearClassPath), "type");
		testInstanceVariableType(Class.forName(gearClassPath), "type", Class.forName(gearTypeClassPath));
	}

	@Test
	public void testInstanceVariablesHumanMaxHealth() throws Exception {
		testInstanceVariablesArePresent(Class.forName(humanClassPath), "maxHealth", true);
		testInstanceVariableIsPrivate(Class.forName(humanClassPath), "maxHealth");
		testInstanceVariableType(Class.forName(humanClassPath), "maxHealth", int.class);
	}

	@Test
	public void testInstanceVariablesHumanCurrentHealth() throws Exception {
		testInstanceVariablesArePresent(Class.forName(humanClassPath), "currentHealth", true);
		testInstanceVariableIsPrivate(Class.forName(humanClassPath), "currentHealth");
		testInstanceVariableType(Class.forName(humanClassPath), "currentHealth", int.class);
	}

	@Test
	public void testInstanceVariablesHumanGears() throws Exception {
		testInstanceVariablesArePresent(Class.forName(humanClassPath), "gears", true);
		testInstanceVariableIsPrivate(Class.forName(humanClassPath), "gears");
		testInstanceVariableType(Class.forName(humanClassPath), "gears", ArrayList.class);
	}

	@Test
	public void testInstanceVariablesAssassinVisible() throws Exception {
		testInstanceVariablesArePresent(Class.forName(assassinClassPath), "visible", true);
		testInstanceVariableIsPrivate(Class.forName(assassinClassPath), "visible");
		testInstanceVariableType(Class.forName(assassinClassPath), "visible", boolean.class);
	}

	/**********************************************************************************************/
	@Test(timeout = 100)
	public void testInstanceVariableGearNameGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(gearClassPath), "getName", String.class);
	}

	@Test(timeout = 100)
	public void testInstanceVariableGearDurabilityGetter() throws Exception {
		testGetterMethodNoExistInClass(Class.forName(gearClassPath), "getDurability", int.class, false);
	}

	@Test(timeout = 100)
	public void testInstanceVariableGearTypeGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(gearClassPath), "getType", Class.forName(gearTypeClassPath));
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanGearsGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(humanClassPath), "getGears", ArrayList.class);
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanCurrentHealthGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(humanClassPath), "getCurrentHealth", int.class);
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanMaxHealthGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(humanClassPath), "getMaxHealth", int.class);
	}

	@Test(timeout = 100)
	public void testInstanceVariableAssassinIsVisibleGetter() throws Exception {
		testGetterMethodExistsInClass(Class.forName(assassinClassPath), "isVisible", boolean.class);
	}

	/**********************************************************************************************/

	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableNameInClassGear() throws Exception {
		int r = (int) (Math.random() * 500);
		String string00 = "wq8" + r;

		int randomGearType = (int) (Math.random() * 2);
		int randomDurability = (int) (Math.random() * 10) + 1;

		Class<?> gearType = Class.forName(gearTypeClassPath);

		int int88 = 88;
		Object gear = Class
				.forName(gearClassPath).getConstructor(String.class, int.class, gearType).newInstance(string00, randomDurability, gearType.getEnumConstants()[randomGearType]);

		testGetterLogic(gear, "name", string00);
	}
	
	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableGearTypeInClassGear() throws Exception {
		int r = (int) (Math.random() * 500);
		String string00 = "wq8" + r;

		int randomGearType = (int) (Math.random() * 2);
		int randomDurability = (int) (Math.random() * 10) + 1;

		Class<?> gearType = Class.forName(gearTypeClassPath);

		int int88 = 88;
		Object gear = Class
				.forName(gearClassPath).getConstructor(String.class, int.class, gearType).newInstance(string00, randomDurability, gearType.getEnumConstants()[randomGearType]);

		testGetterLogic(gear, "type", gearType.getEnumConstants()[randomGearType]);
	}
	
	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableMaxHealthInClassHuman() throws Exception {
		int r = (int) (Math.random() * 500);

		Object human = Class
				.forName(humanClassPath).getConstructor(int.class).newInstance(r);

		testGetterLogic(human, "maxHealth", r);
	}
	
	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableCurrentHealthInClassHuman() throws Exception {
		int r = (int) (Math.random() * 500);

		Object human = Class
				.forName(humanClassPath).getConstructor(int.class).newInstance(r);

		testGetterLogic(human, "currentHealth", r);
	}
	
	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableGearsInClassHuman() throws Exception {
		int r = (int) (Math.random() * 500);

		Object human = Class
				.forName(humanClassPath).getConstructor(int.class).newInstance(r);

		testGetterLogic(human, "gears", new ArrayList<>());
	}
	
	@Test(timeout = 1000)
	public void testGetterLogicForInstanceVariableVisibleInClassAssassin() throws Exception {
		int r = (int) (Math.random() * 500);

		Object human = Class
				.forName(assassinClassPath).getConstructor(int.class).newInstance(r);

		testGetterLogic(human, "visible", true);
	}

	/**********************************************************************************************/
	@Test(timeout = 100)
	public void testInstanceVariableGearNameSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(gearClassPath), "setName", String.class, false);
	}

	@Test(timeout = 100)
	public void testInstanceVariableGearDurabilitySetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(gearClassPath), "setDurability", int.class, true);
	}

	@Test(timeout = 100)
	public void testInstanceVariableGearTypeSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(gearClassPath), "setType", Class.forName(gearTypeClassPath), false);
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanCurrentHealthSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(humanClassPath), "setCurrentHealth", int.class, true);
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanMaxHealthSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(humanClassPath), "setMaxHealth", int.class, false);
	}

	@Test(timeout = 100)
	public void testInstanceVariableHumanGearsSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(humanClassPath), "setGears", ArrayList.class, false);
	}

	@Test(timeout = 100)
	public void testInstanceVariableAssassinVisibleSetter() throws Exception {
		testSetterMethodExistsInClass(Class.forName(assassinClassPath), "setVisible", boolean.class, true);
	}

	/**********************************************************************************************/
	@Test(timeout = 1000)
	public void testSetterLogicForInstanceVariableDurabilityInClassGear() throws Exception {
		int r = (int) (Math.random() * 500);
		String string00 = "wq8" + r;

		int randomGearType = (int) (Math.random() * 2);
		int randomDurability = (int) (Math.random() * 10) + 1;

		Class<?> gearType = Class.forName(gearTypeClassPath);

		Object gear = Class
				.forName(gearClassPath).getConstructor(String.class, int.class, gearType).newInstance(string00, randomDurability, gearType.getEnumConstants()[randomGearType]);


		testSetterLogic(gear, "durability", randomDurability, randomDurability,int.class);
	}
	
	@Test(timeout = 100)
	public void testHumanCurrentHealthSetterLogicGeneral1() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);
		
		int randomHP = (int) (Math.random() * 1000);
		int randCurrent = randomHP + (int)(Math.random()*10)+1;
		Object b = constructor.newInstance(randomHP);
		testSetterLogic(b, "currentHealth", -1, 0, int.class);
	}
	
	@Test(timeout = 100)
	public void testHumanCurrentHealthSetterLogicGeneral2() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);
		
		int randomHP = (int) (Math.random() * 1000);
		int randCurrent = randomHP + (int)(Math.random()*10)+1;
		Object b = constructor.newInstance(randomHP);
		testSetterLogic(b, "currentHealth", randomHP, randomHP, int.class);
	}
	
	@Test(timeout = 100)
	public void testHumanCurrentHealthSetterLogicGeneral3() throws Exception {
		Constructor<?> constructor = Class.forName(humanClassPath).getConstructor(int.class);
		
		int randomHP = (int) (Math.random() * 1000);
		int randCurrent = randomHP + (int)(Math.random()*10)+1;
		Object b = constructor.newInstance(randomHP);
		testSetterLogic(b, "currentHealth", randCurrent, randomHP, int.class);
	}
	
	@Test(timeout = 1000)
	public void testSetterLogicForInstanceVariableVisibleInClassAssassin() throws Exception {
		int r = (int) (Math.random() * 500);

		Object human = Class
				.forName(assassinClassPath).getConstructor(int.class).newInstance(r);


		testSetterLogic(human, "visible", true, true, boolean.class);
	}
	/**********************************************************************************************/
	
	private void testInstanceVariablesArePresent(Class aClass, String varName, boolean implementedVar)
			throws SecurityException {

		boolean thrown = false;
		try {
			aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		if (implementedVar) {
			assertFalse("There should be " + varName + " instance variable in class " + aClass.getName(), thrown);
		} else {
			assertTrue("There should not be " + varName + " instance variable in class " + aClass.getName()
					+ ", it should be inherited from the super class", thrown);
		}
	}

	private void testInstanceVariableIsPrivate(Class aClass, String varName)
			throws NoSuchFieldException, SecurityException {
		Field f = aClass.getDeclaredField(varName);
		boolean b = 2 == f.getModifiers();
		assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
				+ " should not be accessed outside that class.", b);
	}

	private void testInstanceVariableType(Class aClass, String varName, Class expected)
			throws NoSuchFieldException, SecurityException {
		Field f = aClass.getDeclaredField(varName);
		assertEquals(varName + " instance variable in class " + aClass.getName() + " should be of type ", expected,
				f.getType());
	}

	private void testGetterMethodExistsInClass(Class aClass, String methodName, Class returnedType) {
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			found = false;
		}
		String varName;
		if (methodName.substring(0, 2).equals("is")) {
			varName = methodName.substring(2).toLowerCase();
		} else {
			varName = methodName.substring(3).toLowerCase();
		}

		assertTrue("The " + varName + " instance variable in class " + aClass.getName() + " is a READ variable.",
				found);
		assertTrue("incorrect return type for " + methodName + " method in " + aClass.getName() + " class.",
				m.getReturnType().isAssignableFrom(returnedType));
	}
	
	private void testGetterMethodNoExistInClass(Class aClass, String methodName, Class returnedType, boolean writeVar) {
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			found = false;
		}
		Method[] methods = aClass.getDeclaredMethods();
		String varName;
		if (methodName.substring(0, 2).equals("is")) {
			varName = methodName.substring(2).toLowerCase();
		} else {
			varName = methodName.substring(3).toLowerCase();
		}

		
		if (writeVar) {
			assertTrue("The " + varName + " instance variable in class " + aClass.getName() + " is a WRITE variable.",
					containsMethodName(methods, methodName));
		} else {
			assertFalse(
					"The " + varName + " instance variable in class " + aClass.getName() + " is a WRITE ONLY variable.",
					containsMethodName(methods, methodName));
			return;
		}
	}

	private void testSetterMethodExistsInClass(Class aClass, String methodName, Class inputType,
			boolean writeVariable) {

		Method[] methods = aClass.getDeclaredMethods();
		String varName = methodName.substring(3).toLowerCase();
		if (writeVariable) {
			assertTrue("The " + varName + " instance variable in class " + aClass.getName() + " is a WRITE variable.",
					containsMethodName(methods, methodName));
		} else {
			assertFalse(
					"The " + varName + " instance variable in class " + aClass.getName() + " is a READ ONLY variable.",
					containsMethodName(methods, methodName));
			return;
		}
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName, inputType);
		} catch (NoSuchMethodException e) {
			found = false;
		}

		assertTrue(aClass.getName() + " class should have " + methodName + " method that takes one "
				+ inputType.getSimpleName() + " parameter", found);

		assertTrue("incorrect return type for " + methodName + " method in " + aClass.getName() + ".",
				m.getReturnType().equals(Void.TYPE));

	}

	private void testClassIsSubClass(Class subClass, Class superClass) {
		assertEquals(subClass.getName() + " class should inherit from " + superClass.getName() + ".", superClass,
				subClass.getSuperclass());
	}

	private static boolean containsMethodName(Method[] methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals(name))
				return true;
		}
		return false;
	}

	public static boolean containsMethod(Class c, String name, Class[] parameters) {
		try {
			c.getDeclaredMethod(name, parameters);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private void testIsEnum(Class aClass) {
		assertEquals(aClass.getName() + " should be an Enum", true, aClass.isEnum());
	}

	private void testClassIsSubclass(Class subClass, Class superClass) {
		assertEquals(subClass.getSimpleName() + " class should be a subclass from " + superClass.getSimpleName() + ".",
				superClass, subClass.getSuperclass());
	}

	private void testConstructorInitialization(Object createdObject, String[] names, Object[] values)
			throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < names.length; i++) {

			Field f = null;
			Class curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];

			while (f == null) {

				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}

			}

			f.setAccessible(true);
			if (currName.equals("currentHealth")) {

				assertEquals(
						"The constructor of the " + createdObject.getClass().getSimpleName()
								+ " class should initialize the instance variable \"" + currName
								+ "\" correctly. It should be equals to the max initially.",
						currValue, f.get(createdObject));
			} else {
				assertEquals(
						"The constructor of the " + createdObject.getClass().getSimpleName()
								+ " class should initialize the instance variable \"" + currName + "\" correctly.",
						currValue, f.get(createdObject));
			}
		}

	}

	private void testGetterLogic(Object createdObject, String name, Object value) throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
						+ name + "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);
		f.set(createdObject, value);

		Character c = name.charAt(0);

		String methodName = "get" + Character.toUpperCase(c) + name.substring(1, name.length());

		if (value.getClass().equals(Boolean.class))
			methodName = "is" + Character.toUpperCase(c) + name.substring(1, name.length());

		Method m = createdObject.getClass().getMethod(methodName);
		assertEquals(
				"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
						+ " should return the correct value of variable \"" + name + "\".",
				value, m.invoke(createdObject));

	}
	
	private void testSetterLogic(Object createdObject, String name, Object setValue, Object expectedValue, Class type)
			throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
						+ name + "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);

		Character c = name.charAt(0);
		String methodName = "set" + Character.toUpperCase(c) + name.substring(1, name.length());
		Method m = createdObject.getClass().getMethod(methodName, type);
		m.invoke(createdObject, setValue);

		assertEquals(
				"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
						+ " should set the correct value of variable \"" + name + "\".",
				expectedValue, f.get(createdObject));

	}
	
}
