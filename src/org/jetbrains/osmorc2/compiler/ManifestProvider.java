package org.jetbrains.osmorc2.compiler;

import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.osmorc2.manifest.BundleManifest;
import org.jetbrains.osmorc2.manifest.impl.DummyBundleManifestImpl;

import java.io.IOException;
import java.util.Map;

/**
 * @author VISTALL
 * @since 14:01/29.04.13
 */
public abstract class ManifestProvider implements JDOMExternalizable{
  private boolean isActive;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
  }

  @NotNull
  public final BundleManifest getBundleManifest() {
    BundleManifest bundleManifestImpl = getBundleManifestImpl();
    return bundleManifestImpl == null ? DummyBundleManifestImpl.INSTANCE : bundleManifestImpl;
  }

  @NotNull
  public abstract ManifestProviderConfigurable createConfigurable();

  @Nullable
  protected abstract BundleManifest getBundleManifestImpl();

  public abstract void getBuildProperties(@NotNull Map<String, String> map) throws IOException;
}
