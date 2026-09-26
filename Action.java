package library;

/** A single reversible action, pushed onto the undo stack when it happens. */
class Action {
    enum Type { ISSUE, RETURN }

    final Type type;
    final String isbn;
    final String memberName;

    Action(Type type, String isbn, String memberName) {
        this.type = type;
        this.isbn = isbn;
        this.memberName = memberName;
    }
}
