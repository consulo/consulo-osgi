package org.jetbrains.osgi.compiler.artifact.bndTools.serviceComponent;

import aQute.lib.osgi.Clazz;
import aQute.lib.osgi.Constants;
import aQute.lib.osgi.Processor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VISTALL
 * @see aQute.bnd.make.component.ServiceComponent
 * @since 14:35/06.05.13
 */
public class BndServiceComponentUtil implements Constants {
  public final static Pattern REFERENCE = Pattern.compile("([^(]+)(\\(.+\\))?");

  public static String getName(Map<String, String> map, Clazz clazz) {
    String localname = map.get(COMPONENT_NAME);
    if (localname == null) {
      localname = clazz.getFQN();
    }
    return localname;
  }

  public static String toXml(Map<String, String> info, String name) {
    StringBuilder builder = new StringBuilder();

    // Assume the impl==name, but allow override
    String impl = name;
    if (info.containsKey(COMPONENT_IMPLEMENTATION)) {
      impl = info.get(COMPONENT_IMPLEMENTATION);
    }


    builder.append("<?xml version='1.0' encoding='utf-8'?>\n");
    builder.append("<component");

    doAttribute(builder, name, "name");
    doAttribute(builder, info.get(COMPONENT_FACTORY), "factory");
    doAttribute(builder, info.get(COMPONENT_IMMEDIATE), "immediate", "false", "true");
    doAttribute(builder, info.get(COMPONENT_ENABLED), "enabled", "true", "false");
    doAttribute(builder, info.get(COMPONENT_CONFIGURATION_POLICY), "configuration-policy", "optional", "require", "ignore");
    doAttribute(builder, info.get(COMPONENT_ACTIVATE), "activate", JIDENTIFIER);
    doAttribute(builder, info.get(COMPONENT_DEACTIVATE), "deactivate", JIDENTIFIER);
    doAttribute(builder, info.get(COMPONENT_MODIFIED), "modified", JIDENTIFIER);
    builder.append(">\n");

    // Allow override of the implementation when people
    // want to choose their own name
    builder.append("  <implementation class='" + (impl == null ? name : impl) + "'/>\n");

    String provides = info.get(COMPONENT_PROVIDE);
    boolean servicefactory = Processor.isTrue(info.get(COMPONENT_SERVICEFACTORY));

    if (servicefactory && Processor.isTrue(info.get(COMPONENT_IMMEDIATE))) {
      // TODO can become error() if it is up to me
      //warning("For a Service Component, the immediate option and the servicefactory option are mutually exclusive for %(%s)", name, impl);
    }
    provide(builder, provides, servicefactory, impl);
    properties(builder, info);
    reference(info, builder);

    builder.append("</component>");

    return builder.toString();
  }

  private static void properties(StringBuilder builder, Map<String, String> info) {
    Collection<String> properties = Processor.split(info.get(COMPONENT_PROPERTIES));
    for (Iterator<String> p = properties.iterator(); p.hasNext(); ) {
      String clause = p.next();
      int n = clause.indexOf('=');
      if (n <= 0) {
        //error("Not a valid property in service component: " + clause);
      }
      else {
        String type = null;
        String name = clause.substring(0, n);
        if (name.indexOf('@') >= 0) {
          String parts[] = name.split("@");
          name = parts[1];
          type = parts[0];
        }
        else if (name.indexOf(':') >= 0) {
          String parts[] = name.split(":");
          name = parts[0];
          type = parts[1];
        }
        String value = clause.substring(n + 1).trim();
        // TODO verify validity of name and value.
        builder.append("  <property name='");
        builder.append(name);
        builder.append("'");

        if (type != null) {
          if (VALID_PROPERTY_TYPES.matcher(type).matches()) {
            builder.append(" type='");
            builder.append(type);
            builder.append("'");
          }
          else {
            // warning("Invalid property type '" + type + "' for property " + name);
          }
        }

        String parts[] = value.split("\\s*(\\||\\n)\\s*");
        if (parts.length > 1) {
          builder.append(">");
          for (String part : parts) {
            builder.append(part);
          }
          builder.append("</property>");
        }
        else {
          builder.append(" value='");
          builder.append(parts[0]);
          builder.append("'/>");
        }
      }
    }
  }

