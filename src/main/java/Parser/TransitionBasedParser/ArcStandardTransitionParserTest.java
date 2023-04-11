package Parser.TransitionBasedParser;

import Classification.Model.Model;
import DependencyParser.ParserEvaluationScore;
import DependencyParser.Universal.UniversalDependencyTreeBankCorpus;
import DependencyParser.Universal.UniversalDependencyTreeBankSentence;
import Util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ArcStandardTransitionParserTest {

    private static Model loadModel(String fileName) {
        ObjectInputStream outObject;
        Model model = null;
        try {
            outObject = new ObjectInputStream(FileUtils.getInputStream(fileName));
            model = (Model) outObject.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Test
    public void testC1Oracle() {
        ParserEvaluationScore scores = new ParserEvaluationScore();
        TransitionParser transitionParser = new ArcStandardTransitionParser();
        UniversalDependencyTreeBankCorpus corpus = new UniversalDependencyTreeBankCorpus("tr_boun-ud-test.conllu");
        Model model = loadModel("decision-tree-boun-1");
        for (int i = 0; i < corpus.sentenceCount(); i++) {
            UniversalDependencyTreeBankSentence actual = (UniversalDependencyTreeBankSentence) corpus.getSentence(i);
            UniversalDependencyTreeBankSentence expected = transitionParser.dependencyParse(actual, new ArcStandardCombinedOracle(model, 2));
            scores.add(actual.compareParses(expected));
        }
        Assert.assertEquals(64.89458453906572, 100 * scores.getLS(), 0.01);
        Assert.assertEquals(47.110376188507644, 100 * scores.getLAS(), 0.01);
        Assert.assertEquals(54.91525423728817, 100 * scores.getUAS(), 0.01);
    }

    @Test
    public void testC2Oracle() {
        ParserEvaluationScore scores = new ParserEvaluationScore();
        TransitionParser transitionParser = new ArcStandardTransitionParser();
        UniversalDependencyTreeBankCorpus corpus = new UniversalDependencyTreeBankCorpus("tr_boun-ud-test.conllu");
        Model model1 = loadModel("decision-tree-boun-2");
        Model model2 = loadModel("decision-tree-boun-relation-2");
        for (int i = 0; i < corpus.sentenceCount(); i++) {
            UniversalDependencyTreeBankSentence actual = (UniversalDependencyTreeBankSentence) corpus.getSentence(i);
            UniversalDependencyTreeBankSentence expected = transitionParser.dependencyParse(actual, new ArcStandardStepWiseOracle(model1, model2, 2));
            scores.add(actual.compareParses(expected));
        }
        Assert.assertEquals(68.04464654816037, 100 * scores.getLS(), 0.01);
        Assert.assertEquals(49.855312112443166, 100 * scores.getLAS(), 0.01);
        Assert.assertEquals(58.238941711451055, 100 * scores.getUAS(), 0.01);
    }
}
