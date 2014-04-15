package miscJpf.bug21124;

import java.lang.reflect.UndeclaredThrowableException;
import java.lang.IllegalArgumentException;
import java.lang.UnknownError;
import java.lang.ClassNotFoundException;
import java.lang.IndexOutOfBoundsException;

@org.apache.beehive.controls.api.bean.ControlImplementation(isTransient=true)
public class CtrlImpl implements Ctrl
   {
   public void throwWrappedChecked()
      {
      System.out.println(">>> CtrlImpl.throwWrappedChecked");
      String str = "Test - This is a wrapped, checked exception.";
      ClassNotFoundException except = new ClassNotFoundException(str);
      throw new UndeclaredThrowableException(except);
      }

   public void throwWrappedRuntime()
      {
      System.out.println(">>> CtrlImpl.throwWrappedRuntime");
      String str = "Test - This is a wrapped, runtime exception.";
      IllegalArgumentException except = new IllegalArgumentException(str);
      throw new UndeclaredThrowableException(except);
      }

   public void throwUnwrappedRuntime()
      {
         System.out.println(">>> CtrlImpl.throwUnwrappedRuntime");
         String str = "Test - This is an unwrapped, runtime exception.";
         throw new IllegalArgumentException(str);
      }

   public void throwWrappedError()
      {
      System.out.println(">>> CtrlImpl.throwWrappedError");
      String str = "Test - This is a wrapped, error.";
      UnknownError except = new UnknownError(str);
      throw new UndeclaredThrowableException(except);
      }

   public void throwUnwrappedError()
      {
      System.out.println(">>> CtrlImpl.throwUnwrappedError");
      String str = "Test - This is an unwrapped, error.";
      throw new UnknownError(str);
      }

   public void throwWrappedUnhandled()
      {
      System.out.println(">>> CtrlImpl.throwWrappedUnhandled");
      String str = "Test - This is a wrapped, unhandled, runtime exception.";
      IndexOutOfBoundsException except = new IndexOutOfBoundsException(str);
      throw new UndeclaredThrowableException(except);
      }

   public void throwUnwrappedUnhandled()
      {
      System.out.println(">>> CtrlImpl.throwWrappedUnhandled");
      String str = "Test - This is an unwrapped, unhandled, runtime exception.";
      throw new IndexOutOfBoundsException(str);
      }
   }
