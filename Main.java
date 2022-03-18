/* ********************************************************** *
 *          Author:     Andrey Starodumov                     *
 *          Group:      BS20-05                               *
 *          Mail:       a.starodumov@innopolis.university     *
 *          Subject:    ItoAI                                 *
 * ********************************************************** */

package HarryPotter;

import java.util.*;

enum Characters {
    Potter (80), Filch (70), Norris (78), Book (66), Cloak (67), Door (68);
    private int Letter;
    Characters(int Letter) {
        this.Letter = Letter;
    }
    public int getValue() {
        return this.Letter;
    }
}

public class Main {
    static int                      i_see;
    static int[]                    door = new int[2];
    static int[][]                  carta = new int[9][9];
    static boolean                  caught = false;
    static ArrayList<int[]>         path_to_death = new ArrayList<>();
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

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        step_clear();
        if (choose_map(in) == 1) {
            map_generate_2(in);
            System.out.println("The map is automatically generated.");
        } else {
            System.out.println("Enter all data following the assignment rules.");
            hand_map(in);
            System.out.println("Your input data is completely correct!");
            System.out.println("Map is successfully created!");
        }
        show_sense(in);
        show_map(in);
        get_info();
        System.out.println();
        resulting_report(2, 1, timing(2, 1));
        renew_initials();
        resulting_report(1, 1, timing(1, 1));
        renew_initials();
        resulting_report(1, 2, timing(1, 2));
        renew_initials();
        resulting_report(2, 2, timing(2, 2));
    }

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
        min_path_cloak.clear();;
        min_path_book_door_with_cloak.clear();
        min_path_book_door_without_cloak.clear();
        min_path_book_cloak.clear();
        min_path_cloak_door.clear();
        min_path_cloak_book.clear();
        amount_of_steps = 0;
    }
    public static void resulting_report(int method, int scenario, long[] sum_up) {
        int path_steps = 0;
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
            System.out.println("It takes, seconds: " + (float)sum_up[1]/1000000000.0);
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
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Door:");
                System.out.print("   ");
                for (int[] num : min_path_book_door_without_cloak) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else if (sum_up[0] == 2) {
                System.out.print("From start to Cloak:");
                System.out.print("   ");
                for (int[] num : min_path_cloak) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Cloak to Book:");
                System.out.print("   ");
                for (int[] num : min_path_cloak_book) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Door:");
                System.out.print("   ");
                for (int[] num : min_path_book_door_with_cloak) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else if (sum_up[0] == 3){
                System.out.print("From start to Book:");
                System.out.print("   ");
                for (int[] num : min_path_book) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Book to Cloak:");
                System.out.print("   ");
                for (int[] num : min_path_book_cloak) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
                System.out.print("   From Cloak to Door:");
                System.out.print("   ");
                for (int[] num : min_path_cloak_door) {
                    System.out.print("[" + num[0] + "," + num[1] + "]->");
                    path_steps++;
                }
                path_steps--;
                System.out.println("\b\b");
            } else {
                System.out.println("Caught ");
            }
            if (scenario == 2) {
                System.out.println("4. Found solution (may not be optimal): " + path_steps + " steps");
            } else {
                System.out.println("4. Shortest path: " + path_steps + " steps");
            }
            System.out.print("5. ");
            System.out.println("It takes, seconds: " + (float) sum_up[1] / 1000000000.0);
        }
    }
    public static long[] timing(int method, int scenario) throws InterruptedException {
        long output;
        long start;
        long finish;
        long elapsed;
        if (method == 1) {
            if (scenario == 1) {
                start = System.nanoTime();
                output = initial_backtracking();
                Thread.sleep(1);
                finish = System.nanoTime();
            } else {
                start = System.nanoTime();
                output = initial_backtracking_2();
                Thread.sleep(1);
                finish = System.nanoTime();
            }
        } else {
            if (scenario == 1) {
                start = System.nanoTime();
                output = initial_bfs();
                Thread.sleep(1);
                finish = System.nanoTime();
            } else {
                start = System.nanoTime();
                output = initial_bfs_2();
                Thread.sleep(1);
                finish = System.nanoTime();
            }
        }
        elapsed = finish - start;
        return (new long[]{output, elapsed});
    }
    public static void step_clear() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                carta[i][j] = 1000000;
            }
        }
        path.clear();
        step = -1;
    }
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
    public static void sensik_initial() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sensik_without_cloak[i][j] = 0;
                sensik_with_cloak[i][j] = 0;
            }
        }
    }
    public static void visited_initial() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                visited[i][j] = 0;
            }
        }
    }

    /*bactracking with 1 scenario*/
    public static int initial_backtracking() {
        backik_book_without_cloak(0, 0);
        step_clear();
        if (min_path_book.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_cloak(0,0);
                step_clear();
                backik_book_with_cloak(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                        min_path_cloak.get(min_path_cloak.size() - 1)[1]);
                step_clear();
                backik_book_door_with_cloak(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                        min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
                step_clear();
                return 2;
            }
        }
        backik_book_door_without_cloak(min_path_book.get(min_path_book.size() - 1)[0],
                min_path_book.get(min_path_book.size() - 1)[1]);
        step_clear();
        if (min_path_book_door_without_cloak.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_book_cloak(min_path_book.get(min_path_book.size() - 1)[0],
                        min_path_book.get(min_path_book.size() - 1)[1]);
                step_clear();
                backik_cloak_door(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                        min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1]);
                backik_cloak(0,0);
                step_clear();
                backik_book_with_cloak(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                        min_path_cloak.get(min_path_cloak.size() - 1)[1]);
                step_clear();
                backik_book_door_with_cloak(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                        min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
                step_clear();
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                    min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
        backik_cloak(0,0);
        step_clear();
        if (min_path_cloak.isEmpty()) {
            return 1;
        } else {
            backik_book_with_cloak(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                    min_path_cloak.get(min_path_cloak.size() - 1)[1]);
            step_clear();
            backik_book_door_with_cloak(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                    min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
            step_clear();
            backik_book_cloak(min_path_book.get(min_path_book.size() - 1)[0],
                    min_path_book.get(min_path_book.size() - 1)[1]);
            step_clear();
            backik_cloak_door(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                    min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1]);
            step_clear();
            if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                    min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2<
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
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
    public static void backik_book_without_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_book.isEmpty()) {
            if (path.size() >= min_path_book.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_book = new ArrayList<>(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_book_without_cloak(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_book_with_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_cloak_book.isEmpty()) {
            if (path.size() >= min_path_cloak_book.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_cloak_book = new ArrayList<>(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                        if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                            backik_book_with_cloak(i, j);
                        }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_cloak.isEmpty()) {
            if (path.size() >= min_path_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_cloak = new ArrayList<>(path);
            cloak_found = true;
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_cloak(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_book_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_book_cloak.isEmpty()) {
            if (path.size() >= min_path_book_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_book_cloak = new ArrayList<>(path);
            cloak_found = true;
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_book_cloak(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_book_door_without_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_book_door_without_cloak.isEmpty()) {
            if (path.size() >= min_path_book_door_without_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_without_cloak = new ArrayList<>(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_book_door_without_cloak(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_book_door_with_cloak(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_book_door_with_cloak.isEmpty()) {
            if (path.size() >= min_path_book_door_with_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_with_cloak = new ArrayList<>(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_book_door_with_cloak(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }
    public static void backik_cloak_door(int raw, int col) {
        amount_of_steps++;
        step++;
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_cloak_door.isEmpty()) {
            if (path.size() >= min_path_cloak_door.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_cloak_door = new ArrayList<>(path);
            path.remove(path.size() - 1);
            step--;
            return ;
        }
        visited[raw][col] = 1;
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                        backik_cloak_door(i, j);
                    }
                }
            }
        }
        visited[raw][col] = 0;
        path.remove(path.size() - 1);
        step--;
    }

    /*BFS-algorithm with 1 scenario*/
    public static int initial_bfs() {
        queue.add(new int[]{0,0});
        while (!queue.isEmpty()) {
            bfs_book_without_cloak(queue.element().clone());
            queue.clear();
            visited_initial();
        }
        if (min_path_book.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                queue.add(new int[]{0,0});
                while (!queue.isEmpty()) {
                    bfs_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_book_with_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_book_door_with_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                return 2;
            }
        }
        queue.add(min_path_book.get(min_path_book.size() - 1).clone());
        while (!queue.isEmpty()) {
            bfs_book_door_without_cloak(queue.element().clone());
            queue.clear();
            visited_initial();
        }
        if (min_path_book_door_without_cloak.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                queue.add(min_path_book.get(min_path_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_book_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_book_cloak.get(min_path_book_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_cloak_door(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(new int[]{0,0});
                while (!queue.isEmpty()) {
                    bfs_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_book_with_cloak(queue.element().clone());
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    bfs_book_door_with_cloak(queue.element().clone());
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
            bfs_cloak(queue.element().clone());
            queue.clear();
            visited_initial();
        }
        if (min_path_cloak.isEmpty()) {
            return 1;
        } else {
            queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
            while (!queue.isEmpty()) {
                bfs_book_with_cloak(queue.element().clone());
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
            while (!queue.isEmpty()) {
                bfs_book_door_with_cloak(queue.element().clone());
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_book.get(min_path_book.size() - 1).clone());
            while (!queue.isEmpty()) {
                bfs_book_cloak(queue.element().clone());
                queue.clear();
                visited_initial();
            }
            queue.add(min_path_book_cloak.get(min_path_book_cloak.size() - 1).clone());
            while (!queue.isEmpty()) {
                bfs_cloak_door(queue.element().clone());
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
    public static boolean bfs_book_without_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_book.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_book_without_cloak(queue.element().clone())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_book.get(0)[0] +
                            min_path_book.get(0)[1]))) {
                        min_path_book.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_book_with_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_cloak_book.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_book_with_cloak(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_cloak_book.get(0)[0] +
                            min_path_cloak_book.get(0)[1]))) {
                        min_path_cloak_book.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_cloak.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_cloak(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_cloak.get(0)[0] +
                            min_path_cloak.get(0)[1]))) {
                        min_path_cloak.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_book_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_book_cloak.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_book_cloak(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_book_cloak.get(0)[0] +
                            min_path_book_cloak.get(0)[1]))) {
                        min_path_book_cloak.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_book_door_without_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_without_cloak.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_without_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_book_door_without_cloak(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_book_door_without_cloak.get(0)[0] +
                            min_path_book_door_without_cloak.get(0)[1]))) {
                        min_path_book_door_without_cloak.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_book_door_with_cloak(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_with_cloak.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_book_door_with_cloak(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_book_door_with_cloak.get(0)[0] +
                            min_path_book_door_with_cloak.get(0)[1]))) {
                        min_path_book_door_with_cloak.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }
    public static boolean bfs_cloak_door(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_cloak_door.add(0, queue_el.clone());
            return (true);
        }
        for (int i = raw + 1; i > raw - 2; i--) {
            for (int j = col - 1; j < col + 2; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (sensik_with_cloak[i][j] == 0 && visited[i][j] == 0) {
                        visited[i][j]= 1;
                        queue.add(new int[]{i,j});
                        hash_Set.add(Integer.toString(100+10*i+j));
                    }
                }
            }
        }
        queue.remove();
        while (!queue.isEmpty()) {
            if (bfs_cloak_door(queue.element())) {
                if (!hash_Set.isEmpty()) {
                    if (hash_Set.contains(Integer.toString(100 +
                            10 * min_path_cloak_door.get(0)[0] +
                            min_path_cloak_door.get(0)[1]))) {
                        min_path_cloak_door.add(0, new int[]{raw, col});
                    }
                }
                return true;
            }
        }
        return (false);
    }

    /* Backtracing 2 scenario*/
    public static int initial_backtracking_2() {
        boolean passed = false;
        backik_book_without_cloak_2(0, 0);
        if (caught) {
            return 10;
        }
        step_clear();
        if (min_path_book.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_cloak_2(0,0);
                if (caught) {
                    return 20;
                }
                step_clear();
                backik_book_with_cloak_2(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                        min_path_cloak.get(min_path_cloak.size() - 1)[1]);
                if (caught) {
                    return 21;
                }
                step_clear();
                backik_book_door_with_cloak_2(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                        min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
                if (caught) {
                    return 22;
                }
                step_clear();
                return 2;
            }
        }
        backik_book_door_without_cloak_2(min_path_book.get(min_path_book.size() - 1)[0],
                min_path_book.get(min_path_book.size() - 1)[1]);
        if (caught) {
            return 11;
        }
        step_clear();
        if (min_path_book_door_without_cloak.isEmpty()) {
            if (!cloak_found) return 0;
            else {
                backik_book_cloak_2(min_path_book.get(min_path_book.size() - 1)[0],
                        min_path_book.get(min_path_book.size() - 1)[1]);
                if (caught) {
                    return 31;
                }
                step_clear();
                backik_cloak_door_2(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                        min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1]);
                if (caught) {
                    return 32;
                }
                if (!min_path_cloak_door.isEmpty()) {
                    passed = true;
                }
                backik_cloak_2(0,0);
                if (caught) {
                    if (passed)
                        return 3;
                    else
                        return 20;
                }
                step_clear();
                backik_book_with_cloak_2(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                        min_path_cloak.get(min_path_cloak.size() - 1)[1]);
                if (caught) {
                    if (passed)
                        return 3;
                    else
                        return 21;
                }
                step_clear();
                backik_book_door_with_cloak_2(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                        min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
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
                if (min_path_book.size() + min_path_book_cloak.size() + min_path_cloak_door.size() - 2 <
                        min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
        backik_cloak_2(0,0);
        if (caught) {
            return 1;
        }
        step_clear();
        if (min_path_cloak.isEmpty()) {
            return 1;
        } else {
            backik_book_with_cloak_2(min_path_cloak.get(min_path_cloak.size() - 1)[0],
                    min_path_cloak.get(min_path_cloak.size() - 1)[1]);
            if (caught) {
                return 1;
            }
            step_clear();
            backik_book_door_with_cloak_2(min_path_cloak_book.get(min_path_cloak_book.size() - 1)[0],
                    min_path_cloak_book.get(min_path_cloak_book.size() - 1)[1]);
            if (caught) {
                return 1;
            }
            step_clear();
            backik_book_cloak_2(min_path_book.get(min_path_book.size() - 1)[0],
                    min_path_book.get(min_path_book.size() - 1)[1]);
            if (caught) {
                if (min_path_cloak.size() + min_path_cloak_book.size() + min_path_book_door_with_cloak.size() - 2<
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
                    return 2;
                }
                return 1;
            }
            step_clear();
            backik_cloak_door_2(min_path_book_cloak.get(min_path_book_cloak.size() - 1)[0],
                    min_path_book_cloak.get(min_path_book_cloak.size() - 1)[1]);
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
                        min_path_book.size() + min_path_book_door_without_cloak.size() - 2) {
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
    public static void backik_book_without_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, false);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});

        if (!min_path_book.isEmpty()) {
            if (path.size() >= min_path_book.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_book = new ArrayList<>(path);
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
                                backik_book_without_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_book_without_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_book_with_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_with_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_with_cloak[raw][col];
        }
        update_memory(raw, col, true);
        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_cloak_book.isEmpty()) {
            if (path.size() >= min_path_cloak_book.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_cloak_book = new ArrayList<>(path);
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
                                backik_book_with_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_book_with_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, false);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_cloak.isEmpty()) {
            if (path.size() >= min_path_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_cloak = new ArrayList<>(path);
            cloak_found = true;
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
                                backik_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_book_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, false);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});
        if (!min_path_book_cloak.isEmpty()) {
            if (path.size() >= min_path_book_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_book_cloak = new ArrayList<>(path);
            cloak_found = true;
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
                                backik_book_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_book_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_book_door_without_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, false);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});

        if (!min_path_book_door_without_cloak.isEmpty()) {
            if (path.size() >= min_path_book_door_without_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_without_cloak = new ArrayList<>(path);
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
                                backik_book_door_without_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_book_door_without_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_book_door_with_cloak_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, true);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});

        if (!min_path_book_door_with_cloak.isEmpty()) {
            if (path.size() >= min_path_book_door_with_cloak.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_with_cloak = new ArrayList<>(path);
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
                                backik_book_door_with_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_book_door_with_cloak_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
    public static void backik_cloak_door_2(int raw, int col) {
        Set<String> hashSet = new HashSet<>();
        boolean pathik = false;
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
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            path_to_death.add(0, new int[]{raw,col});
            path.remove(path.size() - 1);
            return ;
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
        update_memory(raw, col, true);

        if (step < carta[raw][col]) {
            carta[raw][col] = step;
        } else {
            step--;
            return ;
        }
        path.add(new int[]{raw, col});

        if (!min_path_cloak_door.isEmpty()) {
            if (path.size() >= min_path_cloak_door.size()) {
                path.remove(path.size() - 1);
                step--;
                return ;
            }
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_cloak_door = new ArrayList<>(path);
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
                                backik_cloak_door_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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
                                backik_cloak_door_2(i, j);
                                if (caught) {
                                    visited[raw][col] = 0;
//                                    path.remove(path.size() - 1);
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

    /*BFS-algorithm with 2 scenario*/
    public static int initial_bfs_2() {
        boolean passed = false;

        queue.add(new int[]{0,0});
        while (!queue.isEmpty()) {
            if (bfs_book_without_cloak_2(queue.element().clone())) {
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
                    if (bfs_cloak_2(queue.element().clone())) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) return 20;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak.get(min_path_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_book_with_cloak_2(queue.element().clone())) {
                        queue.clear();
                        visited_initial();
                    }
                    if (caught) return 21;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_cloak_book.get(min_path_cloak_book.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_book_door_with_cloak_2(queue.element().clone())) {
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
            if (bfs_book_door_without_cloak_2(queue.element().clone())) {
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
                    if (bfs_book_cloak_2(queue.element().clone())) {
                        queue.clear();
                    }
                    if (caught) return 31;
                    queue.clear();
                    visited_initial();
                }
                queue.add(min_path_book_cloak.get(min_path_book_cloak.size() - 1).clone());
                while (!queue.isEmpty()) {
                    if (bfs_cloak_door_2(queue.element().clone())) {
                        passed = true;
                        queue.clear();
                    }
                    if (caught) return 32;
                    queue.clear();
                    visited_initial();
                }
                queue.add(new int[]{0,0});
                while (!queue.isEmpty()) {
                    if (bfs_cloak_2(queue.element().clone())) {
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
                    if (bfs_book_with_cloak_2(queue.element().clone())) {
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
                    if (bfs_book_door_with_cloak_2(queue.element().clone())) {
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
            if (bfs_cloak_2(queue.element().clone())) {
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
                if (bfs_book_with_cloak_2(queue.element().clone())) {
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
                if (bfs_book_door_with_cloak_2(queue.element().clone())) {
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
                if (bfs_book_cloak_2(queue.element().clone())) {
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
                if (bfs_cloak_door_2(queue.element().clone())) {
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
    public static boolean bfs_path_2(int[] queue_el, boolean cloak_on, Characters what_to_find) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (cloak_on) {
            if (sensik_without_cloak[raw][col] == 1) {
                caught = true;
                return (false);
            }
        } else {
            if (sensik_with_cloak[raw][col] == 1) {
                caught = true;
                return (false);
            }
        }
        update_memory(raw, col, cloak_on);

        visited[raw][col] = 1;
        if (!cloak_on && !ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains(what_to_find.getValue())) {
            min_path_book.add(0, queue_el.clone());
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
                if (bfs_path_2(queue.element().clone(), cloak_on, what_to_find)) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_book.get(0)[0] +
                                min_path_book.get(0)[1]))) {
                            min_path_book.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }

    public static boolean bfs_book_without_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, false);

        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_book.add(0, queue_el.clone());
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
                if (bfs_book_without_cloak_2(queue.element().clone())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_book.get(0)[0] +
                                min_path_book.get(0)[1]))) {
                            min_path_book.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_book_with_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, true);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'B')) {
            min_path_cloak_book.add(0, queue_el.clone());
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
                if (bfs_book_with_cloak_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_cloak_book.get(0)[0] +
                                min_path_cloak_book.get(0)[1]))) {
                            min_path_cloak_book.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, false);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_cloak.add(0, queue_el.clone());
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
                if (bfs_cloak_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_cloak.get(0)[0] +
                                min_path_cloak.get(0)[1]))) {
                            min_path_cloak.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_book_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, false);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            min_path_book_cloak.add(0, queue_el.clone());
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
                if (bfs_book_cloak_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_book_cloak.get(0)[0] +
                                min_path_book_cloak.get(0)[1]))) {
                            min_path_book_cloak.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_book_door_without_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, false);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'C')) {
            cloak_found = true;
        }
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_without_cloak.add(0, queue_el.clone());
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
                if (bfs_book_door_without_cloak_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_book_door_without_cloak.get(0)[0] +
                                min_path_book_door_without_cloak.get(0)[1]))) {
                            min_path_book_door_without_cloak.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_book_door_with_cloak_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, true);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_book_door_with_cloak.add(0, queue_el.clone());
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
                if (bfs_book_door_with_cloak_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_book_door_with_cloak.get(0)[0] +
                                min_path_book_door_with_cloak.get(0)[1]))) {
                            min_path_book_door_with_cloak.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    public static boolean bfs_cloak_door_2(int[] queue_el) {
        amount_of_steps++;
        Set<String> hash_Set = new HashSet<>();
        int raw = queue_el[0];
        int col = queue_el[1];
        if (sensik_without_cloak[raw][col] == 1) {
            caught = true;
            return (false);
        }
        update_memory(raw, col, true);
        visited[raw][col] = 1;
        if (!ma[raw][col].isEmpty() && ma[raw][col].contains((int)'D')) {
            min_path_cloak_door.add(0, queue_el.clone());
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
                if (bfs_cloak_door_2(queue.element())) {
                    if (!hash_Set.isEmpty()) {
                        if (hash_Set.contains(Integer.toString(100 +
                                10 * min_path_cloak_door.get(0)[0] +
                                min_path_cloak_door.get(0)[1]))) {
                            min_path_cloak_door.add(0, new int[]{raw, col});
                        }
                    }
                    return true;
                }
                if (caught) return (false);
            }
        }
        return (false);
    }
    /*****************************************************************************************/
    public static void update_memory(int raw, int col, boolean cloak) {
        for (int j = col - 1; j < col + 2; j++) {
            if (j >= 0 && j < 9 && raw+2 >= 0 && raw+2 < 9) {
                if (cloak) {
                    mapik[raw + 2][j] = sensik_with_cloak[raw + 2][j];
                } else {
                    mapik[raw + 2][j] = sensik_without_cloak[raw + 2][j];
                }
            }
        }
        for (int j = col - 1; j < col + 2; j++) {
            if (j >= 0 && j < 9 && raw-2 >= 0 && raw-2 < 9) {
                if (cloak) {
                    mapik[raw - 2][j] = sensik_with_cloak[raw - 2][j];
                } else
                    mapik[raw - 2][j] = sensik_without_cloak[raw - 2][j];
                }
            }
        for (int i = raw - 1; i < raw + 2; i++) {
            if (i >= 0 && i < 9 && col - 2 >= 0 && col - 2 < 9) {
                if (cloak) {
                    mapik[i][col - 2] = sensik_with_cloak[i][col - 2];
                } else
                    mapik[i][col - 2] = sensik_without_cloak[i][col - 2];
                }
        }
        for (int i = raw - 1; i < raw + 2; i++) {
            if (i >= 0 && i < 9 && col + 2 >= 0 && col + 2 < 9) {
                if (cloak) {
                    mapik[i][col + 2] = sensik_with_cloak[i][col + 2];
                } else
                    mapik[i][col + 2] = sensik_without_cloak[i][col + 2];
            }
        }
        if (cloak) {
            mapik[raw][col] = sensik_with_cloak[raw][col];
        } else {
            mapik[raw][col] = sensik_without_cloak[raw][col];
        }
    }

    /**
     * The block of functions of automatic generation of the Game-map
     */
    public static void map_generate(Scanner in) {
        int                 scenario = 0;
        map_initial();
        System.out.print("The case created:   ");
        generate_Potter();
        generate_Filch();
        generate_Norris();
        generate_Book();
        generate_Cloak();
        generate_Exit();
        System.out.println("Please chose the perception scenario of the Harry Potter (1 or 2)");
        while (!Arrays.asList(new Integer[]{1, 2})
                .contains(in.hasNextInt() ? scenario = in.nextInt() : in.next())) {
            System.out.println("Error, input int-value!)");
            System.out.println("Enter 1 or 2:");
        }
        i_see= scenario;
    }

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
                                Main.door[0] = i;
                                Main.door[1] = j;
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

    public static void generate_Potter() {
        ma[0][0].add((int)'P');
        System.out.print("[0,0]");
    }

    public static void generate_Filch() {
        boolean approve = false;
        while (!approve) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (i >= 3 || j >= 3) {
                ma[i][j].add((int)'F');
                sensik_with_cloak[i][j] = 1;
                System.out.print(" [" + i + "," + j + "]");
                for (int l = i - 2; l <= i + 2; l++) {
                    for (int k = j - 2; k <= j + 2; k++) {
                        if (l >= 0 && l < 9 && k >= 0 && k < 9) {
                            sensik_without_cloak[l][k] = 1;
                        }
                    }
                }
                approve = true;
            }
        }
    }

    public static void generate_Norris() {
        boolean approve = false;
        while (!approve) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (i >= 2 || j >= 2) {
                ma[i][j].add((int)'N');
                System.out.print(" [" + i + "," + j + "]");
                sensik_with_cloak[i][j] = 1;
                for (int l = i - 1; l <= i + 1; l++) {
                    for (int k = j - 1; k <= j + 1; k++) {
                        if (l >= 0 && l < 9 && k >= 0 && k < 9) {
                            sensik_without_cloak[l][k] = 1;
                        }
                    }
                }
                approve = true;
            }
        }
    }
    public static void set_sensik(int raw, int col, int distance) {
        for (int l = raw - distance; l <= raw + distance; l++) {
            for (int k = col - distance; k <= col + distance; k++) {
                if (l >= 0 && l < 9 && k >= 0 && k < 9) {
                    sensik_without_cloak[l][k] = 1;
                }
            }
        }
    }

    public static void generate_Book() {
        boolean approve = false;
        while (!approve) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (sensik_without_cloak[i][j] == 0) {
                System.out.print(" [" + i + "," + j + "]");
                ma[i][j].add((int)'B');
                approve = true;
            }
        }
    }

    public static void generate_Exit() {
        boolean approve = false;
        while (!approve) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (!ma[i][j].contains((int)'B') && sensik_without_cloak[i][j] == 0) {
                System.out.println(" [" + i + "," + j + "]");
                Main.door[0] = i;
                Main.door[1] = j;
                System.out.println();
                ma[i][j].add((int)'D');
                approve = true;
            }
        }
    }

    public static void generate_Cloak() {
        boolean approve = false;
        while (!approve) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (sensik_without_cloak[i][j] == 0) {
                System.out.print(" [" + i + "," + j + "]");
                ma[i][j].add((int)'C');
                approve = true;
            }
        }
    }

    /**
     * The block of functions of manual creation of the Game-map
     */
    public static boolean map_insert(ArrayList<Integer> cords, boolean output) {
        if (!output)
            System.out.print("The case created:   ");
        for (int i = 0; i < 12; i += 2) {
            if (i == 0) {
                if (!insert_Potter(cords.get(i), cords.get(i + 1), output))
                    return (false);
            } else if (i == 2) {
                if (!insert_Filch(cords.get(i), cords.get(i + 1), output))
                    return (false);
            } else if (i == 4) {
                if (!insert_Norris(cords.get(i), cords.get(i + 1), output))
                    return (false);
            } else if (i == 6) {
                if (!insert_Book(cords.get(i), cords.get(i + 1), output))
                    return (false);
            } else if (i == 8) {
                if (!insert_Cloak(cords.get(i), cords.get(i + 1), output))
                    return (false);
            } else if (i == 10) {
                if (!insert_Exit(cords.get(i), cords.get(i + 1), output))
                    return (false);
            }
        }
        return (true);
    }

    public static boolean insert_Potter(int i, int j, boolean output) {
        if (i != 0 || j != 0) {
            System.out.println("Potter fails");
            return (false);
        }
        ma[0][0].add((int)'P');
        if (output)
            System.out.println("Potter OK");
        else
            System.out.print("[" + i + "," + j + "]");
        return (true);
    }

    public static boolean insert_Filch(int i, int j, boolean output) {
        if ((i < 3 && j < 3) || !ma[i][j].isEmpty()) {
            System.out.println("Filch fails");
            return (false);
        }
        for (int l = i - 2; l <= i + 2; l++) {
            for (int k = j - 2; k <= j + 2; k++) {
                if (l >= 0 && l < 9 && k >= 0 && k < 9) {
                    sensik_without_cloak[l][k] = 1;
                }
            }
        }
        ma[i][j].add((int)'F');
        sensik_with_cloak[i][j] = 1;
        if (output)
            System.out.println("Filch OK");
        else
            System.out.print(" [" + i + "," + j + "]");
        return (true);
    }

    public static boolean insert_Norris(int i, int j, boolean output) {
        if ((i < 2 && j < 2) || ma[i][j].contains((int)'P')) {
            System.out.println("Norris fails");
            return (false);
        }
        ma[i][j].add((int)'N');
        sensik_with_cloak[i][j] = 1;
        for (int l = i - 1; l <= i + 1; l++) {
            for (int k = j - 1; k <= j + 1; k++) {
                if (l >= 0 && l < 9 && k >= 0 && k < 9) {
//                        bat[l][k].set_sense();
                    sensik_without_cloak[l][k] = 1;
                }
            }
        }
        if (output)
            System.out.println("Norris OK");
        else
            System.out.print(" [" + i + "," + j + "]");
        return (true);
    }

    public static boolean insert_Book(int i, int j, boolean output) {
        if (sensik_without_cloak[i][j] == 1) {
            System.out.println("Book fails");
            return (false);
        }
        ma[i][j].add((int)'B');
        if (output)
            System.out.println("Book OK");
        else
            System.out.print(" [" + i + "," + j + "]");
        return (true);
    }

    public static boolean insert_Cloak(int i, int j, boolean output) {
        if (sensik_without_cloak[i][j] == 1) {
            System.out.println("Cloak fails");
            return (false);
        }
        ma[i][j].add((int)'C');
        if (output)
            System.out.println("Cloak OK");
        else
            System.out.print(" [" + i + "," + j + "]");
        return (true);
    }

    public static boolean insert_Exit(int i, int j, boolean output) {
        if (ma[i][j].contains((int)'B') || sensik_without_cloak[i][j] == 1) {
            System.out.println("Exit fails");
            return (false);
        }
        ma[i][j].add((int)'D');
        if (output)
            System.out.println("Exit OK");
        else {
            Main.door[0] = i;
            Main.door[1] = j;
            System.out.println(" [" + i + "," + j + "]");
            System.out.println();
        }
        return (true);
    }

    public static void show_map(Scanner in) {
        System.out.println("________Heroes________");
        for (int i = 8; i >= 0; i--) {
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
    }

    public static void show_sense(Scanner in) {
        System.out.println("_________Sense_________");
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sensik_without_cloak[i][j] + "   ");
            }
            System.out.println();
        }
    }

    public static void get_info() {
        System.out.println("Perception scenario   : " + Main.i_see);
        System.out.println("The door's coordinates: [" + Main.door[0] + "," + Main.door[1] + "]");
    }

    /**
     * Function for choosing the way of creation of the map
     *
     * @param in Scanner object
     * @return number of how to generate the map (manually or automatically)
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
            String[] cord = (data.split("[\\Q, []\\E]"));
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
                    Main.i_see = scenario;
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

    public static boolean check_map(ArrayList<Integer> cords) {
        for (int i = 0; i < 12; i++) {
            if (cords.get(i) < 0 || cords.get(i) > 8) {
                return (false);
            }
            System.out.println("Coordinate #" + (i + 1) + " is OK");
        }
        return map_insert(cords, true);
    }
}
