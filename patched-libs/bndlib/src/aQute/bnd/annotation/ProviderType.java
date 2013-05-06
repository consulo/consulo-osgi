package aQute.bnd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This type is provided for convenience because it is the default. A change
 * in a provider type (that is all except Consumer types) can be changed with
 * only a minor update to the package API version number.
 * <p/>
 * This interface is similar to the Eclipse @noextend and @noimplement annotations.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ProviderType {

}
