package com.arlania.world.content.triviabot;

import java.util.ArrayList;

import com.arlania.world.entity.impl.player.Player;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;

/**
 * @author Bradley Englehart - May 1, 2019 10:54 final end time (Fully programmed)
 * Partially based on original script by Arlania devs
 */

public class TriviaBotHandler {
    public static ArrayList<String> placeCount = new ArrayList<String>();
    private static Stopwatch timer = new Stopwatch().reset();
    public static final int timeInterval = 600000; //Sends/Repeats the question every 10 minutes
    public static String answerToQuestion = "null";
    private static String questionToAsk = "null";
    private static int timesAnswered;
    private static int timesAnsweredMax = 3;

    public static void sequence() {
        if (timer.elapsed(timeInterval) && questionToAsk.equals("null")) {
            timer.reset();
            askQuestion();
        } else if (timer.elapsed(timeInterval) && !questionToAsk.equals("null")){
            timer.reset();
            repeatQuestion();
        }
    }

    public static void attemptAnswer(Player p , String attempt) {
        if (attempt.contains("_")) {
            attempt.replaceAll("_", " ");
        }
        attempt = attempt.toLowerCase();

        if (!questionToAsk.equals("null")) {
            if (attempt.equals(answerToQuestion)) {
                if (timesAnswered < timesAnsweredMax) {
                    timesAnswered ++;
                }

                if (placeCount.size() == 0) {
                    appendWinners(p, timesAnswered);
                } else {
                    if (placeCount.get(placeCount.size() - 1).equals(p.getUsername())) {
                        p.sendMessage("[<col=255>Trivia</col>]You have already answered the question succesfully!");
                        return;
                    } else {
                        appendWinners(p, timesAnswered);
                    }
                }

                if (timesAnswered == timesAnsweredMax) {
                    World.sendChannelMessage("Trivia", "The question has been answered. The winners are: ");
                    sendWinners();
                    resetQuestion();
                    return;
                }
            } else {
                p.sendMessage("[<col=255>Trivia</col>] Sorry, the answer you have entered is incorrect.");
                return;
            }
        } else {
            p.sendMessage("The trivia bot currently has no question to be answered");
        }
    }

    public static boolean acceptingQuestion() {
        return !answerToQuestion.equals("null");
    }

    public static void sendWinners() {
        StringBuilder winMessage = new StringBuilder();
        for (int i = 0; i < placeCount.size(); i++) {
            winMessage.append((i + 1) + appendPlaceSuffix(i + 1) + ": " + placeCount.get(i) + " ");
        }
        World.sendChannelMessage("Trivia", String.valueOf(winMessage));
    }

    private static void askQuestion() {
        TriviaBotData td = TriviaBotData.getRandomQA();
        answerToQuestion = td.getAnswer();
        questionToAsk = td.getQuestion();
        World.sendChannelMessage("Trivia",  questionToAsk);
    }

    private static void resetQuestion() {
        answerToQuestion = "null";
        questionToAsk = "null";
        placeCount.clear();
        timesAnswered = 0;
    }

    private static void repeatQuestion() {
        World.sendChannelMessage("Trivia",  questionToAsk);
    }

    private static void appendWinners(Player p, int place) {
        int pointsToAdd = 0;
        placeCount.add(p.getUsername());
        switch (place) {
            case 1:
                pointsToAdd = 10;
                break;

            case 2:
                pointsToAdd = 5;
                break;

            case 3:
                pointsToAdd = 3;
                break;
            default:
                pointsToAdd = 1;
        }

        p.getPointsHandler().incrementTriviaPoints(p, pointsToAdd);
        p.sendMessage("You Received @red@" + pointsToAdd + " @bla@trivia points for placing in " + place + appendPlaceSuffix(place));
    }

    private static String appendPlaceSuffix(int place) {
        switch (place) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}

