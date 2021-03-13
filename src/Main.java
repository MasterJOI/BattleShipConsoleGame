import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Ship {
    String name;
    int cells;
    int[] coords;
    boolean destroyed = false;

    public Ship(String name, int cells) {
        this.name = name;
        this.cells = cells;
    }

    public Ship(String name, int cells, int[] coords) {
        this.name = name;
        this.cells = cells;
        this.coords = coords;
    }

    public String getName() {
        return name;
    }

    public int getCells() {
        return cells;
    }

    public int[] getCoords() {
        return coords;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }

    public void hitShip() {
        this.cells -= 1;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}

class Player {
    String[][] area;
    String[][] hiddenArea;
    List<Ship> ships;

    public Player() {
        this.area = createArea();
        this.hiddenArea = createArea();
        this.ships = createShips();
    }

    public String[][] getArea() {
        return area;
    }

    public String[][] getHiddenArea() {
        return hiddenArea;
    }

    private static List<Ship> createShips() {
        List<Ship> ships = new ArrayList<>();

        Ship aircraft = new Ship("Aircraft Carrier", 5);
        Ship battleship = new Ship("Battleship", 4);
        Ship submarine = new Ship("Submarine", 3);
        Ship cruiser = new Ship("Cruiser", 3);
        Ship destroyer = new Ship("Destroyer", 2);
        ships.add(aircraft);
        ships.add(battleship);
        ships.add(submarine);
        ships.add(cruiser);
        ships.add(destroyer);

        return ships;
    }

    /**
     * Create blank area
     */
    public String[][] createArea() {
        String[][] area = new String[11][11];
        String letters = " ABCDEFGHIJ";
        String[] numbers = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        int len = area.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (i == 0 && j == 0) {
                    area[i][j] = " ";
                } else if (i == 0) {
                    area[i][j] = numbers[j];
                } else if (j == 0) {
                    area[i][j] = String.valueOf(letters.charAt(i));
                } else {
                    area[i][j] = "~";
                }
            }
        }
        return area;
    }

    /**
     * Print current area
     */
    public void printArea() {
        for (String[] strings : area) {
            for (int j = 0; j < area.length; j++) {
                if (j == area.length - 1) {
                    System.out.print(strings[j]);
                } else {
                    System.out.print(strings[j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printHiddenArea() {
        for (String[] strings : hiddenArea) {
            for (int j = 0; j < hiddenArea.length; j++) {
                if (j == hiddenArea.length - 1) {
                    System.out.print(strings[j]);
                } else {
                    System.out.print(strings[j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Adding ship to area
     */
    public void fillArea() {
        for (int k = 0; k < ships.size(); k++) {
            boolean isRight = false;
            int[] coords = new int[4];
            Ship currentShip = ships.get(k);
            while (!isRight) {
                coords = getShipCoordinates(currentShip);
                isRight = inputCheck(area, coords, currentShip);
            }
            currentShip.setCoords(coords);
            for (int i = coords[0]; i <= coords[2]; i++) {
                for (int j = coords[1]; j <= coords[3]; j++) {
                    area[i][j] = "O";
                }
            }
            printArea();
        }

    }

    /**
     * Get input coordinates of ship
     */
    public int[] getShipCoordinates(Ship ship) {
        Scanner scanner = new Scanner(System.in);
        int[] coords = new int[4];
        System.out.println("Enter the coordinates of the " + ship.getName() + " (" + ship.getCells() + " cells):\n");
        String coord1 = scanner.next("[A-Z]\\d+");
        String coord2 = scanner.next("[A-Z]\\d+");
        coords[0] = Main.Letters.valueOf(coord1.substring(0, 1)).getNumber();
        coords[1] = Integer.parseInt(coord1.substring(1));
        coords[2] = Main.Letters.valueOf(coord2.substring(0, 1)).getNumber();
        coords[3] = Integer.parseInt(coord2.substring(1));
        if (coords[0] > coords[2]) {
            int temp = coords[0];
            coords[0] = coords[2];
            coords[2] = temp;
        } else if (coords[1] > coords[3]) {
            int temp = coords[1];
            coords[1] = coords[3];
            coords[3] = temp;
        }
        return coords;
    }

    /**
     * Get input coordinates of shot
     */
    public int[] getShootCoordinates() {
        Scanner scanner = new Scanner(System.in);
        int[] coords = new int[2];
        String coord1 = scanner.next("[A-Z]\\d+");
        try {
            coords[0] = Main.Letters.valueOf(coord1.substring(0, 1)).getNumber();
            coords[1] = Integer.parseInt(coord1.substring(1));
            if (coords[1] > 10 || coords[1] < 1) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error! You entered the wrong coordinates! Try again:\n");
            return getShootCoordinates();
        }

        return coords;
    }

    /**
     * Checking coordinates for mistakes
     */
    public boolean inputCheck(String[][] area, int[] coords, Ship ship) {
        int row1 = coords[0];
        int col1 = coords[1];
        int row2 = coords[2];
        int col2 = coords[3];
        if (row1 != row2 && col1 != col2) {
            System.out.println("Error! Wrong ship location! Try again:\n");
            return false;
        } else if (Math.abs(row2 - row1) != ship.getCells() - 1 && Math.abs(col2 - col1) != ship.getCells() - 1) {
            System.out.println("Error! Wrong length of the " + ship.getName() + "! Try again:\n");
            return false;
        } else {
            if (ship.getCells() != 5) {
                for (int i = row1 - 1; i <= row2 + 1; i++) {
                    for (int j = col1 - 1; j <= col2 + 1; j++) {
                        if (i >= 0 && i <= 10 && j >= 0 && j <= 10) {
                            if ("O".equals(area[i][j])) {
                                System.out.println("Error! You placed it too close to another one. Try again:\n");
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Shoot at the entered coordinates
     */
    public boolean shootOnce(int[] coord) {
        int damagedShipNum = checkShip(area, hiddenArea, coord);
        boolean isAllDestroyed = true;
        if (damagedShipNum != -1) {
            for (int i = 0; i < ships.size(); i++) {
                if (i != damagedShipNum && !ships.get(i).isDestroyed()) {
                    isAllDestroyed = false;
                    break;
                }
            }
            if (isAllDestroyed && ships.get(damagedShipNum).isDestroyed()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                return true;
            } else if (ships.get(damagedShipNum).isDestroyed()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You hit a ship!");
            }
        } else if ("X".equals(area[coord[0]][coord[1]])) {
            System.out.println("You hit a ship!");
        } else {
            area[coord[0]][coord[1]] = "M";
            hiddenArea[coord[0]][coord[1]] = "M";
            System.out.println("You missed.");
        }
        return false;
    }

    /**
     * Check if we damaged a ship
     */
    private int checkShip(String[][] area, String[][] hiddenArea, int[] coord) {
        for (int k = 0; k < ships.size(); k++) {
            Ship ship = ships.get(k);
            int[] coords = ship.getCoords();
            for (int i = coords[0]; i <= coords[2]; i++) {
                for (int j = coords[1]; j <= coords[3]; j++) {
                    if (i == coord[0] && j == coord[1]) {
                        if ("O".equals(area[coord[0]][coord[1]])) {
                            area[coord[0]][coord[1]] = "X";
                            hiddenArea[coord[0]][coord[1]] = "X";
                            ship.hitShip();
                            if (ship.getCells() == 0) {
                                ship.setDestroyed(true);
                            }
                            return k;
                        }
                    }
                }
            }
        }
        return -1;
    }
}

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();

        System.out.println("Player 1, place your ships on the game field\n");
        player1.printArea();
        player1.fillArea();
        promptEnterKey();

        System.out.println("Player 2, place your ships on the game field\n");
        player2.printArea();
        player2.fillArea();
        promptEnterKey();

        boolean isEnd = false;
        int playerNum = 1;
        while (!isEnd) {
            if (playerNum == 1) {
                player2.printHiddenArea();
                System.out.println("---------------------");
                player1.printArea();
                System.out.println("Player 1, it's your turn:\n");
                isEnd = player2.shootOnce(player1.getShootCoordinates());
                playerNum = 2;
            } else {
                player1.printHiddenArea();
                System.out.println("---------------------");
                player2.printArea();
                System.out.println("Player 2, it's your turn:\n");
                isEnd = player1.shootOnce(player2.getShootCoordinates());
                playerNum = 1;
            }
            if (!isEnd) {
                promptEnterKey();
            }
        }
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Letters {
        A(1),
        B(2),
        C(3),
        D(4),
        E(5),
        F(6),
        G(7),
        H(8),
        I(9),
        J(10);

        private final int number;

        Letters(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
}
