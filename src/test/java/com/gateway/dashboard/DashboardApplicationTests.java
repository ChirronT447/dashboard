package com.gateway.dashboard;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class DashboardApplicationTests {

	Calculator calculator = new Calculator();

	@Test
	void contextLoads() {
	}

	private class Calculator {

		/**
		 * The greatest common divisor is useful for reducing fractions to be in lowest terms.
		 * For example, gcd(42, 56) = 14
		 * @param number1
		 * @param number2
		 * @return greatestCommonDivisor
		 */
		public int greatestCommonDivisor(int number1, int number2) {
			if(number1 == 0 || number2 == 0) {
				return number1 + number2;
			} else {
				int biggerValue = Math.abs(Math.max(number1, number2));
				int smallerValue = Math.abs(Math.min(number1, number2));
				return greatestCommonDivisor(biggerValue % smallerValue, smallerValue);
			}
		}

		/**
		 * The least common multiple (LCM) of two numbers is the smallest number (not zero)
		 * that is a multiple of both / the smallest positive integer that is divisible by both.
		 * @param number1
		 * @param number2
		 * @return lowestCommonMultiple
		 */
		public int lowestCommonMultiple(int number1, int number2) {
			assertTrue(number1 != 0 && number2 != 0);
			return (number1 * number2) / greatestCommonDivisor(number1, number2);
		}

		// Function to convert the obtained fraction into it's simplest form
		public int[] simplify(int numerator, int denominator) {
			int commonFactor = greatestCommonDivisor(numerator, denominator);

			// Simplifying by dividing by a common factor
			numerator = numerator / commonFactor;
			denominator = denominator / commonFactor;

			System.out.println(numerator+"/"+denominator);
			return new int[]{numerator, denominator};
		}

		public int[] addFractions(int[] fraction1, int[] fraction2) {
			int commonDenom = lowestCommonMultiple(fraction1[1], fraction2[1]);

			// Changing the fractions to have same denominator
			// Numerator of the final fraction obtained
			int numerator = (fraction1[0])*(commonDenom/fraction1[1]) +
							(fraction2[0])*(commonDenom/fraction2[1]);

			// Calling function to convert final fraction into it's simplest form
			return simplify(numerator, commonDenom);
		}

	}

	@Test
	void testFractionCalc() {
		// (2 / 3) + (4 / 12) = (2 / 3) + (1 / 3) = 1 / 1
		int[] actual = calculator.addFractions(new int[]{2, 3}, new int[]{4, 12});
		assertArrayEquals(new int[]{1, 1}, actual);

		actual = calculator.addFractions(new int[]{1, 500}, new int[]{2, 1500});
		assertArrayEquals(new int[]{1, 300}, actual);
	}

	@Test
	void testGreatestCommonDivisor() {
		// gcd(42, 56) = 14
		assertEquals(14, calculator.greatestCommonDivisor(42, 56));
	}

	@Test
	void testLowestCommonMultiple() {
		// lcm(12, 18) is 36.
		assertEquals(36, calculator.lowestCommonMultiple(12, 18));
	}

}
