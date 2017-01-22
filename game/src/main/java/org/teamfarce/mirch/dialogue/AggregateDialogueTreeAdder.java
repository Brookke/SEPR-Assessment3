package org.teamfarce.mirch.dialogue;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This represents multiple IDialogueTreeAdders aggregated together into one object.
 * <p>
 * This IDialogueTreeAdder is designed to allow multiple IDialogueTreeAdders to be triggered by this
 * single IDialogueTreeAdder.
 * </p>
 */
public class AggregateDialogueTreeAdder implements IDialogueTreeAdder {
    private Collection<IDialogueTreeAdder> dialogueTreeAdders;

    /**
     * Initialise the AggregateDialogueTreeAdder in an empty state.
     */
    public AggregateDialogueTreeAdder() {
        this.dialogueTreeAdders = new ArrayList<>();
    }

    /**
     * Initialise the AggregateDialogueTreeAdder with the given collection.
     *
     * @param dialogueTreeAdders The collection of IDialogueTreeAdders to add.
     */
    public AggregateDialogueTreeAdder(Collection<IDialogueTreeAdder> dialogueTreeAdders) {
        this.dialogueTreeAdders = dialogueTreeAdders;
    }

    @Override
    public void addToTrees() {
        for (IDialogueTreeAdder dta: this.dialogueTreeAdders) {
            dta.addToTrees();
        }
    }

    @Override
    public IDialogueTreeAdder addDialogueTreeAdder(IDialogueTreeAdder other) {
        this.dialogueTreeAdders.add(other);
        return this;
    }
}