  private static void reference(Map<String, String> info, StringBuilder pw) {
    Collection<String> dynamic = new ArrayList<String>(Processor.split(info.get(COMPONENT_DYNAMIC)));
    Collection<String> optional = new ArrayList<String>(Processor.split(info.get(COMPONENT_OPTIONAL)));
    Collection<String> multiple = new ArrayList<String>(Processor.split(info.get(COMPONENT_MULTIPLE)));

    Collection<String> descriptors = Processor.split(info.get(COMPONENT_DESCRIPTORS));

    for (Map.Entry<String, String> entry : info.entrySet()) {

      // Skip directives
      String referenceName = entry.getKey();
      if (referenceName.endsWith(":")) {
        /*if (!SET_COMPONENT_DIRECTIVES.contains(referenceName)) {
          error("Unrecognized directive in Service-Component header: " + referenceName);
        }     */
        continue;
      }

      // Parse the bind/unbind methods from the name
      // if set. They are separated by '/'
      String bind = null;
      String unbind = null;

      boolean unbindCalculated = false;

      if (referenceName.indexOf('/') >= 0) {
        String parts[] = referenceName.split("/");
        referenceName = parts[0];
        bind = parts[1];
        if (parts.length > 2) {
          unbind = parts[2];
        }
        else {
          unbindCalculated = true;
          if (bind.startsWith("add")) {
            unbind = bind.replaceAll("add(.+)", "remove$1");
          }
          else {
            unbind = "un" + bind;
          }
        }
      }
      else if (Character.isLowerCase(referenceName.charAt(0))) {
        unbindCalculated = true;
        bind = "set" + Character.toUpperCase(referenceName.charAt(0)) + referenceName.substring(1);
        unbind = "un" + bind;
      }

      String interfaceName = entry.getValue();
      if (interfaceName == null || interfaceName.length() == 0) {
        // error("Invalid Interface Name for references in Service Component: " + referenceName + "=" + interfaceName);
        continue;
      }

      // If we have descriptors, we have analyzed the component.
      // So why not check the methods
      if (descriptors.size() > 0) {
        // Verify that the bind method exists
        if (!descriptors.contains(bind)) {
          //  error("The bind method %s for %s not defined", bind, referenceName);
        }

        // Check if the unbind method exists
        if (!descriptors.contains(unbind)) {
          if (unbindCalculated)
          // remove it
          {
            unbind = null;
          }
          else {
            //    error("The unbind method %s for %s not defined", unbind, referenceName);
          }
        }
      }
      // Check tje cardinality by looking at the last
      // character of the value
      char c = interfaceName.charAt(interfaceName.length() - 1);
      if ("?+*~".indexOf(c) >= 0) {
        if (c == '?' || c == '*' || c == '~') {
          optional.add(referenceName);
        }
        if (c == '+' || c == '*') {
          multiple.add(referenceName);
        }
        if (c == '+' || c == '*' || c == '?') {
          dynamic.add(referenceName);
        }
        interfaceName = interfaceName.substring(0, interfaceName.length() - 1);
      }

      // Parse the target from the interface name
      // The target is a filter.
      String target = null;
      Matcher m = REFERENCE.matcher(interfaceName);
      if (m.matches()) {
        interfaceName = m.group(1);
        target = m.group(2);
      }


      pw.append(String.format("  <reference name='%s'", referenceName));
      pw.append(String.format(" interface='%s'", interfaceName));

      String cardinality = optional.contains(referenceName) ? "0" : "1";
      cardinality += "..";
      cardinality += multiple.contains(referenceName) ? "n" : "1";
      if (!cardinality.equals("1..1")) {
        pw.append(" cardinality='" + cardinality + "'");
      }

      if (bind != null) {
        pw.append(String.format(" bind='%s'", bind));
        if (unbind != null) {
          pw.append(String.format(" unbind='%s'", unbind));
        }
      }

      if (dynamic.contains(referenceName)) {
        pw.append(" policy='dynamic'");
      }

      if (target != null) {
        // Filter filter = new Filter(target);
        // if (filter.verify() == null)
        // pw.print(" target='" + filter.toString() + "'");
        // else
        // error("Target for " + referenceName
        // + " is not a correct filter: " + target + " "
        // + filter.verify());
        pw.append(" target='" + escape(target) + "'");
      }
      pw.append("/>\n");
    }
  }


  /**
   * Escape a string, do entity conversion.
   */
  static String escape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '&':
          sb.append("&amp;");
          break;
        case '\'':
          sb.append("&quot;");
          break;
        default:
          sb.append(c);
          break;
      }
    }
    return sb.toString();
  }

  private static void provide(StringBuilder pw, String provides, boolean servicefactory, String impl) {
    if (provides != null) {
      if (!servicefactory) {
        pw.append("  <service>\n");
      }
      else {
        pw.append("  <service servicefactory='true'>\n");
      }

      StringTokenizer st = new StringTokenizer(provides, ",");
      while (st.hasMoreTokens()) {
        String interfaceName = st.nextToken();
        pw.append("    <provide interface='" + interfaceName + "'/>\n");
      }
      pw.append("  </service>\n");
    }
    ///else if (servicefactory) warning("The servicefactory:=true directive is set but no service is provided, ignoring it");
  }

  private static void doAttribute(StringBuilder builder, String value, String name, String... matches) {
    if (value != null) {
      if (matches.length != 0) {
        if (matches.length == 1 && matches[0].equals(JIDENTIFIER)) {
          //if (!Verifier.isIdentifier(value)) error("Component attribute %s has value %s but is not a Java identifier", name, value);
        }
        else {

          /*if (!Verifier.isMember(value, matches)) {
            error("Component attribute %s has value %s but is not a member of %s", name, value, Arrays.toString(matches));
          }  */
        }
      }
      builder.append(" ");
      builder.append(name);
      builder.append("='");
      builder.append(value);
      builder.append("'");
    }
  }
}
