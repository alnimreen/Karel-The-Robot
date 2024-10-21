import stanford.karel.SuperKarel;

public class Homework extends SuperKarel {

    int beepers = 0, moved = 0;

    public void run() {
        beepers = 0;
        moved = 0;
        int row = getDim(), column = getDim();
        setBeepersInBag(1000);
        System.out.println("The Map Dimensions: ( " + column + " , " + row + " )" + "\nDividing The Map Started...");
        divideMap(row, column);
        System.out.println("Mission Done...\nReturning Origin...");
        returnToOrigin();
        System.out.println("The Steps Count: " + moved);
        System.out.println("Number Of Beepers Used: " + beepers + "\n----------------");
    }

    private void divideMap(int x, int y) {
        if (x < 3 || y < 3) {
            if (x == 1 && y == 1)
                System.out.println("The map can't be divided");
            else handleSpecialCases(x, y);
        }           // 4 Champers
        else if (y % 2 != 0 && x % 2 != 0) {          // odd row && odd column
            fillcolumn(x, y);
            fillrow(x, y);
        } else if (y % 2 != 0 && x % 2 == 0) {          // odd row && even column
            divideLine(x);
            turnLeft();
            oneEven( y);
            fillrow(x, y);
        } else if (x % 2 != 0 && y % 2 == 0) {           // odd column  && even row
            fillcolumn(x, y);
            divideLine(x);
            turnLeft();
            divideLine(y);
            turnLeft();
            oneEven( x);
        } else if (x % 2 == 0 && x == y) {                    // square map (x==y) for even axis
            diagonal(x, 1);
            countBeepers();
            turnRight();
            for (int i = 1; i < x; i++)
                keepMoving();
            diagonal(x, 2);
        } else if (x % 2 == 0 && y % 2 == 0) {           // even column  && even row
            fillColumTogether(x, y, 1);
            turnLeft();
            divideLine(x);
            turnLeft();
            fillColumTogether(y, x, 1);
        }
    }

    private void handleSpecialCases(int x, int y) {
        if (x == 1 || y == 1) {                                   // Divide the map that the one of its axis = 1
            if (x == 1 && y >= 8) {                               //width is 1 and height >=8
                divideMoreEight(y);
            } else if (x == 1 && y < 8) {                            //width is 1 and height less than 8
                turnLeft();
                divideLessEight(1, y, x);
            } else if (y == 1 && x >= 8) {                              //height is 1 and width >=8
                turnRight();
                divideMoreEight(x);
            } else if (y == 1 && x < 8) {                                //height is 1 and width less than 8
                divideLessEight(1, x, y);
            }
        } else if (x == 2 || y == 2) {                                 // Divide the map that the one of its axis = 2
            if (x == 2 && y == 2) {
                diagonal(x, 1);
            } else if (x == 2 && y >= 8) {                                  //width is 2 and height >=8
                divideMoreEight(y);
                turnRight();
                keepMoving();
            } else if (x == 2 && y < 8) {                                //width is 2 and height less than 8
                int lineNum = 1;
                turnLeft();
                divideLessEight(1, y, x);
                turnRight();
                keepMoving();
                turnRight();
                if (y == 3) lineNum = 2;
                divideLessEight(lineNum, y, x);
            } else if (y == 2 && x >= 8) {                              //height is 2 and width >=8
                turnRight();
                divideMoreEight(x);
            } else if (y == 2 && x < 8) {                           //height is 2 and width <8
                int lineNum = 1;
                divideLessEight(1, x, y);
                turnLeft();
                keepMoving();
                turnLeft();
                if (x == 3) lineNum = 2;
                divideLessEight(lineNum, x, y);
            }
        }
    }

    private void divideMoreEight(int length) {
        turnLeft();
        int k;
        while (frontIsClear()) {
            if (length % 4 == 0) {              //determine whether it can divide the line into exactly four equal parts.
                k = length / 4;                 //If the length is, say, 12, it can be divided into four equal segments of 3 step each.
                fillAboveBelow();
                while (frontIsClear()) {
                    for (int i = 0; i < k; i++) {
                        if (frontIsClear()) keepMoving();
                    }
                    if (frontIsClear()) fillAboveBelow();
                }
            } else {
                fillAboveBelow();
                keepMoving();
                length--;
            }
        }
    }

