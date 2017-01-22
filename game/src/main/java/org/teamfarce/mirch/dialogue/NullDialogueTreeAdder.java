package org.teamfarce.mirch.dialogue;

/**
 * This represents a DialogueTreeAdder which does nothing.
 * <p>
 * This is useful to represent the end of a dialogue tree since when it is called, no
 * questionIntent will be added to any dialogueTree.
 * </p>
 */
public class NullDialogueTreeAdder implements IDialogueTreeAdder {
    /**
     * Construct a NullDialogueTreeAdder.
     */
    public NullDialogueTreeAdder() {}

    @Override
    public void addToTrees() {}

    @Override
    public IDialogueTreeAdder addDialogueTreeAdder(IDialogueTreeAdder other) {
        return other;
    }
}
