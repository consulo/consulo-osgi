package aQute.bnd.settings;

import aQute.libg.cryptography.Crypto;
import aQute.libg.cryptography.RSA;
import aQute.libg.tuple.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {
  public final static String EMAIL = "email";
  public final static String NAME = "name";
  public final static String PASSWORD_SHA1 = "password.sha1";
  final static String KEY_PRIVATE = "key.private";
  final static String KEY_PUBLIC = "key.public";
  final static String KEY_SET = "key.set";

  static Preferences prefs = Preferences.userNodeForPackage(Settings.class);

  public String globalGet(String key, String def) {
    return prefs.get(key, def);
  }

  public void globalSet(String key, String value) throws BackingStoreException {
    prefs.put(key, value);
    prefs.sync();
  }

  public Collection<String> getKeys() throws BackingStoreException {
    return Arrays.asList(prefs.keys());
  }

  public void globalRemove(String key) throws BackingStoreException {
    prefs.remove(key);
    prefs.sync();
  }

  private void generate() throws NoSuchAlgorithmException {
    Pair<? extends PrivateKey, ? extends RSAPublicKey> pair = RSA.generate();
    prefs.put(KEY_PRIVATE, Crypto.toString(pair.a));
    prefs.put(KEY_PUBLIC, Crypto.toString(pair.b));
    prefs.putBoolean(KEY_SET, true);
  }

  public PrivateKey getPrivateKey() throws Exception {
    if (prefs.getBoolean(KEY_SET, false)) generate();

    String key = prefs.get(KEY_PRIVATE, null);
    return Crypto.fromString(key, PrivateKey.class);
  }

  public PublicKey getPublicKey() throws Exception {
    if (prefs.getBoolean(KEY_SET, false)) generate();

    String key = prefs.get(KEY_PUBLIC, null);
    return Crypto.fromString(key, PublicKey.class);
  }

}
