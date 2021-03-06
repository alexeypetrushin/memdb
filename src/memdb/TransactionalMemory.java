package memdb;

import java.io.*;

public class TransactionalMemory {

  static public Object deepClone(Object o) {
    return deserialize(serialize(o));
  }

  public static byte[] serialize(Object obj) {
    try {
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      ObjectOutputStream o = new ObjectOutputStream(b);
      o.writeObject(obj);
      return b.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object deserialize(byte[] bytes) {
    ByteArrayInputStream b = new ByteArrayInputStream(bytes);
    try {
      ObjectInputStream o = new ObjectInputStream(b);
      return o.readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void atomic(Transaction transaction) {
    try {
      transaction.run();
    } catch (Exception e) {
      if (!transaction.rolledBack()) transaction.rollback();
      throw new RuntimeException(e);
    }
  }
}