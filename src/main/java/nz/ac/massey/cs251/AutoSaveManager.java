package nz.ac.massey.cs251;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSaveManager {
    private Timer autoSaveTimer;
    private FilesOperations fileOperations;

    public AutoSaveManager(FilesOperations fileOperations) {
        this.fileOperations = fileOperations;
        this.autoSaveTimer = new Timer();
    }

    public void startAutoSave() {
        autoSaveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSave();
            }
        }, 60000, 60000);
    }

    private void autoSave() {
        try {
            if (fileOperations.getCurrentFileName() != null) {
                fileOperations.saveFile(false);
                System.out.println("Auto saved at: " + System.currentTimeMillis());
            }
        } catch (IOException ex) {
            System.err.println("Auto save failed: " + ex.getMessage());
        }
    }
}
