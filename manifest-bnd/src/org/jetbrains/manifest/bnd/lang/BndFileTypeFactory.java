package org.jetbrains.manifest.bnd.lang;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 1:33/12.05.13
 */
public class BndFileTypeFactory extends FileTypeFactory {
  @Override
  public void createFileTypes(@NotNull FileTypeConsumer consumer) {
    consumer.consume(BndFileType.INSTANCE);
  }
}
