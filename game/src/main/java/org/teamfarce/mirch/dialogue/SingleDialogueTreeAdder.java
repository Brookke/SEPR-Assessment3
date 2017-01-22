package org.teamfarce.mirch.dialogue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * This represents a DialogueTreeAdder which adds the provided questionIntent to the provided
 * dialogueTree when triggered.
 * <p>
 * This class goes to an effort to make sure that the existence of this object does not cause the
 * dialogueTree it is assosiated with to live on when it should not.
 * </p>
 */
public class SingleDialogueTreeAdder implements IDialogueTreeAdder {
    private WeakReference<DialogueTree> dialogueTreeRef;
    private QuestionIntent questionIntent;

    /**
     * Construct a SingleDialogueTreeAdder with the assosiated dialogueTree and questionIntent.
     *
     * @param dialogueTree The dialogue tree to add dialogue to.
     * @param questionIntent The QuestionIntent to add to the dialogue tree.
     */
    public SingleDialogueTreeAdder(DialogueTree dialogueTree, QuestionIntent questionIntent) {
        this.dialogueTreeRef = new WeakReference<>(dialogueTree);
        this.questionIntent = questionIntent;
    }

    @Override
    public void addToTrees() {
        DialogueTree dialogueTree = this.dialogueTreeRef.get();
        if (dialogueTree != null) {
            dialogueTree.addQuestionIntent(this.questionIntent);
        }
    }

    @Override
    public IDialogueTreeAdder addDialogueTreeAdder(IDialogueTreeAdder other) {
        ArrayList<IDialogueTreeAdder> dialogueTreeAdders = new ArrayList<>();
        dialogueTreeAdders.add(this);
        dialogueTreeAdders.add(other);
        return new AggregateDialogueTreeAdder(dialogueTreeAdders);
    }
}
