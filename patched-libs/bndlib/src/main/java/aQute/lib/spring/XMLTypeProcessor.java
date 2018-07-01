package aQute.lib.spring;

import aQute.bnd.service.AnalyzerPlugin;
import aQute.lib.osgi.Analyzer;
import aQute.lib.osgi.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XMLTypeProcessor implements AnalyzerPlugin {

  public boolean analyzeJar(Analyzer analyzer) throws Exception {
    List<XMLType> types = getTypes(analyzer);
    for (XMLType type : types) {
      type.analyzeJar(analyzer);
    }
    return false;
  }

  protected List<XMLType> getTypes(Analyzer analyzer) throws Exception {
    return new ArrayList<XMLType>();
  }


  protected void process(List<XMLType> types, String resource, String paths, String pattern) throws Exception {

    Map<String, Map<String, String>> map = Processor.parseHeader(paths, null);
    for (String path : map.keySet()) {
      types.add(new XMLType(getClass().getResource(resource), path, pattern));
    }
  }


}
