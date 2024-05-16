package scoliosis;

public class LetterInfo {
    // i was gonna store more info but this is sorta silly now...
    Character letter;
    float hopTime;
    float colorFadeTime;
    public LetterInfo(Character letter, long hopTime, long colorFadeTime) {
        this.letter = letter;
        this.hopTime = hopTime;
        this.colorFadeTime = colorFadeTime;
    }
}
