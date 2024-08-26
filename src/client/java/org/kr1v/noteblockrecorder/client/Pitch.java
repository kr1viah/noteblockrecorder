package org.kr1v.noteblockrecorder.client;

public class Pitch {
public static class Result {
    int closestInteger;
    int leftover;

    public Result(int closestInteger, int leftover) {
        this.closestInteger = closestInteger;
        this.leftover = leftover;
    }
}

    public static Result mapValueToIntegerWithLeftover(float x) {
        // Calculate y using the derived formula: y = 12 * log2(x) + 44
        float y = (float) (12 * (Math.log(x) / Math.log(2)) + 44);
        int closestInteger = Math.round(y);

        // Calculate the leftover as a float value
        float fractionalPart = y - closestInteger;

        // Scale the fractional part to the range -50 to 49
        int leftover = Math.round(fractionalPart * 100);

        // Adjust leftover to fit within the range -50 to 49
        if (leftover == 50) {
            leftover = -50;
            closestInteger += 1;
        }

        return new Result(closestInteger, leftover);
    }

    public static int key(float input) {
        Result result = mapValueToIntegerWithLeftover(input);
        return result.closestInteger;
    }
    public static int pitch(float input) {
        Result result = mapValueToIntegerWithLeftover(input);
        return result.leftover;
    }
}
