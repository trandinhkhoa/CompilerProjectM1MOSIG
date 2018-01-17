/**
 * Class Id generate new Id for identifiers
 *
 */

class Id {
    String id;
    static int x = -1;

    Id(String id) {
        this.id = id;
    }


    /**
     * Convert identifiers to id.
     *
     * @return The new identifiers id
     */
    @Override
    public String toString() {
        return id;
    }

    static Id gen() {
        x++;
        return new Id("?v" + x);
    }

}
