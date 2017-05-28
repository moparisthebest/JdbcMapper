package com.moparisthebest.jdbc;

/**
 * The MapperException class declares an unchecked exception that is thrown by the Controls
 * runtime under certain failure conditions.
 */
public class MapperException extends RuntimeException
{
	/**
	 * Default constructor.
	 */
	public MapperException() {
		super();
	}

	/**
	 * Constructs a MapperException object with the specified String as a message.
	 *
	 * @param message The message to use.
	 */
	public MapperException(String message)
	{
		super(message);
	}

	/**
	 * Constructs a MapperException with the specified cause.
	 * @param t the cause
	 */
	public MapperException(Throwable t) {
		super(t);
	}

	/**
	 * Constructs a MapperException object using the specified String as a message, and the
	 * specified Throwable as a nested exception.
	 *
	 * @param message The message to use.
	 * @param t The exception to nest within this exception.
	 */
	public MapperException(String message, Throwable t)
	{
		super(message + "[" + t + "]", t);
	}
}
