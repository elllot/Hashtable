package lab2;

/**
 * A hash table mapping Strings to their positions in the pattern sequence.
 *
 * Fill in the methods for this class.
 */
public class StringTable {
    public Record table[];
    public int elems;
    public int size;
    /**
     * Create an empty table of size n
     *
     * @param n size of the table
     */
    public StringTable(int n) {
        // TODO: implement this method
        double adjusted;
        double a;
        size = n;
        elems = 0;
        a = Math.log(n) / Math.log(2);
        if (a % 1 == 0){
            adjusted = Math.pow(2,a);
        }
        else {
            adjusted = Math.pow(2, a+1);
        }

        table = new Record[(int)adjusted];

    }

    /**
     * Create an empty table.  You should use this construction when you are
     * implementing the doubling procedure.
     */
    public StringTable() {
        // TODO: implement this method
        elems = 0;
        size = 4;
        table = new Record[4];
    }

    /**
     * Insert a Record r into the table.
     *
     * If you get two insertions with the same key value, return false.
     *
     * @param r Record to insert into the table.
     * @return boolean true if the insertion succeeded, false otherwise
     */
    public boolean insert(Record r) {
        // TODO: implement this method
        DoubleTable();
        int hKey = toHashKey(r.getKey());
        for (int i = 0; i < table.length ; i++){
            int insertPosition = (baseHash(hKey) + i * stepHash(hKey)) % table.length;
            if (table[insertPosition] == null || table[insertPosition].getKey().equals("DeleteMe")){
                table[insertPosition] = r;
                table[insertPosition].hash = hKey;
                r.set_position(insertPosition);
                elems++;
                return true;
            }
            else if (table[insertPosition].hash == hKey){
                if (table[insertPosition].getKey().equals(r.getKey())){
                    return false;
                }
            }
        }
       return false;
    }

    public void DoubleTable(){
         if ((double)elems/(double)size >= 0.25){
            StringTable dTable = new StringTable(this.size() * 2);
            for (Record r: table){
                if (r != null){
                dTable.insert(r);
                }
            }
            this.table = dTable.table;
            this.size = dTable.size;
        }
    }
    /**
     * Delete a record from the table.
     *
     * Note: You'll have to find the record first unless you keep some
     * extra information in the Record structure.
     *
     * @param r Record to remove from the table.
     */
    public void remove(Record r) {
        // TODO: implement this method
        if (r.get_position() != -1){
            table[r.get_position()] = new Record("DeleteMe");
            r.set_position(-1);
            elems--;
        }
        /*int hKey = toHashKey(r.getKey());
        for (int i = 0; i < table.length ; i++){
            int deletePosition = (baseHash(hKey) + stepHash(hKey)) % table.length;;
            if (table[deletePosition].getKey().equals(r.getKey())){
                table[deletePosition] = new Record("DeleteMe");
                elems--;
            }
        }*/
    }

    /**
     * Find a record with a key matching the input.
     *
     * @param key to find
     * @return the matched Record or null if no match exists.
     */
    public Record find(String key) {
        int hKey = toHashKey(key);
        for (int i = 0; i < table.length; i++){
            int position = (baseHash(hKey) + i * stepHash(hKey)) % table.length;
            if (table[position] == null){
                return null;
            }
            //Terminal.println("table[" + position + "]: " + table[position]);
            if (table[position] != null){
                if (table[position].hash == hKey){
                    if (table[position].getKey().equals(key)){
                    return table[position];
                    }
                }
            }
        }
        // TODO: implement this method
       return null;
    }

    /**
     * Return the size of the hash table (i.e. the number of elements
     * this table can hold)
     *
     * @return the size of the table
     */
    public int size() {
        // TODO: implement this method
       return size;
    }

    /**
     * Return the hash position of this key.  If it is in the table, return
     * the postion.  If it isn't in the table, return the position it would
     * be put in.  This value should always be smaller than the size of the
     * hash table.
     *
     * @param key to hash
     * @return the int hash
     */
    public int hash(String key) {

        int hKey = toHashKey(key);
        for (int i = 0; i < table.length ; i++){
            int position = (baseHash(hKey) + i * stepHash(hKey)) % table.length;
            if (table[position] == null || table[position].getKey().equals("DeleteMe")){
                return position;
            }
            else if (table[position].hash == hKey){
                return position;
            }
        }
        return 0;
    }

    /**
     * Convert a String key into an integer that serves as input to hash functions.
     * This mapping is based on the idea of a linear-congruential pseuodorandom
     * number generator, in which successive values r_i are generated by computing
     *    r_i = (A * r_(i-1) + B) mod M
     * A is a large prime number, while B is a small increment thrown in so that
     * we don't just compute successive powers of A mod M.
     *
     * We modify the above generator by perturbing each r_i, adding in the ith
     * character of the string and its offset, to alter the pseudorandom
     * sequence.
     *
     * @param s String to hash
     * @return int hash
     */
    int toHashKey(String s) {
        int A = 1952786893;
        int B = 367253;
        int v = B;

        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            v = A * (v + (int) c + j) + B;
        }

        if (v < 0) {
            v = -v;
        }
        return v;
    }

    /**
     * Computes the base hash of a hash key
     *
     * @param hashKey
     * @return int base hash
     */
    int baseHash(int hashKey) {
       double a;
       if (hashKey % 2 == 0){
    	   hashKey *= 0.120937109;
    	   a = (Math.sqrt(5) - 1) / 2;
       }
       else {
    	   hashKey *= 0.7234254;
    	   a = (Math.sqrt(11) - 1) / 2;
       }
       // (sqrt(5) - 1) / 2 from the notes
       
       //int bHash = (int) (Math.floor(table.length * (hashKey * a - Math.floor(hashKey * a))) * table.length);
       int bHash = (int) Math.floor(table.length * ((a * hashKey) % 1));
       return bHash;
    }

    /**
     * Computes the step hash of a hash key
     *
     * @param hashKey
     * @return int step hash
     */
    int stepHash(int hashKey) {
    	double a;
    	if (hashKey % 2 == 0){
    		hashKey *= 0.5912541;
    		a = (Math.sqrt(11) - 1) / 2;
    	}
    	else {
    		hashKey *= 0.138913;
    		a = (Math.sqrt(5) - 1) / 2;
    	}
        int calc = (int) Math.floor(table.length * ((a * hashKey) % 1));
        if (calc % 2 == 0){
            calc++;
        }
        // TODO: implement this method
        return calc;
    }

}