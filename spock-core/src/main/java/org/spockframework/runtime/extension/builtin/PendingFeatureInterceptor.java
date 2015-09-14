package org.spockframework.runtime.extension.builtin;

import org.junit.AssumptionViolatedException;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;

/**
 * @author Leonard Brünings
 */
class PendingFeatureInterceptor implements IMethodInterceptor {
  private final Class<? extends Throwable>[] handledExceptions;

  public PendingFeatureInterceptor(Class<? extends Throwable>[] handledExceptions) {

    this.handledExceptions = handledExceptions;
  }

  @Override
  public void intercept(IMethodInvocation invocation) throws Throwable {
    try {
      invocation.proceed();
    } catch (AssertionError e) {
      throw new AssumptionViolatedException("Feature not yet implemented correctly.");
    } catch (Exception e) {
      for (Class<? extends Throwable> exception : handledExceptions) {
        if(exception.isInstance(e)) {
          throw new AssumptionViolatedException("Feature not yet implemented correctly.");
        }
      }
      throw e;
    }
    throw new AssertionError("Feature is marked with @PendingFeature but passes unexpectedly");
  }
}
