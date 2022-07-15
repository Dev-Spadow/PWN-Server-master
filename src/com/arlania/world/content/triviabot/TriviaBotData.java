package com.arlania.world.content.triviabot;

import java.util.Random;

/**
 *
 * @author Bradley Englehart - May 1, 2019 8:57pm Framework Completion Time 2 minutes
 *
 */

public enum TriviaBotData {
    QUESTION_0("What is brad's favorite drink", "coffee"),
    QUESTION_1("What continent has the largest land mass?", "asia"),
    QUESTION_2("What magic level do you need to cast high alchemy?", "55"),
    QUESTION_3("Which NPC drop's the Meat Tenderizer", "chaos elemental"),
    QUESTION_4("What is the max combat level in-game?", "126"),
    QUESTION_5("Who is the server owner?", "boomer"),
    QUESTION_6("What combat level is Skotizo?", "321"),
    QUESTION_7("What year did Runescape 2 release", "2004"),
    QUESTION_8("Who is the strongest NPC in the game", "skotizo"),
    QUESTION_9("What is the highest rank on Pwnlite?", "owner"),
    QUESTION_10("Quick Maths: 0+2+5+8*16 = ?", "135"),
    QUESTION_11("Who was the best Runescape player from 2004 till 2007?", "zezima"),
    QUESTION_12("In osrs who is the king of pvm?", "woox"),
    QUESTION_13("What year did Runescape release?", "2001"),
    QUESTION_14("Who was the number one player on runescape in 2008?", "yogosun"),
    QUESTION_15("Who is the developer of Pwnlite", "brad"),
    QUESTION_16("How many NPC's are located at ::train", "44"),
    QUESTION_17("In what year did motherlode mine get released?", "2014"),
    QUESTION_18("Finish the Lyric: I kissed a girl", "and i liked it"),
    QUESTION_19("What was the original name of Runescape?", "deviousmud"),
    QUESTION_20("What is the world of runescape called?", "gielinor"),
    QUESTION_21("What animal bends their legs at their ankles and not their knees?", "flamingos"),
    QUESTION_22("How many hearts to Octopus' have?", "3"),
    QUESTION_23("Who is the demon barber of Fleet Street?", "sweeney todd"),
    QUESTION_24("Finish the Lyric: I want it", "that way"),
    QUESTION_25("What animal tastes with their hind feet?", "butterflies"),
    QUESTION_26("A group of crows is called?", "murder"),
    QUESTION_27("A donkey will sink in quicksand, but what animal won't?", "mule"),
    QUESTION_28("What's the worlds most shoplifted item?", "bible"),
    QUESTION_29("What male sea creature carries eggs?", "seahorse"),
    QUESTION_30("Where is it illegal to carry icecream in yur back pocket?", "kentucky"),
    QUESTION_31("What is a Dart Board made from?", "horse hair"),
    QUESTION_32("How many licks does it take to get to the centre of a Tootsie pop", "364"),
    QUESTION_33("Just like Sid from Ice Age, Cookie Monster has a name. What is it?", "sid"),
    QUESTION_34("What's the proper name for the toothpaste that sits on your toothbrush?", "nurdle"),
    QUESTION_35("In the movie, 'The Little Mermaid', what did Scuttle call a fork?", "dinglehopper"),
    QUESTION_36("What is the name of the sugary substance that many insects get from flowers?", "nectar"),
    QUESTION_37("First to type n3ywodr8eh323547unsl", "n3ywodr8eh323547unsl"),
    QUESTION_38("First to type to2zvgtpiha54x5fddjf", "to2zvgtpiha54x5fddjf"),
    QUESTION_39("First to type 0f7mnbydmwaiha668avg", "0f7mnbydmwaiha668avg"),
    QUESTION_40("First to type 7tlm8hrqoz9obc7mh3qm", "7tlm8hrqoz9obc7mh3qm"),
    QUESTION_41("First to type 9m7youf1vr2qs61ilg4l", "9m7youf1vr2qs61ilg4l"),
    QUESTION_42("Quick Maths: -8 * -16 - 15 = ?", "113"),
    QUESTION_43("Quick Maths: 50 * 50 = ?", "2500"),
    QUESTION_44("Quick Maths: 170 * 352 / 3 = ?", "19946"),
    QUESTION_45("Quick Maths: 1 + 1 - 1 + 1 = ?", "3"),
    QUESTION_46("Quick Maths: (13 + 17 - 5) * (134 + 1524 - 1000 + 2) * 0 = ?", "0"),
    QUESTION_47("Quick Maths: What is the square root of 12 to the power 2", "12"),
    QUESTION_48("What does Earth do to cause night and day?", "rotates"),
    QUESTION_49("Which cheese has blue mold in it?", "gorgonzola cheese"),
    QUESTION_50("What volatile gas was responsible for the Hindenburg disaster in 1937?", "hydrogen");

    public String question, answer;

    private TriviaBotData(String q, String a) {
        this.question = q;
        this.answer = a;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public static TriviaBotData getRandomQA() {
        int chosenQA = new Random().nextInt(TriviaBotData.values().length);
        return TriviaBotData.values()[chosenQA];
    }
}

