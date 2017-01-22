package org.teamfarce.mirch.dialogue;

/**
 * An object which adds to some dialogue trees when invoked.
 */
public interface IDialogueTreeAdder {
    /**
     * This method adds to the dialogue trees.
     */
    public void addToTrees();

    /**
     * Add a new IDialogueTreeAdder to this IDialogueTreeAdder.
     *
     * @param other The new IDialogueTreeAdder.
     * @return Returns the new IDialogueTreeAdder with both of these adders's effects combined.
     */
    public IDialogueTreeAdder addDialogueTreeAdder(IDialogueTreeAdder other);
}
