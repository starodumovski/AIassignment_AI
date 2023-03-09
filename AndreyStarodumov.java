/** ***********************************************************
 *          Author:     Andrey Starodumov                     *
 *          Group:      BS20-05                               *
 *          Mail:       a.starodumov@innopolis.university     *
 *          Subject:    ItoAI                                 *
 * ************************************************************
 */

import java.util.*;

/**
 * Enumerator is for identifying the actors we have
 */
enum Characters {
    Potter (80), Filch (70), Norris (78), Book (66), Cloak (67), Door (68);
    private final int Letter;
    Characters(int Letter) {this.Letter = Letter;}
    public int getValue() {return this.Letter;}
}

/**
 * Main class which contains all needed functionality to find shortest or optimal paths
 */
public class AndreyStarodumov {
    public static final String      ANSI_RESET = "\u001B[0m";
    public static final String      ANSI_RED = "\u001B[31m";
    public static final String      ANSI_BLUE = "\u001B[34m";
    public static final String      ANSI_GREEN = "\u001B[32m";
    static int                      i_see;
    static int[]                    door = new int[2];
    static int[][]                  carta = new int[9][9];
    static boolean                  caught = false;
    static ArrayList<Integer>[][]   ma = new ArrayList[9][9];
    static boolean                  cloak_found;
    static Queue<int[]>             queue = new LinkedList<>();
    static int[][]                  mapik = new int[9][9];
    static int[][]                  sensik_without_cloak = new int[9][9];
    static int[][]                  sensik_with_cloak = new int[9][9];
    static int[][]                  visited = new int[9][9];
    static ArrayList<int[]>         path = new ArrayList<>();
    static ArrayList<int[]>         min_path_book = new ArrayList<>();
    static ArrayList<int[]>         min_path_cloak = new ArrayList<>();
    static ArrayList<int[]>         min_path_book_door_with_cloak = new ArrayList<>();
    static ArrayList<int[]>         min_path_book_door_without_cloak = new ArrayList<>();
    static ArrayList<int[]>         min_path_book_cloak = new ArrayList<>();
    static ArrayList<int[]>         min_path_cloak_door = new ArrayList<>();
    static ArrayList<int[]>         min_path_cloak_book = new ArrayList<>();
    static int                      amount_of_steps = 0;
    static int                      step = -1;

    /**
     * Function to start the process of creating the map and finding solution
     */
    public static void run_program() {
        Scanner in = new Scanner(System.in);

        step_clear();
        if (choose_map(in) == 1) {
            map_generate_2(in);
            System.out.println("The map is automatically generated.");
        } else {
            System.out.println("Enter all data following the assignment rules.");
            hand_map(in);
            System.out.println();
            System.out.println("Your input data is completely correct!");
            System.out.println("Map is successfully created!");
        }
//        show_sense();
        show_map();
        get_info();
        System.out.println();

        resulting_report(1, i_see, timing(1));
        System.out.println();
        resulting_report(2, i_see, timing(2));
    }

