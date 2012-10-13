package br.rio.puc.inf.control.instruments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordTest {
	
	/**********
	 *  Password testing methods
	 * 
	 */
	
	// Test Password
	public static boolean testPassword (String password)
	{
		if(!findRepetition(password) && !findSequence(password) && testLenght(password))
			return true;
		return false;
	}
	
	// Test to find character repetitions within string
	private static boolean findRepetition(String string)
	{
		
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			String subString = string.substring(index, index + 1);
			Pattern pattern = Pattern.compile(subString);
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index + 1) {
					System.out.println("Caracter repetido");
					return true;
				}	
			}
			matcher.reset();
			
		}
		
		return false;
	}
	
	// Test to find sequences of numbers
	private static boolean findSequence(String string)
	{
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			int subString = Integer.parseInt(string.substring(index, index + 1));
			Pattern pattern = Pattern.compile(Integer.toString(subString + 1));
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index + 1) {
					System.out.println("Sequencia crescente encontrada");
					return true;
				}	
			}
			matcher.reset();
			
		}
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			int subString = Integer.parseInt(string.substring(index, index + 1));
			Pattern pattern = Pattern.compile(Integer.toString(subString - 1));
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index + 1) {
					System.out.println("Sequencia decrescente encontrada");
					return true;
				}	
			}
			matcher.reset();
			
		}
		return false;
	}
	
	// Test string length
	private static boolean testLenght(String string)
	{
		if(string.length() >= 8 && string.length() <= 10)
			return true;
		System.out.println("Senha deve ter entre 8 e 10 caracteres");
		return false;
	}

}
