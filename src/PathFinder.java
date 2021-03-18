import java.util.*;

public class PathFinder {

	//list of variables and lists, as well as start and end node objects
	private static PriorityQueue<Node> openList;
	private static Node[][] grid;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private static boolean[][] closedList;
	private int blocks;
	private static int startX;
	private int startY;
	private static int goalX;
	private int goalY;
	static Node startNode;
	static Node goalNode;
	

	//constructor class that takes in start and end coordinates
	public PathFinder(int sX, int sY, int gX, int gY) {
		grid = new Node[15][15];
		//creates open and closed lists for discovered,undiscovered, and used nodes
		 closedList = new boolean[15][15];
		 openList = new PriorityQueue<Node>((Node n1, Node n2) -> {
				return n1.getF() < n2.getF() ? -1 : n1.getF() > n2.getF() ? 1 : 0;
			});
		 
		startNode = new Node(sY, sX, 0);
		goalNode = new Node(gY, gX, 0);
		 //nested for loop to create array of nodes and set Heuristic values
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Node(i, j, 0);
				grid[i][j].setH(Math.abs(i - gY) + Math.abs(j - gX));
				grid[i][j].isGoal = false;
			}
		}
		
		grid[sY][sX].setG(0);
		//randomly implements block nodes throughout array
		Random rnd = new Random();
		while(blocks < 22) {
			int i = rnd.nextInt(grid.length);
			int j = rnd.nextInt(grid[0].length);
			if(grid[i][j].getT() == 0) {
				addBlocks(i, j);
				blocks++;
			}
		}
	}
	//Method that changes a nodes type to create blocks
	public void addBlocks(int r, int c) {
		grid[r][c].setT(1);
	}
	//Method to change F value and updates closed and open lists
	public void updateF(Node c, Node p, int gValue) {
		if (p.getT() == 1 || closedList[p.getCol()][p.getRow()]) {
			return;
		}
		
		int pFValue = p.getF() + gValue;
		boolean oList = openList.contains(p);
		
		if(!oList || pFValue < p.getF()) {
			p.setF();
			p.setParent(c);
			
			if(!oList) {
				openList.add(p);
			}
		}
	}
	//Method containing movement logic and uses F value to find path
	//May throw out of bounds array exception if used on the edges of the grid
	//method and algorithm still works inner portions however
	public void createPath() {
		openList.add(grid[startNode.getCol()][startNode.getRow()]);
		
		Node c;
		
		while(true) {
			c = openList.poll();
			if(c.getT() == 1) {
				break;
			}
			
			closedList[c.getCol()][c.getRow()] = true;
		
			
			if(c.equals(grid[goalNode.getCol()][goalNode.getRow()])) {
				return;
				}
			
				Node temp;
				//If loops that move left or in a downwards diagonal
				if(c.getRow() - 1 >= 0) {
					temp = grid[c.getRow() - 1][c.getCol()];
					updateF(c, temp, c.getF() + 10);
			
				if(c.getCol() - 1 >= 0) {
					temp = grid[c.getRow() - 1][c.getCol() - 1];
					updateF(c, temp, c.getF() + 14);
				}
				
				if(c.getCol() + 1 < grid[0].length) {
					temp = grid[c.getRow() - 1][c.getCol() + 1];
					updateF(c, temp, c.getF() + 14);
				}
			}
			//moves up
			if(c.getCol() - 1 >= 0) {
				temp = grid[c.getRow()][c.getCol() - 1];
				updateF(c, temp, c.getF() + 10);
			}
			//moves down
			if(c.getCol() + 1 < grid[0].length) {
				temp = grid[c.getRow()][c.getCol() + 1];
				updateF(c, temp, c.getF() + 10);
			}// move left
			if(c.getRow() + 1 < grid.length) {
				temp = grid[c.getRow() + 1][c.getCol()];
				updateF(c, temp, c.getF() + 10);
			}
		
			if(c.getCol() - 1 >= 0) {
				temp = grid[c.getRow() - 1][c.getCol() - 1];
				updateF(c, temp, c.getF() + 14);
			}
		
			if(c.getCol() + 1 < grid[0].length) {
				temp = grid[c.getRow()][c.getCol() + 1];
				updateF(c, temp, c.getF() + 14);
			}
		
		}

	}
	//method to show the initial grid before algorithim is performed
	public void showGrid() {
		System.out.println("Grid:");
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(i == goalNode.getRow() && j == goalNode.getCol()) 
					System.out.print("E ");
				 else if(i == startNode.getRow() && j == startNode.getCol()) 
					System.out.print("S ");
				 else if(grid[i][j].getT() == 0) 
					System.out.print(0 + " ");
				 else 
					System.out.print("X ");
				
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//method to show that Manhattan Heuristic is properly calculated and displayed
	public void showGridH() {
		
		System.out.println("Grid of Manhattan Heuristic: ");
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j].getT() == 0) {
					System.out.print(grid[i][j].getH() + " ");
				} else {
					System.out.print("X ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	//method to show path of algorithm from start to finish with 1s, is a little
	//bugged and will sometimes place 1s in a spot that is not on the path
	public void showGridPath() {
		if(closedList[goalNode.getCol()][goalNode.getRow()]) {
			System.out.print("Path taken: ");
			Node d = grid[goalNode.getCol()][goalNode.getRow()];
			System.out.println(d.toString());
			grid[d.getCol()][d.getRow()].isGoal = true;
			
			while(d.getParent() != null) {
				System.out.print(d.getParent().toString() +  " <- ");
				grid[d.getParent().getRow()][d.getParent().getCol()].isGoal = true;
				d = d.getParent();
			}
			
			System.out.println("\n");
			
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[0].length; j++) {
					if(i == startNode.getRow() && j == startNode.getCol()) {
						System.out.print("S ");
					} else if(i == goalNode.getRow() && j == goalNode.getCol()) {
						System.out.print("E ");
					} else if(grid[i][j].isGoal) {
						System.out.print("1 ");
					}else if(grid[i][j].getT() == 0) {	
						System.out.print("0 ");
					} else {
						System.out.print("X ");
					}
				}
				System.out.println();
			}
			System.out.println();
		} else {
			System.out.println("No path found");
		}
	}
	
	//main method to get user input and create 15x15 grid, and deploys A* search algorithm to find path through grid
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter starting node x value: ");
		int x1 = input.nextInt();
		System.out.println("Enter starting node y value: ");
		int y1 = input.nextInt();
		System.out.println("Enter goal node x value: ");
		int x2 = input.nextInt();
		System.out.println("Enter Goal node y value: ");
		int y2 = input.nextInt();
		PathFinder p = new PathFinder(x1, y1, x2, y2);
		p.showGrid();
		p.createPath();
		p.showGridH();
		p.showGridPath();
		
	}
	
}