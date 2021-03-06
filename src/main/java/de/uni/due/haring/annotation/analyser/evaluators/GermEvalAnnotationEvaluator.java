package de.uni.due.haring.annotation.analyser.evaluators;

import java.util.List;
import java.util.stream.Collectors;

import org.dkpro.statistics.agreement.coding.CodingAnnotationStudy;
import org.dkpro.statistics.agreement.coding.CohenKappaAgreement;

import de.uni.due.haring.annotation.analyser.annotations.SentenceAnnotation;
import de.uni.due.haring.annotation.analyser.services.AppPrintService;
import de.uni.due.haring.annotation.analyser.services.SentenceAnnotationService;

public class GermEvalAnnotationEvaluator implements AnnotationEvaluator {
    private List<SentenceAnnotation> sentenceAnnotations;

    private int totalSentencesOffensiveGold;
    private int totalSentencesNegativeSentiment;

    private int totalSentencesOffensiveAndNoNegativeSentiment;
    private int totalSentencesNotOffensiveAndNegativeSentiment;
    private int totalSentencesNotOffensiveAndNoNegativeSentiment;

    private int coverageOffensiveAnnotations;

    private double kappaAgreement;
    private double accuracy;

    public GermEvalAnnotationEvaluator() {
	this.sentenceAnnotations = SentenceAnnotationService.getSentencesAnnotations();
    }

    @Override
    public void processAnnotations() {
	totalSentencesOffensiveGold = getTotalSentencesOffensive();
	totalSentencesNegativeSentiment = getTotalSentencesNegativeSentiment();
	coverageOffensiveAnnotations = getCoverageOffensiveAnnotations();

	accuracy = calculateAccuracy();
	kappaAgreement = calculateKappaAgreement();

	totalSentencesOffensiveAndNoNegativeSentiment = getOffensiveNoNegativeSentiment();
	totalSentencesNotOffensiveAndNegativeSentiment = getNotOffensiveNegativeSentiment();
	totalSentencesNotOffensiveAndNoNegativeSentiment = getNotOffensiveAndNoNegativeSentiment();
    }

    @Override
    public void printEvaluationResults() {
	AppPrintService.printSurfaceStructure("");
	AppPrintService.printSurfaceStructure("");

	AppPrintService.printSurfaceStructure("Sentences Total Offensive: " + totalSentencesOffensiveGold);
	AppPrintService
		.printSurfaceStructure("Sentences Total w/negativeSentiment: " + totalSentencesNegativeSentiment);
	AppPrintService.printSurfaceStructure("Sentences Total offensive but no negative sentiment: "
		+ totalSentencesOffensiveAndNoNegativeSentiment);
	AppPrintService.printSurfaceStructure("Sentences Total not offensive but negative sentiment: "
		+ totalSentencesNotOffensiveAndNegativeSentiment);
	AppPrintService.printSurfaceStructure("Sentences Total not offensive and no negative sentiment: "
		+ totalSentencesNotOffensiveAndNoNegativeSentiment);

	AppPrintService.printSurfaceStructure("Coverage Offensive Annotations: " + coverageOffensiveAnnotations);
	AppPrintService.printSurfaceStructure("Kappa Agreement: " + kappaAgreement);

    }

    private int getCoverageOffensiveAnnotations() {
	return sentenceAnnotations.stream().filter(
		sentenceAnnotation -> sentenceAnnotation.isOffensive() && sentenceAnnotation.hasNegativeSentiment())
		.collect(Collectors.toList()).size();
    }

    private int getOffensiveNoNegativeSentiment() {
	return sentenceAnnotations.stream().filter(
		sentenceAnnotation -> sentenceAnnotation.isOffensive() && !sentenceAnnotation.hasNegativeSentiment())
		.collect(Collectors.toList()).size();
    }

    private int getNotOffensiveNegativeSentiment() {
	return sentenceAnnotations.stream().filter(
		sentenceAnnotation -> !sentenceAnnotation.isOffensive() && sentenceAnnotation.hasNegativeSentiment())
		.collect(Collectors.toList()).size();
    }

    private int getTotalSentencesNegativeSentiment() {
	return sentenceAnnotations.stream().filter(sentenceAnnotation -> sentenceAnnotation.hasNegativeSentiment())
		.collect(Collectors.toList()).size();
    }

    private int getTotalSentencesOffensive() {
	return sentenceAnnotations.stream().filter(sentenceAnnotation -> sentenceAnnotation.isOffensive())
		.collect(Collectors.toList()).size();
    }

    private int getNotOffensiveAndNoNegativeSentiment() {
	return sentenceAnnotations.stream().filter(
		sentenceAnnotation -> !sentenceAnnotation.isOffensive() && !sentenceAnnotation.hasNegativeSentiment())
		.collect(Collectors.toList()).size();
    }

    private double calculateAccuracy() {
	return 00;
    }

    private double calculateKappaAgreement() {
	CodingAnnotationStudy study = new CodingAnnotationStudy(2);

	for (SentenceAnnotation sentenceAnnotation : sentenceAnnotations) {
	    boolean personAddressAnnotation = sentenceAnnotation.hasNegativeSentiment();
	    boolean germEvalGoldAnnotation = sentenceAnnotation.isOffensive();
	    study.addItem(personAddressAnnotation, germEvalGoldAnnotation);
	}

	CohenKappaAgreement kappa = new CohenKappaAgreement(study);

	return kappa.calculateAgreement();
    }

}
