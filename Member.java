package library;

/** A library member who can borrow or reserve books. */
public class Member {
    private final String name;

    public Member(String name) { this.name = name; }
    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
