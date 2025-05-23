import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This abstract data type represents a collection that maps keys to values, in which duplicate keys
 * are not allowed (each key maps to exactly one value).
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  // private instance variable to hold the table
  private LinkedList<Pair>[] table = null;
  // private instance to store the maps capacity
  private int capacity;
  // private instance to store the size of the map
  private int size;

  
  /*
   * constructor to create a new hashmap with the provided capacity
   * 
   * @param capacity the initial capacity that we will give the hashmap
   */
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    this.capacity = capacity;
    this.table = (LinkedList<Pair>[]) new LinkedList[capacity];
    this.size = 0;
  }

  /*
   * constructor to create a new hashmap with an initial capacity of 64
   */
  @SuppressWarnings("unchecked")
  public HashtableMap() {
    this.capacity = 64;
    this.table = (LinkedList<Pair>[]) new LinkedList[capacity];
    this.size = 0;
  }

  /**
   * inner class used to store pairs of key-value pairs in our hashtable array
   */
  protected class Pair {

    public KeyType key;
    public ValueType value;

    /**
     * constuctor for pairs that will be used within our hash table
     * 
     * @param key   the key linking to value
     * @param value the value we will access using the key
     */
    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }

  /**
   * Adds a new key,value pair/mapping to this collection.
   * 
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    // make sure key is not null
    if (key == null)
      throw new NullPointerException("Key can't be null");

    // get the index of this table
    int index = Math.abs(key.hashCode()) % capacity;

    // if the bucket does not exist implement it
    if (table[index] == null) {
      table[index] = new LinkedList<>();
    }

    // make sure the key does not already exist in the bucket
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        throw new IllegalArgumentException("Key already exists in the table.");
      }
    }
    // add pair to the table
    table[index].add(new Pair(key, value));
    size++;

    // check if we need to rehash the array
    if ((float) this.size / this.capacity > .8) {
      reHash();
    }
  }

  /**
   * private method to reshash the table whenever the size becomes 80% of capacity
   */
  @SuppressWarnings("unchecked")
  private void reHash() {
    // create a new table with double the capacity
    LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[capacity * 2];

    // add each value from the old table at every other index
    for (LinkedList<Pair> bucket : table) {

      // as long as the bucket isn't null
      if (bucket != null) {
        // for every pair in the bucket rehash their values with the new capacity
        for (Pair pair : bucket) {
          int newIndex = Math.abs(pair.key.hashCode()) % capacity * 2;

          // if that new index is null then create a new bucket at that index
          if (newTable[newIndex] == null) {
            newTable[newIndex] = new LinkedList<>();
          }

          // add the pair to a new table
          newTable[newIndex].add(pair);
        }
      }
    }

    // set table to the new table and capacity
    table = newTable;
    capacity = capacity * 2;
  }

  /**
   * Checks whether a key maps to a value in this collection.
   * 
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  public boolean containsKey(KeyType key) {
    // add each value from the old table at every other index
    for (LinkedList<Pair> bucket : table) {

      // as long as the bucket isn't null
      if (bucket != null) {
        // for every pair in the bucket check if it is the key and if so return true
        for (Pair pair : bucket) {
          if (pair.key.equals(key)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Retrieves the specific value that a key maps to.
   * 
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  public ValueType get(KeyType key) throws NoSuchElementException {
    // add each value from the old table at every other index
    for (LinkedList<Pair> bucket : table) {

      // as long as the bucket isn't null
      if (bucket != null) {
        // for every pair in the bucket check if it is the key and if so return its value
        for (Pair pair : bucket) {
          if (pair.key.equals(key)) {
            return pair.value;
          }
        }
      }
    }
    // throw NoSuchElementException if the key cannot be found
    throw new NoSuchElementException("key could not be found in the table");
  }

  /**
   * Remove the mapping for a key from this collection.
   * 
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  public ValueType remove(KeyType key) throws NoSuchElementException {
    // add each value from the old table at every other index
    for (LinkedList<Pair> bucket : table) {

      // as long as the bucket isn't null
      if (bucket != null) {
        // for every pair in the bucket check if it is the key and if so return its value
        for (Pair pair : bucket) {
          if (pair.key.equals(key)) {
            bucket.remove(pair);
            size --;
            return pair.value;
          }
        }
      }
    }
    // throw NoSuchElementException if the key cannot be found
    throw new NoSuchElementException("key could not be found in the table");
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @SuppressWarnings("unchecked")
  public void clear() {
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    size = 0;
  }

  /**
   * Retrieves the number of keys stored in this collection.
   * 
   * @return the number of keys stored in this collection
   */
  public int getSize() {
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   * 
   * @return the size of the underlying array for this collection
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Retrieves this collection's keys.
   * 
   * @return a list of keys in the underlying array for this collection
   */
  public List<KeyType> getKeys() {
    List<KeyType> keys = new LinkedList<>();
    // add each value from the old table at every other index
    for (LinkedList<Pair> bucket : table) {

      // as long as the bucket isn't null
      if (bucket != null) {
        // for every pair in the bucket and add its keys to the list
        for (Pair pair : bucket) {
          keys.add(pair.key);
        }
      }
    }
    // return the list of keys
    return keys;
  }
}
