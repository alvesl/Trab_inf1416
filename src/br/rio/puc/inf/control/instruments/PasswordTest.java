package br.rio.puc.inf.control.instruments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordTest {
	
	/**********
	 *  Password testing methods
	 * 
	 */
	
	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		while(true) {
			try {
				System.out.println("Senha:");
				String password = in.readLine();
				if(testPassword(password))
					System.out.println("Senha forte");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Test Password
	public static boolean testPassword (String password)
	{
		boolean b1 = findRepetition(password);
		boolean b2 = findSequence(password);
		boolean b3 = testLenght(password);
		if(!b1 && !b2 && b3)
			return true;
		return false;
	}
	
	// Test to find character repetitions within string
	public static boolean findRepetition(String string)
	{
		
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			String subString = string.substring(index, index + 1);
			String aux = subString + subString + subString;
			Pattern pattern = Pattern.compile(aux);
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index ) {
					//System.out.println("Caracter repetido");
					return true;
				}	
			}
			matcher.reset();
			
		}
		
		return false;
	}
	
	// Test to find sequences of numbers
	public static boolean findSequence(String string)
	{
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			int subString = Integer.parseInt(string.substring(index, index + 1));
			Pattern pattern = Pattern.compile(Integer.toString(subString + 1) + Integer.toString(subString + 2));
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index + 1) {
					//System.out.println("Sequencia crescente encontrada");
					return true;
				}	
			}
			matcher.reset();
			
		}
		for (int index = 0 ; index < string.length() - 1 ; index++)
		{
			int subString = Integer.parseInt(string.substring(index, index + 1));
			Pattern pattern = Pattern.compile(Integer.toString(subString - 1) + Integer.toString(subString - 2));
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()) {
				if(matcher.start() == index + 1) {
					//System.out.println("Sequencia decrescente encontrada");
					return true;
				}	
			}
			matcher.reset();
			
		}
		return false;
	}
	
	// Test string length
	public static boolean testLenght(String string)
	{
		if(string.length() >= 8 && string.length() <= 10)
			return true;
		//System.out.println("Senha deve ter entre 8 e 10 caracteres");
		return false;
	}

}