    private void divideLessEight(int lineNum, int ax2, int ax1) {
        if (lineNum % 2 == 0 || (ax1 == 2 && ax2 == 6))             //special case for 3 and 6
            countBeepers();
        while (frontIsClear()) {
            if (ax2 == 5 && ax1 == 2) {                    //special case for 5
                keepMoving();
                putBeepersAndMove(2);
                keepMoving();
                keepMoving();
                countBeepers();
                return;
            }
            keepMoving();
            lineNum++;
            if (lineNum % 2 == 0) countBeepers();
        }
    }

    private void oneEven( int col) {
        for (int i = 0; i < Math.round(col / 4.0); i++) {
            countBeepers();
            if (i != Math.round(col / 4.0) - 1) keepMoving();
        }
        turnRight();
        keepMoving();
        turnLeft();
        if (col % 4 == 2 || col % 4 == 3) countBeepers();
        keepMoving();
        long counter = col - 2 * (Math.round(col / 4.0));
        for (int i = 0; i < counter; i++) {
            countBeepers();
            keepMoving();
        }
        if (col % 4 > 1 ) countBeepers();
        turnLeft();
        keepMoving();
        turnRight();
        for (int i = 1; i < Math.round(col / 4.0); i++) {
            countBeepers();
            keepMoving();
        }
        countBeepers();
    }

    private void fillAboveBelow() {
        countBeepers();
        if (rightIsClear()) putBeeperAbove();
        else if (leftIsClear()) putBeeperBelow();
    }

    private void fillrow(int row, int col) {
        int r = row;
        if (row % 2 == 0) {
            r = row - 1;
            turnLeft();
        }
        divideLine(r);
        turnLeft();
        divideLine(col);
        turnLeft();
        putBeepersAndMove(row);
    }

    private void fillcolumn(int row, int col) {
        divideLine(row);
        turnAround();
        turnRight();
        putBeepersAndMove(col);
        turnLeft();
    }

    private void fillColumTogether(int row, int col, int lineNum) {
        divideLine(row);
        for (int i = 0; i < (2 * col) - 1; i++) {
            if (lineNum == 1) {
                if (i % 2 == 0) {
                    countBeepers();
                    keepMoving();
                    turnLeft();
                    lineNum = 2;
                    countBeepers();
                } else {
                    countBeepers();
                    keepMoving();
                    turnRight();
                }
            } else {
                if (i % 2 != 0) {
                    countBeepers();
                    keepMoving();
                    turnLeft();
                } else {
                    countBeepers();
                    keepMoving();
                    turnRight();
                    lineNum = 1;
                    countBeepers();
                }
            }
        }
    }

    private void divideLine(int m) {
        double mid = Math.ceil((double) m / 2);
        while (mid > 1) {
            keepMoving();
            mid--;
        }
    }

    private void putBeepersAndMove(int m) {
        for (int i = 1; i < m; i++) {
            countBeepers();
            keepMoving();
        }
        countBeepers();
    }

    private void diagonal(int x, int digNum) {
        for (int i = 1; i < x; i++) {
            countBeepers();
            if (digNum == 1) turnLeft();
            else if (digNum == 2 && i == 1) turnAround();
            else turnRight();
            keepMoving();
            if (digNum == 1) turnRight();
            else turnLeft();
            keepMoving();
            countBeepers();
        }
    }

    private void countBeepers() {
        if (!beepersPresent()) {
            putBeeper();
            beepers++;
        }
    }

    private int getDim() {
        int x = 1;
        while (frontIsClear()) {
            keepMoving();
            x++;
        }
        turnLeft();
        return x;
    }

    private void returnToOrigin() {
        if (facingEast()) turnAround();
        if (facingNorth()) turnLeft();
        while (frontIsClear()) {
            keepMoving();
        }
        turnLeft();
        while (frontIsClear()) {
            keepMoving();
        }
        turnLeft();
    }

    private void keepMoving() {
        move();
        moved++;
    }

    public void putBeeperAbove() {
        turnRight();
        keepMoving();
        if (noBeepersPresent()) putBeeper();
        turnAround();
        keepMoving();
        turnRight();
    }

    public void putBeeperBelow() {
        turnLeft();
        keepMoving();
        if (noBeepersPresent()) putBeeper();
        turnAround();
        keepMoving();
        turnLeft();
    }
}
