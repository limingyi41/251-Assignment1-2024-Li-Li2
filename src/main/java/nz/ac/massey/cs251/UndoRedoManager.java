package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class UndoRedoManager {
    private UndoManager undoManager;

    public UndoRedoManager(JTextPane textPane) {
        undoManager = new UndoManager();
        textPane.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
    }

    public void undo() {
        try {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        } catch (CannotUndoException e) {
            e.printStackTrace();
        }
    }

    public void redo() {
        try {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        } catch (CannotRedoException e) {
            e.printStackTrace();
        }
    }
}
