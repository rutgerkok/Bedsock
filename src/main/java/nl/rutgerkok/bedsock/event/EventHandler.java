package nl.rutgerkok.bedsock.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied on public methods with a single parameter that indicates
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

    /**
     * Whether the event handler should still be called when the event is cancelled.
     *
     * @return False if the event handler should still be called, even if the event
     *         is cancelled. True by default.
     */
    boolean ignoreCancelled() default true;

    /**
     * When the event handler should be called.
     *
     * @return When the event handler should be called.
     */
    EventPriority priority() default EventPriority.NORMAL;
}
