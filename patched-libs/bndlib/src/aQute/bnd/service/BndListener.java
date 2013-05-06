package aQute.bnd.service;

import aQute.libg.reporter.Reporter;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class BndListener {
  final AtomicInteger inside = new AtomicInteger();

  public void changed(File file) {
  }

  public void begin() {
    inside.incrementAndGet();
  }

  public void end() {
    inside.decrementAndGet();
  }

  public boolean isInside() {
    return inside.get() != 0;
  }

  public void signal(Reporter reporter) {

  }
}
