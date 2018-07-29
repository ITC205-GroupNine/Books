public class FixBookControl {

    private FixBookUI userInterface;
    private enum fixBookControlState { INITIALISED, READY, FIXING }

    private fixBookControlState state;
    private library library;
    private book currentBook;


    public FixBookControl() {
        //this should probably be a getInstance() per Jims notes. LIBRARY is not under my control, so request update?
        this.library = library.INSTANCE();
        state = fixBookControlState.INITIALISED;
    }


    public void setUI(FixBookUI newUserInterface) {
        if (!state.equals(fixBookControlState.INITIALISED)) {
            //Should we be throwing exceptions whne they can be avoided?
            throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
        }
        this.userInterface = newUserInterface;
        userInterface.setState(FixBookUI.fixBookUserInterfaceState.READY);
        state = fixBookControlState.READY;
    }


    public void bookScanned(int bookId) {
        if (!state.equals(fixBookControlState.READY)) {
            //Should we be throwing exceptions whne they can be avoided?
            throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
        }
        currentBook = library.Book(bookId);

        if (currentBook == null) {
            userInterface.display("Invalid bookId");
            return;
        }
        if (!currentBook.Damaged()) {
            userInterface.display("Book has not been damaged");
            return;
        }
        userInterface.display(currentBook.toString());
        userInterface.setState(FixBookUI.fixBookUserInterfaceState.FIXING);
        state = fixBookControlState.FIXING;
    }


    public void fixBook(boolean fix) {
        if (!state.equals(fixBookControlState.FIXING)) {
            throw new RuntimeException("FixBookControl: cannot call fixBook except in Fixing state");
        }
        if (fix) {
            library.repairBook(currentBook);
        }
        currentBook = null;
        userInterface.setState(FixBookUI.fixBookUserInterfaceState.READY);
        state = fixBookControlState.READY;
    }


    public void scanningComplete() {
        if (!state.equals(fixBookControlState.READY)) {
            throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
        }
        userInterface.setState(FixBookUI.fixBookUserInterfaceState.COMPLETED);
    }
}
