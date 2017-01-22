package org.teamfarce.mirch.tests.dialogue;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.teamfarce.mirch.dialogue.*;

public class DialogueTreeTest {
    @Test
    public void test0() {
        DialogueTree dialogueTree = new DialogueTree(new ArrayList<>());

        QuestionAndResponse qar11 = new QuestionAndResponse(
            "question 1", "style 1", "response 1", new ArrayList<>()
        );
        QuestionAndResponse qar12 = new QuestionAndResponse(
            "question 2", "style 2", "response 2", new ArrayList<>()
        );
        QuestionAndResponse qar13 = new QuestionAndResponse(
            "question 3", "style 3", "response 3", new ArrayList<>()
        );

        ArrayList<QuestionAndResponse> qi1_questions = new ArrayList<>();
        qi1_questions.add(qar11);
        qi1_questions.add(qar12);
        qi1_questions.add(qar13);

        QuestionIntent qi1 = new QuestionIntent(qi1_questions, "question intent 1");

        SingleDialogueTreeAdder dta = new SingleDialogueTreeAdder(dialogueTree, qi1);

        assertEquals(dialogueTree.getAvailableIntentChoices().size(), 0);
        dta.addToTrees();
        assertEquals(dialogueTree.getAvailableIntentChoices().size(), 1);

        QuestionAndResponse qar21 = new QuestionAndResponse(
            "question 4", "style 4", "response 4", new ArrayList<>()
        );
        QuestionAndResponse qar22 = new QuestionAndResponse(
            "question 5", "style 5", "response 5", new ArrayList<>()
        );
        QuestionAndResponse qar23 = new QuestionAndResponse(
            "question 6", "style 6", "response 6", new ArrayList<>()
        );
        ArrayList<QuestionAndResponse> qi2_questions = new ArrayList<>();
        qi2_questions.add(qar21);
        qi2_questions.add(qar22);
        qi2_questions.add(qar23);

        QuestionIntent qi2 = new QuestionIntent(qi2_questions, "question intent 2");

        qar12.addDialogueTreeAdder(new SingleDialogueTreeAdder(dialogueTree, qi2));

        assertEquals(dialogueTree.getAvailableIntentChoices().get(0), "question intent 1");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(0), "style 1: question 1");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(1), "style 2: question 2");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(2), "style 3: question 3");

        QuestionResult qr1 = dialogueTree.selectQuestion(0, 1);
        assertEquals(qr1.response, "response 2");
        assertEquals(qr1.clues, new ArrayList<>());
        assertEquals(dialogueTree.getAvailableIntentChoices().size(), 1);

        assertEquals(dialogueTree.getAvailableIntentChoices().get(0), "question intent 2");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(0), "style 4: question 4");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(1), "style 5: question 5");
        assertEquals(dialogueTree.getAvailableStyleChoices(0).get(2), "style 6: question 6");

        QuestionResult qr2 = dialogueTree.selectQuestion(0, 0);
        assertEquals(qr2.response, "response 4");
        assertEquals(qr2.clues, new ArrayList<>());
        assertEquals(dialogueTree.getAvailableIntentChoices().size(), 0);
    }
}
