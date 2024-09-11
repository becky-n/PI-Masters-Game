package nz.ac.auckland;

import javafx.scene.layout.Pane;

public class ClueMenu {
    private static Pane storedClueMenuPane;

    public static Pane getStoredClueMenuPane() {
        return storedClueMenuPane;
    }

    public static void setStoredClueMenuPane(Pane pane) {
        storedClueMenuPane = pane;
    }
}