    /**
     * the function to clear all used data for the new algorithm work
     */
    public static void renew_initials() {
        step_clear();
        caught = false;
        cloak_found = false;
        queue.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mapik[i][j] = 3;
            }
        }
        path.clear();
        min_path_book.clear();
        min_path_cloak.clear();
        min_path_book_door_with_cloak.clear();
        min_path_book_door_without_cloak.clear();
        min_path_book_cloak.clear();
        min_path_cloak_door.clear();
        min_path_cloak_book.clear();
        amount_of_steps = 0;
    }

    /**
     * Function whic print the small report to find the result of an algorithm
     * @param method the method to run (1-Backtracking, 2-BFS)
     * @param scenario the scenario of the vision
     * @param sum_up two numbers: output of the algorithm and time of its work
     */
    public static void resulting_report(int method, int scenario, long[] sum_up) {
        int path_steps = 0;
        HashSet<String> cords = new HashSet<>();
        if (method == 1) {
            System.out.println("******Backtracking with " + scenario + " scenario******");
        } else {
            System.out.println("******BFS-algorithm with " + scenario + " scenario******");
        }
        if (sum_up[0] == 0) {
            System.out.println("1. Lose");
            System.out.println("2. Overall " + amount_of_steps + " steps");
            System.out.println("3. No way");
            System.out.print("4. ");
            System.out.println("Time taken, ms: " + (double)sum_up[1]/1000000.0);
        } else {
            if (sum_up[0] == 1 || sum_up[0] == 2 || sum_up[0] == 3) {
                System.out.println("1. Win");
            } else {
                System.out.println("1. Lose. Harry was caught");
            }
            System.out.println("2. Overall " + amount_of_steps + " steps");
            System.out.print("3. ");
            if (sum_up[0] == 1) {
                System.out.print("From start to Book:");
                System.out.print("   ");
                for (int[] num : min_path_book) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Door:");
                System.out.print("   ");
                for (int[] num : min_path_book_door_without_cloak) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else if (sum_up[0] == 2) {
                System.out.print("From start to Cloak:");
                System.out.print("   ");
                for (int[] num : min_path_cloak) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Cloak to Book:");
                System.out.print("   ");
                for (int[] num : min_path_cloak_book) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Door:");
                System.out.print("   ");
                for (int[] num : min_path_book_door_with_cloak) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else if (sum_up[0] == 3){
                System.out.print("From start to Book:");
                System.out.print("   ");
                for (int[] num : min_path_book) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Cloak:");
                System.out.print("   ");
                for (int[] num : min_path_book_cloak) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Cloak to Door:");
                System.out.print("   ");
                for (int[] num : min_path_cloak_door) {
                    cords.add(Integer.toString(100 + 10*num[0] + num[1]));
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else {
                System.out.println("Caught ");
            }
            if (scenario == 2) {
                System.out.println("4. Found optimal solution (may not be shortest): " + path_steps + " steps");
            } else {
                System.out.println("4. Shortest path: " + path_steps + " steps");
            }
            System.out.print("5. ");
            System.out.println("Time taken, ms: " + (float) sum_up[1] / 1000000.0);
        }
        for (int i = 8; i >= 0; i--) {
            System.out.print(i + " | ");
            for (int j = 0; j < 9; j++) {
                if (cords.contains(Integer.toString(100+10*i+j))) {
                    if (ma[i][j].isEmpty()) {
                        if (sensik_without_cloak[i][j] == 1) {
                            System.out.print(ANSI_BLUE + "~" +ANSI_RESET);
                            System.out.print(ANSI_RED + "*  " + ANSI_RESET);
                        } else {
                            System.out.print(ANSI_RED + "*   " + ANSI_RESET);
                        }
                    }
                    else {
                        String spaces = "    ";
                        for (int s : ma[i][j]) {
                            Character ca = (char) s;
                            System.out.print(ca);
                            spaces = spaces.substring(0, spaces.length() - 1);
                        }
                        System.out.print(ANSI_RED+"*"+ANSI_RESET);
                        spaces = spaces.substring(0, spaces.length() - 1);
                        System.out.print(spaces);
                    }
                } else if (ma[i][j].isEmpty()) {
                    if (sensik_without_cloak[i][j] == 1) System.out.print(ANSI_BLUE + "~   " + ANSI_RESET);
                    else System.out.print("_   ");
                } else {
                    String spaces = "    ";
                    for (int s : ma[i][j]) {
                        Character ca = (char) s;
                        System.out.print(ca);
                        spaces = spaces.substring(0, spaces.length() - 1);
                    }
                    System.out.print(spaces);
                }
            }
            System.out.println();
        }
        System.out.println("    ------------------------------------");
        System.out.print("    ");
        for (int j = 0; j < 9; j ++) {
            System.out.print(j + " | ");
        }
        System.out.println();
    }

    /**
     * Function to run the needed algorithm and record the time of the work
     * @param method the method to run (1-Backtracking, 2-BFS)
     * @return two numbers: output of the algorithm and time of its work
     */
    public static long[] timing(int method) {
        long output;
        long start;
        long finish;
        long elapsed;
        renew_initials();
        if (method == 1) {
            start = System.nanoTime();
            output = initial_backtracking_path();
            finish = System.nanoTime();
        } else {
            start = System.nanoTime();
            output = initial_bfs_start();
            finish = System.nanoTime();
        }
        elapsed = finish - start;
        return (new long[]{output, elapsed});
    }

    /**
     * Function to clear the map of the amount of steps to the certain cells and the path-list
     */
    public static void step_clear() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                carta[i][j] = 1000000;
            }
        }
        path.clear();
        step = -1;
    }

    /**
     * Function to clear the map os actors and arrays of visited cells and sensations in them
     */
    public static void map_initial() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (ma[i][j] == null) {
                    ma[i][j] = new ArrayList<>();
                } else {
                    ma[i][j].clear();
                }
            }
        }
        sensik_initial();
        visited_initial();
    }

    /**
     * Function to clear sensations in the cells of the map
     */
    public static void sensik_initial() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sensik_without_cloak[i][j] = 0;
                sensik_with_cloak[i][j] = 0;
            }
        }
    }

    /**
     * Function to clear the array of visited cells of the map
     */
    public static void visited_initial() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                visited[i][j] = 0;
            }
        }
    }

    /**
     * Function to return the shortest or optimal path with the 1st algorithm
     * @return the number of result finding shortest or optimal path
     */
    public static int initial_backtracking_path() {
        boolean passed = false;
        backik_path(0, 0, false, Characters.Book, min_path_book);
        if (caught) {
            return 10;
        }
        step_clear();
        if (min_path_book.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_path(0,0, false, Characters.Cloak, min_path_cloak);
                if (caught) {
                    return 20;
                }
                step_clear();
                backik_path(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                        min_path_cloak.get(min_path_cloak.size() - 1)[1], true, Characters.Book, min_path_cloak_book);
                if (caught) {
                    return 21;
                }
                step_clear();
                backik_path(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                        min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1], true, Characters.Door, min_path_book_door_with_cloak);
                if (caught) {
                    return 22;
                }
                step_clear();
                return 2;
            }
        }
        backik_path(min_path_book.get(min_path_book.size() - 1)[0],
                min_path_book.get(min_path_book.size() - 1)[1],
                false, Characters.Door, min_path_book_door_without_cloak);
        if (caught) {
            return 11;
        }
        step_clear();
        if (min_path_book_door_without_cloak.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_path(min_path_book.get(min_path_book.size() - 1)[0],
                        min_path_book.get(min_path_book.size() - 1)[1], false, Characters.Cloak, min_path_book_cloak);
                if (caught) {
                    return 31;
                }
                step_clear();
                backik_path(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                        min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1],
                        true, Characters.Door, min_path_cloak_door);
                if (caught) {
                    return 32;
                }
                if (!min_path_cloak_door.isEmpty()) {
                    passed = true;
                }
                backik_path(0,0, false, Characters.Cloak, min_path_cloak);
                if (caught) {
                    if (passed)
                        return 3;
                    else
                        return 20;
                }
                step_clear();
                if (!min_path_cloak.isEmpty()) {
                    backik_path(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                            min_path_cloak.get(min_path_cloak.size() - 1)[1],
                            true, Characters.Book, min_path_cloak_book);
                    if (caught) {
                        if (passed)
                            return 3;
                        else
                            return 21;
                    }
                    step_clear();
                    backik_path(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                            min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1],
                            true, Characters.Door, min_path_book_door_with_cloak);
                    if (caught) {
                        if (passed)
                            return 3;
                        else
                            return 21;
                    }
                    if (!passed) {
                        return 2;
                    }
                    step_clear();
                } else {
                    if (passed)
                        return 3;
                }
                if (min_path_cloak_book.isEmpty() || min_path_book_door_with_cloak.isEmpty()) {
                    if (passed) return 3;
                }
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                        min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
        backik_path(0,0, false, Characters.Cloak, min_path_cloak);
        if (caught) {
            return 1;
        }
        step_clear();
        if (min_path_cloak.isEmpty()) {
            return 1;
        } else {
            backik_path(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                    min_path_cloak.get(min_path_cloak.size() - 1)[1],
                    true, Characters.Book, min_path_cloak_book);
            if (caught) {
                return 1;
            }
            step_clear();
            backik_path(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                    min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1],
                    true, Characters.Door, min_path_book_door_with_cloak);
            if (caught) {
                return 1;
            }
            step_clear();
            backik_path(min_path_book.get(min_path_book.size() - 1)[0],
                    min_path_book.get(min_path_book.size() - 1)[1],
                    false, Characters.Cloak, min_path_book_cloak);
            if (caught) {
                if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2<
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
                    return 2;
                }
                return 1;
            }
            step_clear();
            backik_path(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                    min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1],
                    true, Characters.Door, min_path_cloak_door);
            if (caught) {
                if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2<
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
                    return 2;
                }
                return 1;
            }
            step_clear();
            if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                    min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2<
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 1) {
                    return 3;
                } else {
                    return 1;
                }
            } else {
                if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2 <
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * the function to find the shortest path to the certain item with 1st algorithm
     * @param raw the index of the raw in the map
     * @param col the index of the column in the map
     * @param cloak_on the flag is true if the cloak is on the Harry
     * @param what_to_find the item we want to find
     * @param where_to_write the list where we want to save our shortest path
     */
    public static void backik_path(int raw, int col, boolean cloak_on,
                                   Characters what_to_find, ArrayList<int[]> where_to_write) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;

        update_memory(raw, col, cloak_on, i_see);
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (i != raw || j != col) {
                        if (visited[i][j] == 0 && (mapik[i][j] == 0 || mapik[i][j] == 3)) {
                            hashSet.add(Integer.toString(100 + 10 * i + j));
                        }
                    }
                }
            }
        }
        amount_of_steps++;
        step++;
        if ((!cloak_on && sensik_without_cloak[raw][col] == 1) || (cloak_on && sensik_with_cloak[raw][col] == 1)) {
            caught = true;
            path.remove(path.size() - 1);
            return ;
        }

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});

        if (!where_to_write.isEmpty()) {
            if (path.size() >= where_to_write.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!cloak_on && !ma[raw][col].isEmpty() && ma[raw][col].contains(Characters.Cloak.getValue())) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains(what_to_find.getValue())) {
            where_to_write.clear();
            where_to_write.addAll(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        while (!hashSet.isEmpty()) {
            for (String cord: hashSet) {
                if (mapik[(Integer.parseInt(cord) - 100)/10][(Integer.parseInt(cord) - 100)%10] == 0 &&
                        visited[(Integer.parseInt(cord) - 100)/10][(Integer.parseInt(cord) - 100)%10] == 0) {
                    pathik = true;
                    break ;
                }
            }
            if (pathik) {
                for (int i = raw + 1; i > raw - 2; i--) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                            if (mapik[i][j] == 0 && visited[i][j] == 0 &&
                                    hashSet.contains(Integer.toString(100 + 10 * i + j))) {
                                hashSet.remove(Integer.toString(100 + 10 * i + j));
                                backik_path(i, j, cloak_on, what_to_find, where_to_write);
                                if (caught) {
                                    visited[raw][col] = 0;
                                    return;
                                }
                            }
                        }
                    }
                }
                pathik = false;
            } else {
                for (int i = raw + 1; i > raw - 2; i--) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                            if (mapik[i][j] == 3 && visited[i][j] == 0 &&
                                    hashSet.contains(Integer.toString(100 + 10 * i + j))) {
                                hashSet.remove(Integer.toString(100 + 10 * i + j));
                                backik_path(i, j, cloak_on, what_to_find, where_to_write);
                                if (caught) {
                                    visited[raw][col] = 0;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = raw + 1; i > raw - 2; i--) {
                for (int j = col - 1; j < col + 2; j++) {
                    if (hashSet.contains(Integer.toString(100 + 10*i + j))) {
                        if (mapik[i][j] == 1) {
                            hashSet.remove(Integer.toString(100 + 10*i + j));
                        }
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }

    /**
     * Function to return the shortest or optimal path with the 2nd algorithm
     * @return the number of result finding shortest or optimal path
     */
    public static int initial_bfs_start() {
        boolean passed = false;

        queue.add(new int[]{0,0});
        while (!queue.isEmpty()) {
            if (bfs_path_2(queue.element().clone(), false, Characters.Book, min_path_book)) {
                queue.clear();
                visited_initial();
            }
            if (caught) return 10;
            queue.clear();
            visited_initial();
        }
        if (min_path_book.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                queue.add(new int[]{0,0});
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), false, Characters.Cloak, min_path_cloak)) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) return 20;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), true, Characters.Book, min_path_cloak_book)) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) return 21;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), true, Characters.Door, min_path_book_door_with_cloak)) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) return 22;
                    queue.clear();
                    visited_initial();
                }
                return 2;
            }
        }
        queue.add(min_path_book.get(min_path_book.size() - 1).clone());
        while (!queue.isEmpty()) {
            if (bfs_path_2(queue.element().clone(), false, Characters.Door, min_path_book_door_without_cloak)) {
                queue.clear();
                visited_initial();
            }
            if (caught) return 11;
            queue.clear();
            visited_initial();
        }
        if (min_path_book_door_without_cloak.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                queue.add(min_path_book.get(min_path_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), false, Characters.Cloak, min_path_book_cloak)) {
                        queue.clear();
                    }
                    if (caught) return 31;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_book_cloak.get(min_path_book_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), true, Characters.Door, min_path_cloak_door)) {
                        passed = true;
                        queue.clear();
                    }
                    if (caught) return 32;
                    queue.clear();
                    visited_initial();
                }
                queue.add(new int[]{0,0});
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), false, Characters.Cloak, min_path_cloak)) {
                        queue.clear();
                    }
                    if (caught) {
                        if (passed) return 3;
                        else return 20;
                    }
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), true, Characters.Book, min_path_cloak_book)) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) {
                        if (passed) return 3;
                        else return 21;
                    }
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_path_2(queue.element().clone(), true, Characters.Door, min_path_book_door_with_cloak)) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) {
                        if (passed) return 3;
                        else return 22;
                    }
                    queue.clear();
                    visited_initial();
                }
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                        min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
        queue.add(new int[]{0,0});
        while (!queue.isEmpty()) {
            if (bfs_path_2(queue.element().clone(), false, Characters.Cloak, min_path_cloak)) {
                queue.clear();
                visited_initial();
            }
            if (caught) {
                return 1;
            }
            queue.clear();
            visited_initial();
        }
        if (min_path_cloak.isEmpty()) {
            return 1;
        } else {
            queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
            while (!queue.isEmpty()) {
                if (bfs_path_2(queue.element().clone(), true, Characters.Book, min_path_cloak_book)) {
                    queue.clear();
                    visited_initial();
                }
                if (caught) {
                    return 1;
                }
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
            while (!queue.isEmpty()) {
                if (bfs_path_2(queue.element().clone(), true, Characters.Door, min_path_book_door_with_cloak)) {
                    queue.clear();
                    visited_initial();
                }
                if (caught) {
                    return 1;
                }
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_book.get(min_path_book.size() - 1).clone());
            while (!queue.isEmpty()) {
                if (bfs_path_2(queue.element().clone(), false, Characters.Cloak, min_path_book_cloak)) {
                    queue.clear();
                    visited_initial();
                }
                if (caught) {
                    if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2 <
                            min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
                        return 2;
                    }
                    return 1;
                }
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_book_cloak.get(min_path_book_cloak.size() - 1).clone());
            while (!queue.isEmpty()) {
                if (bfs_path_2(queue.element().clone(), true, Characters.Door, min_path_cloak_door)) {
                    queue.clear();
                    visited_initial();
                }
                if (caught) {
                    if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2 <
                            min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
                        return 2;
                    }
                    return 1;
                }
                queue.clear();
                visited_initial();
            }
            if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                    min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 1) {
                    return 3;
                } else {
                    return 1;
                }
            } else {
                if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2 <
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * the function to find the shortest path to the certain item with 2nd algorithm
     * @param queue_el element of the queue with the indexes on the map
     * @param cloak_on the flag is true if the cloak is on the Harry
     * @param what_to_find the item we want to find
     * @param where_to_write the list where we want to save our shortest path
     * @return true if ite was found (as it is the shortest path)
     */
    public static boolean bfs_path_2(int[] queue_el,
                                     boolean cloak_on,
                                     Characters what_to_find, ArrayList<int[]> where_to_write) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (cloak_on) {
            if (sensik_with_cloak[raw][col] == 1) {
                caught = true;
                return (false);
            }
        } else {
            if (sensik_without_cloak[raw][col] == 1) {
                caught = true;
                return (false);
            }
        }
        update_memory(raw, col, cloak_on, i_see);

        visited[raw][col] = 1;
        if (!cloak_on && !ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains(what_to_find.getValue())) {
            where_to_write.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if ((mapik[i][j] == 0 || mapik[i][j] == 3) && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (mapik[queue.element()[0]][queue.element()[1]] == 1) {
                visited[queue.element()[0]][queue.element()[1]] = 0;
                queue.remove();
            } else {
                if (bfs_path_2(queue.element().clone(), cloak_on, what_to_find, where_to_write)) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * where_to_write.get(0)[0] +
                                where_to_write.get(0)[1]))) {
                            where_to_write.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }

    /**
     * Function to update the memory of the map of Harry according to his perception
     * @param raw the raw index on the map from where we see
     * @param col the raw index on the map from where we see
     * @param cloak true if the cloak is on the Harry
     * @param scenario the perception scenario
     */
    public static void update_memory(int raw, int col, boolean cloak, int scenario) {
        for (int j = col - 1; j < col + 2; j++) {
            if (j >= 0 && j < 9 && raw+2 >= 0 && raw+2 < 9) {
                if (cloak) {
                    mapik[raw + scenario][j] = sensik_with_cloak[raw + scenario][j];
                } else {
                    mapik[raw + scenario][j] = sensik_without_cloak[raw + scenario][j];
                }
            }
        }
        for (int j = col - 1; j < col + 2; j++) {
            if (j >= 0 && j < 9 && raw-2 >= 0 && raw-2 < 9) {
                if (cloak) {
                    mapik[raw - scenario][j] = sensik_with_cloak[raw - scenario][j];
                } else
                    mapik[raw - scenario][j] = sensik_without_cloak[raw - scenario][j];
                }
            }
        for (int i = raw - 1; i < raw + 2; i++) {
            if (i >= 0 && i < 9 && col - 2 >= 0 && col - 2 < 9) {
                if (cloak) {
                    mapik[i][col - scenario] = sensik_with_cloak[i][col - scenario];
                } else
                    mapik[i][col - scenario] = sensik_without_cloak[i][col - scenario];
                }
        }
        for (int i = raw - 1; i < raw + 2; i++) {
            if (i >= 0 && i < 9 && col + 2 >= 0 && col + 2 < 9) {
                if (cloak) {
                    mapik[i][col + scenario] = sensik_with_cloak[i][col + scenario];
                } else
                    mapik[i][col + scenario] = sensik_without_cloak[i][col + scenario];
            }
        }
        if (cloak) {
            mapik[raw][col] = sensik_with_cloak[raw][col];
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
    }

    /**
     * The function to generate the whole map of needed actors
     * @param in the Scanner object to read from the console
     */
    public static void map_generate_2(Scanner in) {
        int                 scenario = 0;
        map_initial();
        System.out.print("The case created:  ");
        for (Characters actor: Characters.values()) {
            map_generator(actor.getValue());
        }
        System.out.println();
        System.out.println("Please chose the perception scenario of the Harry Potter (1 or 2)");
        while (!Arrays.asList(new Integer[]{1, 2})
                .contains(in.hasNextInt() ? scenario = in.nextInt() : in.next())) {
            System.out.println("Error, input int-value!)");
            System.out.println("Enter 1 or 2:");
        }
        i_see= scenario;
    }

    /**
     * Function og placement of the actor to the map according to the rules
     * @param characters the actor we want to place on the map
     */
    public static void map_generator(int characters) {
        boolean approve = false;

        if (characters == Characters.Potter.getValue()) {
            ma[0][0].add((int)'P');
            System.out.print("[0,0]");
        } else {
            while (!approve) {
                int i = (int) (Math.random() * 9);
                int j = (int) (Math.random() * 9);
                if (characters == Characters.Filch.getValue()) {
                    if (i >= 3 || j >= 3) {
                        sensik_with_cloak[i][j] = 1;
                        set_sensik(i, j, 2);
                        approve = true;
                    }
                } else if (characters == Characters.Norris.getValue()) {
                    if (i >= 2 || j >= 2) {
                        sensik_with_cloak[i][j] = 1;
                        set_sensik(i, j, 1);
                        approve = true;
                    }
                } else {
                    if (sensik_without_cloak[i][j] == 0) {
                        if (characters == Characters.Door.getValue()) {
                            if (!ma[i][j].contains(Characters.Book.getValue())) {
                                AndreyStarodumov.door[0] = i;
                                AndreyStarodumov.door[1] = j;
                                approve = true;
                            }
                        } else {
                            approve = true;
                        }
                    }
                }
                if (approve) {
                    ma[i][j].add(characters);
                    System.out.print(" [" + i + "," + j + "]");
                }
            }
        }
    }

    /**
     * Function to save the memory of the Harry according to his perception
     * @param raw the raw index on the map where Harry is
     * @param col the column index on the map where Harry is
     * @param distance perception scenario
     */
    public static void set_sensik(int raw, int col, int distance) {
        for (int l = raw - distance; l <= raw + distance; l++) {
            for (int k = col - distance; k <= col + distance; k++) {
                if (l >= 0 && l < 9 && k >= 0 && k < 9) {
                    sensik_without_cloak[l][k] = 1;
                }
            }
        }
    }

    /**
     * Function to insert actor on the map
     * @param cords the coordinates of actors
     * @param output if false, we print the created case
     * @return true if the all actors are inserted according to the rules
     */
    public static boolean map_insert(ArrayList<Integer> cords, boolean output) {
        if (!output)
            System.out.print("The case created:   ");
        for (int i = 0; i < 6; i++) {
            if (!insert_actor(cords.get(i*2), cords.get(i*2 + 1), output, Characters.values()[i])) return (false);
        }
        return (true);
    }

    /**
     * Function to insert a character on the map
     * @param raw the raw index on the map where to insert a character
     * @param col the column index on the map where to insert a character
     * @param output if true, the function prints the actor fails or is OK during the insertion
     * @param actor a character to insert
     * @return tru if insertion is successful
     */
    public static boolean insert_actor(int raw, int col, boolean output, Characters actor) {
        if (actor == Characters.Potter) {
            if (raw != 0 || col != 0) {
                System.out.println(ANSI_RED + "Potter fails" + ANSI_RESET);
                return (false);
            }
            ma[0][0].add(actor.getValue());
        } else if (actor == Characters.Filch) {
            if (raw < 3 && col < 3) {
                System.out.println(ANSI_RED+ "Filch fails"+ANSI_RESET);
                return (false);
            }
            set_sensik(raw, col, 2);
            ma[raw][col].add(actor.getValue());
            sensik_with_cloak[raw][col] = 1;
        } else if (actor == Characters.Norris) {
            if (raw < 2 && col < 2) {
                System.out.println(ANSI_RED + "Norris fails" + ANSI_RESET);
                return (false);
            }
            ma[raw][col].add(actor.getValue());
            sensik_with_cloak[raw][col] = 1;
            set_sensik(raw, col, 1);
        } else {
            if (sensik_without_cloak[raw][col] == 1) {
                System.out.println(ANSI_RED+ actor.toString() + " fails"+ ANSI_RESET);
                return (false);
            }
            if (actor == Characters.Door) {
                if (ma[raw][col].contains(Characters.Book.getValue())) {
                    System.out.println(ANSI_RED + actor + " fails" + ANSI_RESET);
                    return (false);
                }
                AndreyStarodumov.door[0] = raw;
                AndreyStarodumov.door[1] = col;
            }
            ma[raw][col].add(actor.getValue());
        }
        if (output) System.out.println(ANSI_GREEN + actor + " is OK" + ANSI_RESET);
        else System.out.print(" [" + raw + "," + col + "]");
        return (true);
    }

    /**
     * Function to print the map with characters
     */
    public static void show_map() {
        System.out.println("________Heroes________");
        for (int i = 8; i >= 0; i--) {
            System.out.print(i + " | ");
            for (int j = 0; j < 9; j++) {
                if (ma[i][j].isEmpty()) {
                    System.out.print("_   ");
                } else {
                    String spaces = "    ";
                    for (int s : ma[i][j]) {
                        Character ca = (char) s;
                        System.out.print(ca);
                        spaces = spaces.substring(0, spaces.length() - 1);
                    }
                    System.out.print(spaces);
                }
            }
            System.out.println();
        }
        System.out.println("    ------------------------------------");
        System.out.print("    ");
        for (int j = 0; j < 9; j ++) {
            System.out.print(j + " | ");
        }
        System.out.println();
    }

    /**
     * Function to show where the Enemies are able to catch the Harry if he is without Cloak
     */
    public static void show_sense() {
        System.out.println("_________Sense_________");
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sensik_without_cloak[i][j] + "   ");
            }
            System.out.println();
        }
    }

    /**
     * Function to print the perception scenario and the coordinates of the DORO
     */
    public static void get_info() {
        System.out.println("Perception scenario   : " + AndreyStarodumov.i_see);
        System.out.println("The door's coordinates: [" + AndreyStarodumov.door[0] + "," + AndreyStarodumov.door[1] + "]");
    }

    /**
     * Function to chose how we want to create the map
     * @param in Scanner object to read from console
     * @return 1 if we want the automatic generation, 2 if manual one
     */
    public static int choose_map(Scanner in) {
        System.out.println("Choose the number (1 or 2) of how the map will be created:");
        System.out.println("1. Automatically generated");
        System.out.println("2. By hands");
        int map_creator = 0;
        while (!Arrays.asList(new Integer[]{1, 2})
                .contains(in.hasNextInt() ? map_creator = in.nextInt() : in.next())) {
            System.out.println("Error, input int-value!)");
            System.out.println("Enter 1 or 2:");
        }
        in.nextLine();
        return (map_creator);
    }

    /**
     * Function of manual creation of the car via entering coordinates of the characters
     * @param in Scanner object to read from console
     */
    public static void hand_map(Scanner in) {
        boolean correct = false;
        String data;
        int scenario;
        ArrayList<Integer> final_cords = new ArrayList<>();
        map_initial();

        while (!correct) {
            System.out.println();
            System.out.println("Print coordinates of the characters:");
            data = in.nextLine();
            System.out.println("Print the number of scenario of vision (1 or 2)");
            scenario = in.nextInt();
            in.nextLine();
            String regex = "\\[\\d,\\d]\\s\\[\\d,\\d]\\s\\[\\d,\\d]\\s\\[\\d,\\d]\\s\\[\\d,\\d]\\s\\[\\d,\\d]";
            String[] cord = (data.split("[\\Q, []\\E]"));
            if (!data.matches(regex)) {
                System.out.println();
                System.out.println("**********************************************");
                System.out.println("*      Your input must be the following:     *");
                System.out.println("*     [x,x] [x,x] [x,x] [x,x] [x,x] [x,x]    *");
                System.out.println("*      Where x - is a digit from 0 to 8      *");
                System.out.println("**********************************************");
                continue;
            }
            for (String s : cord) {
                if (!s.equals("")) {
                    final_cords.add(Integer.parseInt(s));
                }
            }
            if (final_cords.size() != 12 || !Arrays.asList(new Integer[]{1, 2}).contains(scenario)) {
                System.out.println();
                System.out.println("Your data is incorrect, try again:");
                System.out.println("**********************************Hints********************************");
                System.out.println("*   All coordinates are between [0,0] and [8,8]. There are 6 heroes.  *");
                System.out.println("*                   The scenario is 1 or 2 only.                      *");
                System.out.println("***********************************************************************");
                final_cords.clear();
            } else {
                if (check_map(final_cords)) {
                    map_initial();
                    renew_initials();
                    map_insert(final_cords, false);
                    AndreyStarodumov.i_see = scenario;
                    correct = true;
                } else {
                    final_cords.clear();
                    System.out.println();
                    System.out.println("Your data is incorrect, try again:");
                    System.out.println("**********************************Hints********************************");
                    System.out.println("*   All coordinates are between [0,0] and [8,8]. There are 6 heroes.  *");
                    System.out.println("*                   The scenario is 1 or 2 only.                      *");
                    System.out.println("***********************************************************************");
                }
            }
        }
    }

    /**
     * Function to check the coordinates of the characters
     * @param cords coordinates to check (if they satisfy the rules)
     * @return true if coordinates are correct
     */
    public static boolean check_map(ArrayList<Integer> cords) {
        for (int i = 0; i < 12; i++) {
            if (cords.get(i) < 0 || cords.get(i) > 8) {
                System.out.println(ANSI_RED + "Coordinate #" + (i + 1) + " fails" + ANSI_RESET);
                return (false);
            }
            System.out.println(ANSI_GREEN + "Coordinate #" + (i + 1) + " is OK" + ANSI_RESET);
        }
        return map_insert(cords, true);
    }
}

/**
 * Main class to start the program
 */
class Main {
    /**
     * Function to run the program
     * @param args all needed arguments
     */
    public static void main(String[] args) {
        AndreyStarodumov.run_program();
    }
}
