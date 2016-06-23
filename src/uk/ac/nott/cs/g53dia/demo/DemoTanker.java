package uk.ac.nott.cs.g53dia.demo;
import uk.ac.nott.cs.g53dia.library.*;
import java.util.*;
import java.util.Arrays;
import java.nio.file.Files;
//Currently just tries to move in an unknown direction index of -1.


/**
 * A simple example tanker that chooses actions.
 * 
 * @author Julian Zappala
 */
/*
 * 
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class DemoTanker extends Tanker {
	
    private int exploreStep = 0;
	//x, y stored as int array
    private ArrayList<int[]> seenWells = new ArrayList<int[]>();
    private ArrayList<int[]> seenStations = new ArrayList<int[]>();
    private ArrayList<int[]> seenTasks = new ArrayList<int[]>();
	private int tankerXPos = 0;
	private int tankerYPos = 0;
	private int[] fuelStationPos = new int[]{0,0};
    private boolean initialExplorationToDo = true;
    private int explorationStage = 0;
	private ArrayList<int[]> initialExplorationLocations = new ArrayList<int[]>();
	private ArrayList<int[]> directionsList = new ArrayList<int[]>();
	
	public DemoTanker() {
	/*
	Initialisation was done like this to help conceptualising of the link between
	the index (which will be passed to the moveAction) and the direction.
	*/
	int[] NORTH = {0,-1};
	int[] SOUTH = {0,1};
	int[] EAST = {1,0};
	int[] WEST = {-1,0};
	int[] NORTH_EAST = {1,-1};
	int[] NORTH_WEST = {-1,-1};
	int[] SOUTH_EAST = {1,1};
	int[] SOUTH_WEST = {-1,1};
	directionsList.add(NORTH);
	directionsList.add(SOUTH);
	directionsList.add(EAST);
	directionsList.add(WEST);
	directionsList.add(NORTH_EAST);
	directionsList.add(NORTH_WEST);
	directionsList.add(SOUTH_EAST);
	directionsList.add(SOUTH_WEST);	
    
	//adding the exploration routes positions.
    initialExplorationLocations.add(new int[]{0, 0});
    initialExplorationLocations.add(new int[]{12, 12});
    initialExplorationLocations.add(new int[]{-12, 36});
    initialExplorationLocations.add(new int[]{-36, 12});
    initialExplorationLocations.add(new int[]{-12, -12});
    initialExplorationLocations.add(new int[]{0,0});
    initialExplorationLocations.add(new int[]{12, -12});
    initialExplorationLocations.add(new int[]{-12, -36});
    initialExplorationLocations.add(new int[]{-36, -12});
    initialExplorationLocations.add(new int[]{-12, 12});
    initialExplorationLocations.add(new int[]{0, 0});
    initialExplorationLocations.add(new int[]{12, 12});
    initialExplorationLocations.add(new int[]{36, -12});
    initialExplorationLocations.add(new int[]{12, -36});
    initialExplorationLocations.add(new int[]{-12, -12});
    initialExplorationLocations.add(new int[]{0, 0});
    initialExplorationLocations.add(new int[]{-12, 12});
    initialExplorationLocations.add(new int[]{12, 36});
    initialExplorationLocations.add(new int[]{36, 12});
    initialExplorationLocations.add(new int[]{12, -12});
    initialExplorationLocations.add(new int[]{0, 0});    
	}
	
    public Action senseAndAct(Cell[][] view, long timestep) {
		updateState(view);
		//This can be removed if printing of the end score not wanted.
		if (timestep == 100000)
		{
			System.out.println("Final Score: " + getScore());
		}
        if (initialExplorationToDo){
            return initialExploration(view, timestep);
        }
        else if (seenTasks.size() >= 1){
            return deliverWater(view, timestep);
        }
        else {
            return subsequentExploration(view, timestep); 	
        }
    }
	
	private Action subsequentExploration(Cell[][] view, long timestep){
		explorationStage = 0;
        initialExplorationToDo = true;
        return initialExploration(view, timestep); 
	}
    
	private void updateState(Cell[][] view){
        for (int j = 0; j < view.length; j++ ){
			for (int i = 0; i < view[j].length; i++ ){
                int[] position = new int[]{j+tankerXPos-12, i+tankerYPos-12};
				if (Math.max(Math.abs(position[0]), Math.abs(position[1])) <= 50) 
				{
					if (view[j][i] instanceof Station) {
						if (isInList(seenStations, position) == -1){
							seenStations.add(position);
						}
						Station iteratedStation = (Station)view[j][i];
						if (iteratedStation.getTask() != null){
							int[] taskInfo = new int[]{position[0], position[1], iteratedStation.getTask().getWaterDemand()};
							if (isInList(seenTasks, taskInfo) == -1){
								seenTasks.add(taskInfo);
							}
						}
					}
					else if (view[j][i] instanceof Well && isInList(seenWells, position) == -1) {
						seenWells.add(position);
					}
					else{
						continue;
					}
				}
            }
        }
		return;
    }     
         
    private Action getWater(Cell[][] view){
        if (getCurrentCell(view) instanceof Well){
            return new LoadWaterAction();
        }
        else {
			int closestWellIndex = getClosest(seenWells);
			int[] closestWellPos = seenWells.get(closestWellIndex);
            return moveToPos(view, closestWellPos);
        }
    }
	
    private Action moveToPos(Cell[][] view, int[] pos){
		int horiChange = (pos[0] - tankerXPos);
		int vertChange = (pos[1] - tankerYPos);
		int horiMove;
		int vertMove;
		//horiMove and vertMove could be used to update tanker x and y positions rather than updateTankerPosition
		if (horiChange > 0){
			horiMove = 1; 
		}
		else if (horiChange < 0){
			horiMove = -1;
		}
		else{
			horiMove = 0;
		}
		if (vertChange > 0){
			vertMove = 1;
		}
		else if (vertChange < 0){
			vertMove = -1;
		}
		else {
			vertMove = 0;
		}		
		int[] movementChange = {horiMove, vertMove};
		int movementIndex = isInList(directionsList, movementChange);
		if (movementIndex >= 0){
			int fuelLeft = getFuelLevel() - Math.max(Math.abs(horiChange), Math.abs(vertChange));
			int distanceToFuel = Math.max(Math.abs(pos[0]), Math.abs(pos[1]));
			if (fuelLeft < distanceToFuel){
				return getFuel(view);
			}
			else{
				return moveInDirection(movementIndex);
			}
		}
		else{
			//This should never occur, added during testing
			return moveInDirection(0);
		}
	}

	private int isInList(ArrayList<int[]> arrayList, int[] intArray){
			//This function is never needed to find a second instance so returning from the for loop works fine.
			for (int i = 0; i < arrayList.size(); i++)
			{
				if (Arrays.equals(arrayList.get(i), intArray))
				{
					return i;
				}
			}
			return -1;

	}
	
    private Action moveInDirection(int dir){
        //added to ensure that tanker position is always updated, could probably be condensed.
		updateTankerPosition(dir);
        return new MoveAction(dir);
        }
 
    private Action getFuel(Cell[][] view){
        if (getCurrentCell(view) instanceof FuelPump){
            //should never change the x and y values - included for peace of mind.
			tankerXPos = 0;
			tankerYPos = 0;
			return new RefuelAction();
        }
        else {
            return moveToPos(view, fuelStationPos);
        }
    }

    private Action deliverWater(Cell[][] view, long timestep){
        int taskIndex = getClosest(seenTasks);
		int[] taskInfo = seenTasks.get(taskIndex);
        int[] taskPos = new int[]{taskInfo[0], taskInfo[1]};
		int[] tankerPos = new int[]{tankerXPos, tankerYPos};
		//taskInfo[2] has a very very small chance of being > Max Water, can possibly be 10,001.
		if (taskInfo[2] <= getWaterLevel() || taskInfo[2] > MAX_WATER)
		{
			if (Arrays.equals(taskPos, tankerPos) && getCurrentCell(view) instanceof Station) {
				Station CurrentCellStation = (Station) getCurrentCell(view);
				Task CurrentStationTask = CurrentCellStation.getTask();
				if (CurrentStationTask.getWaterDemand() <= getWaterLevel()){
					seenTasks.remove(taskIndex);
				}
				else{
					/*
					This was added in the small chance that the task required more water than the max
					or design choice changed to allow delivery of part of a task.
					*/
					int newWaterRequirements = seenTasks.get(taskIndex)[2] - getWaterLevel();
					seenTasks.get(taskIndex)[2] = newWaterRequirements;
				}
				return new DeliverWaterAction(CurrentStationTask);
			}
			else {
				return moveToPos(view, taskPos);
			}
		}
		else if (seenWells.size() >= 1){
			return getWater(view);
		}
		else{
			//This should never occur but included incase exploration was decided to not always be run initially.
			return subsequentExploration(view, timestep);
		}
    }

	private int getClosest(ArrayList<int[]> iterableList){
		//starting at furthest possible distance for observed cells.
		int distance = 100;
		int returnableIndex = 0;
		for (int i = 0; i < iterableList.size(); i++ ){
			int newDistance = Math.max(Math.abs(iterableList.get(i)[0] - tankerXPos), Math.abs(iterableList.get(i)[1] - tankerYPos));
			if ( newDistance < distance){
				distance = newDistance;
				returnableIndex = i;
				}
		}
		return returnableIndex;
	}
	
    private Action initialExploration(Cell[][] view, long timestep){
        if (explorationStage >= initialExplorationLocations.size())
        {
            initialExplorationToDo = false;
            return senseAndAct(view, timestep);
        }
        int[] travelLocation = initialExplorationLocations.get(explorationStage);
        if (travelLocation[0] == tankerXPos && travelLocation[1] == tankerYPos){
            explorationStage++;
            if (tankerXPos == 0 && tankerYPos == 0 && getFuelLevel() < MAX_FUEL){
                return new RefuelAction();
            }
            else{
                return initialExploration(view, timestep);
            }
        }
        else{
            return moveToPos(view, travelLocation);
        }
    }
      
	private void updateTankerPosition(int dir){
		switch (dir){
			case 0: 
				tankerYPos--;
				break;
			case 1: 
				tankerYPos++;
				break;
			case 2: 
				tankerXPos++;
				break;
			case 3: 
				tankerXPos--;
				break;
			case 4: 
				tankerXPos++;
				tankerYPos--;
				break;
			case 5: 
				tankerXPos--;
				tankerYPos--;
				break;
			case 6: 
				tankerXPos++;
				tankerYPos++;
				break;
			case 7: 
				tankerXPos--;
				tankerYPos++;
				break;
		}
	}
}

/*
zero is north, one is south, two is east, three is west, 
four is north east, five is north west,
six is south east, seven is south west
*/
