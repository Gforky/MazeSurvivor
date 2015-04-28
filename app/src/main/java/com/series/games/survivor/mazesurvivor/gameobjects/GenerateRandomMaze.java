package com.series.games.survivor.mazesurvivor.gameobjects;

import android.os.SystemClock;

/**
 * Created by Malvin on 4/18/2015.
 * Generate a random maze with size M * N (M and N are both even numbers)
 * the start point is the survivor's current position in the maze
 * for any cell in the maze, there is one and only one path between them
 * the exit is the cell with the max cost to the survivor
 * 'p' means available path, 'w' means walls, 's' means the survivor, 'e' means exit
 */
public class GenerateRandomMaze {

    private int maxCost;//record the global max cost from a path cell to the survivor
    private int[] exitCell = new int[2];//record the cell indices of exit in the maze
    private MazeWorld.Cell[][] maze;//Currently used maze
    private int row;//maze's row number
    private int col;//maze's column number
    private int maxNumOfMonster;//Max num of monsters that move in the maze
    private Monster[] monsters;//store all the monsters
    private int maxNumOfTrap;//Max num of traps in the maze
    private Trap[] traps;//store all the traps
    //Valid cost from trap and monster to the survivor
    private int costFromMonsterToSurvivor;
    private int costFromTrapToSurvivor;
    //BonusTime delayers
    private BonusTime[] bonusTimes;
    private int maxNumOfBonusTime;
    private int costFromBonusTimeToSurvivor;

    public void generateMaze(MazeWorld.Cell[][] maze, int startX, int startY, int maxNumOfMonster, int maxNumOfTrap) {//main function to generate the maze

        //Initialize the maze
        this.maze = maze;
        this.row = maze.length;
        this.col = maze[0].length;

        //Initialize the max cost as minimum int
        maxCost = Integer.MIN_VALUE;
        costFromMonsterToSurvivor = 40;
        costFromTrapToSurvivor = 20;

        //Initializations for monsters
        this.maxNumOfMonster = maxNumOfMonster;
        monsters = new Monster[maxNumOfMonster];

        //Initializations for traps
        this.maxNumOfTrap = maxNumOfTrap;
        traps = new Trap[maxNumOfTrap];

        maxNumOfBonusTime = 3;
        bonusTimes = new BonusTime[maxNumOfBonusTime];
        costFromBonusTimeToSurvivor = 60;

        generatePath(startX, startY, 0);//generate a maze with the matrix and start point
        maze[exitCell[0]][exitCell[1]].Type = 'e';//set the exit with the largest cost to survivor
    }

    /**Recursion: at each level, move the path two steps to a random direction, if valid
     *
     * @param X
     * @param Y
     * @param localMax
     */
    private void generatePath(int X, int Y, int localMax) {

        Dir[] dirs = Dir.values();
        shuffle(dirs);//shuffle the order of the directions, in order to move to random direction at each level
        for(Dir dir : dirs) {//try every direction, check whether can move to
            //move 2 steps
            int nextX = dir.moveX(dir.moveX(X));
            int nextY = dir.moveY(dir.moveY(Y));
            if(valid(nextX, nextY)) {
                maze[dir.moveX(X)][dir.moveY(Y)].Type = 'p';
                if(localMax < maxCost && localMax > costFromMonsterToSurvivor && maxNumOfMonster > 0
                        && canPutMonsterOrTrap(nextX, nextY)) {//check whether can set the cell as a monster
                    //Can create monster, and the cost to the survivor is larger than distance from monster to survivor
                    //if set current cell as a monster, increase costFromMonsterToSurvivor by 40, set the next monster further
                    maze[nextX][nextY].Type = 'm';//set the cell as monster
                    costFromMonsterToSurvivor += 20;
                    //create a monster by using current coordinates
                    monsters[maxNumOfMonster - 1] = new Monster(nextX, nextY, SystemClock.uptimeMillis());
                    maxNumOfMonster--;//decrease the available number of monsters by 1
                } else if(localMax < maxCost && localMax > costFromTrapToSurvivor && maxNumOfTrap > 0
                        && canPutMonsterOrTrap(nextX, nextY)) {//check if current cell can set as trap

                    maze[nextX][nextY].Type = 't';
                    costFromTrapToSurvivor += 20;
                    traps[maxNumOfTrap - 1] = new Trap(nextX, nextY, SystemClock.uptimeMillis());
                    maxNumOfTrap--;
                } else if(localMax < maxCost && localMax > costFromBonusTimeToSurvivor && maxNumOfBonusTime > 0
                        && canPutMonsterOrTrap(nextX, nextY)) {

                    maze[nextX][nextY].Type = 'b';
                    costFromBonusTimeToSurvivor += 60;
                    bonusTimes[maxNumOfBonusTime - 1] = new BonusTime(nextX, nextY);
                    maxNumOfBonusTime--;
                } else {
                    maze[nextX][nextY].Type = 'p';
                }
                generatePath(nextX, nextY, localMax + 2);//continue generate the path from the new cell
            } else {//check whether the cell can be set as the exit, and temporarily record the indices of the cell
                if(localMax > maxCost) {//find a larger cost path to the survivor, set the cell as exit
                    //update the exit indices and global max cost
                    exitCell[0] = X;
                    exitCell[1] = Y;
                    maxCost = localMax;
                }
            }
        }//end-for
    }

    //shuffle the direction array to get a random order directions
    private void shuffle(Dir[] dirs) {
        for(int index = 0; index < dirs.length; index++) {
            int random = (int)(Math.random() * index);
            //swap the current direction with the randomly chose direction
            Dir temp = dirs[random];
            dirs[random] = dirs[index];
            dirs[index] = temp;
        }
    }

    /**check if the destination cell is valid to move to
     *
     * @param x
     * @param y
     * @return
     */
    private boolean valid(int x, int y) {
        if(x >= 0 && x < row && y >= 0 && y < col && maze[x][y].Type == 'w') {//cell is with the maze, and is not be dig yet
            return true;
        }
        return false;
    }

    /**Check whether current cell can put a monster / trap or not
     *
     * @param x
     * @param y
     * @return
     */
    private boolean canPutMonsterOrTrap(int x, int y) {

        int leftBorder = y - 5 < 0 ? 0 : y - 5;
        int rightBorder = y + 5 >= col ? col - 1 : y + 5;
        int upBorder = x - 5 < 0 ? 0 : x - 5;
        int downBorder = x + 5 >= row ? row - 1 : x + 5;
        for(int r = upBorder; r <= downBorder; r++) {
            for(int c = leftBorder; c <= rightBorder; c++) {
                if(maze[r][c].Type == 'm' || maze[r][c].Type == 't' || maze[r][c].Type == 'b') {
                //within 11*11 area, already has the monster / trap, can't put a new one ar current position
                    return false;
                }
            }
        }
        return true;
    }

    //get the cost from exit to the survivor
    public int getMaxCost() {

        return maxCost;
    }

    //get the array of monsters
    public Monster[] getMonsters() {

        return monsters;
    }

    //get the trap with corresponding coordinates
    public Trap getTrap(int x, int y) {

        for(Trap trap : traps) {
            if(trap != null && trap.getX() == x && trap.getY() == y) {
                return trap;
            }
        }
        return null;
    }

    //get the traps in maze
    public Trap[] getTraps() {

        return traps;
    }
}
